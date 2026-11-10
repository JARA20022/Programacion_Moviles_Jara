package com.tecsup.productos.data.repository

import com.tecsup.productos.data.mapper.aItem
import com.tecsup.productos.data.remote.ApiService
import com.tecsup.productos.domain.model.Item
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class ItemRepositoryImpl(private val api: ApiService) : ItemRepository {
    // H6 mantiene la lista en memoria. H9 la conectará a Room.
    private val datos = MutableStateFlow<List<Item>>(emptyList())
    override val items: Flow<List<Item>> = datos.asStateFlow()
    override val favoritos: Flow<List<Item>> = items.map { lista ->
        lista.filter(Item::esFavorito)
    }

    override suspend fun refrescar(): Result<Int> = consultarApi {
        val productos = api.listar().products
        datos.update { anteriores ->
            val idsFavoritos = anteriores.filter(Item::esFavorito).map(Item::id).toSet()
            productos.map { dto -> dto.aItem(esFavorito = dto.id in idsFavoritos) }
        }
        productos.size
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = consultarApi {
        val esFavorito = datos.value.any { it.id == id && it.esFavorito }
        api.detalle(id).aItem(esFavorito)
    }

    override suspend fun alternarFavorito(id: Int) {
        datos.update { lista ->
            lista.map { item ->
                if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
            }
        }
    }

    override suspend fun cantidadGuardada(): Int = datos.value.size

    override suspend fun cantidadFavoritos(): Int = datos.value.count(Item::esFavorito)

    override suspend fun limpiarCache() {
        datos.value = emptyList()
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
