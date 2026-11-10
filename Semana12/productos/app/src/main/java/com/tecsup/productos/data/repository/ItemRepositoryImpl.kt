package com.tecsup.productos.data.repository

import androidx.room.withTransaction
import com.tecsup.productos.data.local.AppDatabase
import com.tecsup.productos.data.mapper.aEntity
import com.tecsup.productos.data.mapper.aItem
import com.tecsup.productos.data.mapper.aModelo
import com.tecsup.productos.data.remote.ApiService
import com.tecsup.productos.domain.model.Item
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val baseDatos: AppDatabase
) : ItemRepository {
    private val dao = baseDatos.itemDao()
    private val sincronizacion = Mutex()

    override val items: Flow<List<Item>> = dao.todos().map { lista ->
        lista.map { it.aModelo() }
    }

    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista ->
        lista.map { it.aModelo() }
    }

    override suspend fun refrescar(): Result<Int> = consultarApi {
        sincronizacion.withLock {
            val productos = api.listar().products
            baseDatos.withTransaction {
                val idsFavoritos = dao.idsFavoritos().toSet()
                dao.guardarTodos(
                    productos.map { dto -> dto.aEntity(esFavorito = dto.id in idsFavoritos) }
                )
            }
            productos.size
        }
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = consultarApi {
        val producto = api.detalle(id)
        producto.aItem(esFavorito = id in dao.idsFavoritos())
    }

    override suspend fun alternarFavorito(id: Int) {
        dao.alternarFavorito(id)
    }

    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() {
        sincronizacion.withLock {
            dao.borrarTodo()
        }
    }

    private suspend fun <T> consultarApi(consulta: suspend () -> T): Result<T> = try {
        Result.success(consulta())
    } catch (error: IOException) {
        Result.failure(IllegalStateException("No se pudo conectar. Revisa tu conexión.", error))
    } catch (error: HttpException) {
        Result.failure(IllegalStateException("El servidor respondió con error HTTP ${error.code()}.", error))
    } catch (error: SerializationException) {
        Result.failure(IllegalStateException("El catálogo llegó en un formato inesperado.", error))
    }
}
