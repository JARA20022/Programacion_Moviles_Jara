package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.*
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun editarTitulo(id: Int, titulo: String): Result<Post>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    override suspend fun listar() = llamar { api.listar() }
    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }
    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }
    override suspend fun editarTitulo(id: Int, titulo: String) =
        llamar { api.editarTitulo(id, CambioTitulo(titulo)) }

    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}
