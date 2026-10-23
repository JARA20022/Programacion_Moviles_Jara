package com.jara.lavacampus.data.repository

import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.model.ClienteResumen
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.model.IngresoServicio
import com.jara.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    // Permite validar y guardar una operación con una misma lectura consistente.
    suspend fun <T> enTransaccion(bloque: suspend () -> T): T
    suspend fun obtenerPedido(id: Int): PedidoConDetalle?
    suspend fun obtenerCasillerosOcupados(): List<String>


    suspend fun obtenerUsuario(id: Int): Usuario?

    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    fun observarClientes(): Flow<List<ClienteResumen>>

    // Servicios
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneDetalles(id: Int): Boolean

    // Pedidos
    suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long

    suspend fun contarActivos(usuarioId: Int): Int
    fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidos(): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>

    suspend fun cambiarEstado(
        id: Int,
        estado: EstadoPedido,
        casillero: String?
    )

    suspend fun marcarPagado(id: Int, pagado: Boolean)
    fun observarCasillerosOcupados(): Flow<List<String>>

    // Panel
    fun observarPedidosDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>

    fun observarIngresosPorServicio(
        fechaInicio: String
    ): Flow<List<IngresoServicio>>
}
