package com.jara.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?


    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE rol = 'SOPORTE' ORDER BY nombre")
    fun observarSoportes(): Flow<List<Usuario>>

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun contarPorCorreo(correo: String): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE codigo = :codigo")
    suspend fun contarPorCodigo(codigo: String): Int

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query(
        """
        SELECT
            u.id AS id,
            u.nombre AS nombre,
            u.codigo AS codigo,
            u.correo AS correo,
            COUNT(CASE WHEN p.estado != 'CANCELADO' THEN 1 END) AS cantidadPedidos,
            COALESCE(
                SUM(CASE WHEN p.pagado = 1 THEN p.total ELSE 0.0 END),
                0.0
            ) AS totalGastado
        FROM usuarios AS u
        LEFT JOIN pedidos AS p ON p.usuarioId = u.id
        WHERE u.rol = 'CLIENTE'
        GROUP BY u.id, u.nombre, u.codigo, u.correo
        ORDER BY u.nombre
        """
    )
    fun observarClientes(): Flow<List<ClienteResumen>>
}
