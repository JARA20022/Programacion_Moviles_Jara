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
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

class LavaRepositoryRoom(
    private val db: LavaDatabase
) : LavaRepository {
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
    ): Usuario? = usuarios.login(correo, clave)

    override fun observarClientes(): Flow<List<ClienteResumen>> =
        usuarios.observarClientes()

    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> =
        servicios.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> =
        servicios.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            servicios.insertar(s)
        } else {
            servicios.actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicios.eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        servicios.cambiarActivo(id, activo)
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

        // Los precios se consultan en Room dentro de la transacción.
        val detallesNuevos = lineas.map { linea ->
            val servicio = servicios.obtener(linea.servicioId)
                ?: error("No existe el servicio ${linea.servicioId}.")

            DetallePedido(
                pedidoId = 0,
                servicioId = servicio.id,
                cantidad = linea.cantidad,
                precioUnitario = servicio.precio,
                subtotal = linea.cantidad * servicio.precio
            )
        }

        val pedidoId = pedidos.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = detallesNuevos.sumOf { it.subtotal },
                casillero = null,
                pagado = false,
                nota = nota
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
