package com.jara.lavacampus.data.repository

import androidx.room.withTransaction
import com.jara.lavacampus.data.local.LavaDatabase
import com.jara.lavacampus.data.local.entity.DetallePedido
import com.jara.lavacampus.data.local.entity.Pedido
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.model.ClienteResumen
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.model.IngresoServicio
import com.jara.lavacampus.model.LineaPedido
import com.jara.lavacampus.model.SolicitudSoporteResumen
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.model.PreciosCampus
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

class LavaRepositoryRoom(
    private val db: LavaDatabase
) : LavaRepository {
    override fun observarIdsAtrasados(hoy: String): Flow<List<Int>> = db.pedidoDao().observarIdsAtrasados(hoy)

    override suspend fun obtenerUsuarioPorRol(rol: String): Usuario? = db.usuarioDao().obtenerPorRol(rol)
    override suspend fun obtenerServicio(id: Int): Servicio? = db.servicioDao().obtener(id)

    override suspend fun <T> enTransaccion(bloque: suspend () -> T): T = db.withTransaction { bloque() }
    override suspend fun obtenerPedido(id: Int): PedidoConDetalle? = db.pedidoDao().obtenerConDetalle(id)
    override suspend fun obtenerCasillerosOcupados(): List<String> = db.pedidoDao().obtenerCasillerosOcupados()


    override suspend fun obtenerUsuario(id: Int): Usuario? = db.usuarioDao().obtener(id)

    private val usuarios = db.usuarioDao()
    private val servicios = db.servicioDao()
    private val pedidos = db.pedidoDao()
    private val detalles = db.detalleDao()

    // Usuarios
    override suspend fun login(
        correo: String,
        clave: String
    ): Usuario? {
        require(Validaciones.errorLogin(correo, clave) == null) { "Correo o contraseña con formato inválido." }
        return usuarios.login(Validaciones.normalizarCorreo(correo), clave)
    }

    override fun observarClientes(): Flow<List<ClienteResumen>> =
        usuarios.observarClientes()

    override fun observarSoportes(): Flow<List<Usuario>> =
        usuarios.observarSoportes()

    override suspend fun registrarSoporte(
        codigo: String,
        nombre: String,
        apellido: String,
        correo: String,
        clave: String,
        actorId: Int
    ) {
        db.withTransaction {
            val actor = usuarios.obtener(actorId)
            require(actor?.rol == "ADMIN") { "Solo el administrador puede registrar usuarios de soporte." }
            
            require(Validaciones.errorCodigoSoporte(codigo) == null) { Validaciones.errorCodigoSoporte(codigo).orEmpty() }
            require(Validaciones.errorNombre(nombre) == null) { Validaciones.errorNombre(nombre).orEmpty() }
            require(Validaciones.errorNombre(apellido) == null) { Validaciones.errorNombre(apellido).orEmpty() }
            require(Validaciones.correoValido(correo)) { "Correo inválido." }
            require(Validaciones.errorClaveSoporte(clave) == null) { Validaciones.errorClaveSoporte(clave).orEmpty() }

            val nCorreo = Validaciones.normalizarCorreo(correo)
            require(usuarios.contarPorCorreo(nCorreo) == 0) { "El correo ya está en uso." }
            require(usuarios.contarPorCodigo(codigo) == 0) { "El código ya está en uso." }

            val nNombre = Validaciones.normalizarNombre(nombre)
            val nApellido = Validaciones.normalizarNombre(apellido)

            usuarios.insertar(
                Usuario(
                    nombre = nNombre,
                    apellido = nApellido,
                    codigo = codigo,
                    correo = nCorreo,
                    clave = clave,
                    rol = "SOPORTE"
                )
            )
        }
    }

    override fun observarSolicitudesSoporte(): Flow<List<SolicitudSoporteResumen>> =
        detalles.observarSolicitudesSoporte()

    override suspend fun responderSolicitud(
        detalleId: Int,
        estadoAtencion: String,
        respuesta: String,
        soporteId: Int,
        fecha: String
    ) = db.withTransaction {
        val actor = usuarios.obtener(soporteId)
        require(actor?.rol == "SOPORTE") { "Solo el soporte puede responder estas solicitudes." }
        require(estadoAtencion in listOf("PENDIENTE", "EN_ATENCION", "ATENDIDA")) { "Estado inválido." }
        if (estadoAtencion == "ATENDIDA") {
            require(Validaciones.errorRespuestaSoporte(respuesta) == null) { "Respuesta inválida para marcar como ATENDIDA." }
        }
        val limpia = respuesta.trim()
        if (limpia.isNotEmpty()) {
            require(Validaciones.errorRespuestaSoporte(limpia) == null) { Validaciones.errorRespuestaSoporte(limpia).orEmpty() }
        }
        detalles.responderSolicitud(detalleId, estadoAtencion, limpia.takeIf { it.isNotEmpty() } ?: "", soporteId, fecha)
    }

    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> =
        servicios.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> =
        servicios.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        Validaciones.validarServicio(s)
        db.withTransaction {
            val limpio = s.copy(nombre = Validaciones.normalizarNombre(s.nombre), descripcion = s.descripcion.trim())
            require(servicios.obtenerTodos().none { it.id != s.id && Validaciones.claveNombre(it.nombre) == Validaciones.claveNombre(limpio.nombre) }) { "Ya existe un servicio con ese nombre." }
            if (limpio.id == 0) servicios.insertar(limpio)
            else {
                requireNotNull(servicios.obtener(limpio.id)) { "El servicio ya no existe." }
                servicios.actualizar(limpio)
            }
        }
    }

    override suspend fun eliminarServicio(s: Servicio) {
        db.withTransaction {
            require(s.id > 0) { "Servicio inválido." }
            require(detalles.contarPorServicio(s.id) == 0) { "El servicio ya se usa en pedidos. Solo puedes desactivarlo." }
            requireNotNull(servicios.obtener(s.id)) { "El servicio ya no existe." }
            servicios.eliminar(s)
        }
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        db.withTransaction {
            require(id > 0) { "Servicio inválido." }
            val actual = requireNotNull(servicios.obtener(id)) { "El servicio ya no existe." }
            if (activo) Validaciones.validarServicio(actual)
            servicios.cambiarActivo(id, activo)
        }
    }

    override suspend fun servicioTieneDetalles(id: Int): Boolean =
        detalles.contarPorServicio(id) > 0

    // Pedidos
    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long = db.withTransaction {

        require(usuarioId > 0 && usuarios.obtener(usuarioId)?.rol == "CLIENTE") { "Estudiante inválido." }
        Validaciones.validarLineas(lineas)
        require(Validaciones.errorTexto(nota, "La nota") == null) { "La nota no es válida." }
        require(pedidos.contarActivos(usuarioId) < 3) { "Ya tienes 3 pedidos activos" }
        val elegidos = lineas.map { requireNotNull(servicios.obtener(it.servicioId)) { "Un servicio ya no existe." } }
        val fechaEsperada = Validaciones.fechaEntrega(elegidos.maxOf { it.horasEntrega }).toString()
        require(fechaEntrega == fechaEsperada) { "La fecha de entrega no corresponde a los servicios elegidos." }

        // El total y el descuento se recalculan con tarifas vigentes dentro de Room.
        val precio = PreciosCampus.calcular(elegidos, lineas.associate { it.servicioId to it.cantidad })
        val detallesNuevos = precio.lineas.map { linea ->
            DetallePedido(pedidoId=0,servicioId=linea.servicioId,cantidad=linea.cantidad,
                precioUnitario=linea.precioUnitario,subtotal=linea.neto,descuento=linea.descuento)
        }

        val pedidoId = pedidos.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = precio.total, subtotal = precio.subtotal, descuento = precio.descuento,
                casillero = null,
                pagado = false,
                nota = nota.trim()
            )
        )

        detalles.insertarTodos(
            detallesNuevos.map { detalle ->
                detalle.copy(pedidoId = pedidoId.toInt())
            }
        )

        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int =
        pedidos.contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(
        usuarioId: Int
    ): Flow<List<PedidoConDetalle>> =
        pedidos.observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> =
        pedidos.observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> =
        pedidos.observarUno(id)

    override suspend fun cambiarEstado(
        id: Int,
        estado: EstadoPedido,
        casillero: String?
    ) {
        // Las reglas para avanzar de estado corresponden al ViewModel.
        pedidos.actualizarEstado(id, estado.name, casillero)
    }

    override suspend fun marcarPagado(id: Int, pagado: Boolean) {
        pedidos.actualizarPagado(id, pagado)
    }

    override fun observarCasillerosOcupados(): Flow<List<String>> =
        pedidos.observarCasillerosOcupados()

    // Panel
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> =
        pedidos.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(
        estado: EstadoPedido
    ): Flow<Int> = pedidos.contarPorEstado(estado.name)

    override fun observarIngresosDesde(
        fechaInicio: String
    ): Flow<Double> = pedidos.ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(
        fechaInicio: String
    ): Flow<List<IngresoServicio>> =
        detalles.observarIngresosPorServicio(fechaInicio)
}
