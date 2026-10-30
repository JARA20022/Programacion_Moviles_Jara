package com.jara.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jara.lavacampus.data.local.entity.DetallePedido
import com.jara.lavacampus.model.IngresoServicio
import com.jara.lavacampus.model.SolicitudSoporteResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleDao {

    @Insert
    suspend fun insertarTodos(lista: List<DetallePedido>)

    @Query(
        """
        SELECT 
            d.id AS detalleId,
            p.id AS pedidoId,
            u.nombre AS nombreEstudiante,
            d.descripcionOtros,
            p.fechaRegistro,
            p.estado AS estadoPedido,
            d.estadoAtencion
        FROM detalles_pedido d
        INNER JOIN pedidos p ON d.pedidoId = p.id
        INNER JOIN usuarios u ON p.usuarioId = u.id
        WHERE d.descripcionOtros IS NOT NULL
        ORDER BY p.id DESC
        """
    )
    fun observarSolicitudesSoporte(): Flow<List<SolicitudSoporteResumen>>

    @Query(
        """
        UPDATE detalles_pedido 
        SET estadoAtencion = :estadoAtencion, respuestaSoporte = :respuesta, soporteId = :soporteId, fechaRespuesta = :fecha
        WHERE id = :detalleId
        """
    )
    suspend fun responderSolicitud(detalleId: Int, estadoAtencion: String, respuesta: String, soporteId: Int, fecha: String)

    @Query(
        "SELECT COUNT(*) FROM detalles_pedido WHERE servicioId = :servicioId"
    )
    suspend fun contarPorServicio(servicioId: Int): Int

    @Query(
        """
        SELECT
            s.nombre AS nombre,
            SUM(d.subtotal) AS total
        FROM detalles_pedido AS d
        INNER JOIN servicios AS s ON d.servicioId = s.id
        INNER JOIN pedidos AS p ON d.pedidoId = p.id
        WHERE p.pagado = 1
          AND p.fechaRegistro >= :fechaInicio
        GROUP BY s.id, s.nombre
        ORDER BY total DESC
        """
    )
    fun observarIngresosPorServicio(
        fechaInicio: String
    ): Flow<List<IngresoServicio>>
}
