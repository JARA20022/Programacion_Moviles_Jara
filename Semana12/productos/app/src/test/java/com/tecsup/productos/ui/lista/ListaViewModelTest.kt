package com.tecsup.productos.ui.lista

import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    private val telefono = Item(1, "Teléfono de prueba", "Marca", null)
    private val mensajes = mutableListOf<String>()
    private val notificador = object : Notificador {
        override fun mostrar(titulo: String, texto: String) {
            mensajes += texto
        }
    }
    private var viewModel: ListaViewModel? = null

    @Before
    fun configurarMain() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun liberarMain() {
        viewModel?.viewModelScope?.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun cargaExitosaMuestraCatalogoYNotifica() = runTest {
        val lista = listOf(telefono)
        val modelo = crear(RepositorioFalso(lista, Result.success(1)))

        assertEquals(UiState.Cargando, modelo.estado.value)
        advanceUntilIdle()

        assertEquals(UiState.Exito(lista), modelo.estado.value)
        assertNull(modelo.aviso.value)
        assertEquals(listOf("Se guardaron 1 elementos"), mensajes)
    }

    @Test
    fun sinConexionConCacheMuestraCatalogoGuardado() = runTest {
        val lista = listOf(telefono)
        val fallo = Result.failure<Int>(IllegalStateException("offline", IOException()))
        val modelo = crear(RepositorioFalso(lista, fallo))

        advanceUntilIdle()

        assertEquals(UiState.Exito(lista), modelo.estado.value)
        assertEquals("Sin conexión. Mostrando productos guardados.", modelo.aviso.value)
        assertTrue(mensajes.isEmpty())
    }

    @Test
    fun sinConexionSinCacheMuestraErrorYPermiteReintentar() = runTest {
        val repo = RepositorioFalso(emptyList(), Result.failure(IOException()))
        val modelo = crear(repo)

        advanceUntilIdle()
        assertEquals(
            UiState.Error("Revisa tu conexión. Aún no hay productos guardados en este dispositivo."),
            modelo.estado.value
        )
        assertNull(modelo.aviso.value)

        repo.items.value = listOf(telefono)
        repo.resultado = Result.success(1)
        modelo.cargar()
        advanceUntilIdle()

        assertEquals(UiState.Exito(listOf(telefono)), modelo.estado.value)
    }

    private fun crear(repo: ItemRepository): ListaViewModel =
        ListaViewModel(repo, notificador).also { viewModel = it }

    private class RepositorioFalso(
        inicial: List<Item>,
        var resultado: Result<Int>
    ) : ItemRepository {
        override val items = MutableStateFlow(inicial)
        override val favoritos = items.map { lista -> lista.filter { it.esFavorito } }

        override suspend fun refrescar(): Result<Int> = resultado

        override suspend fun obtenerDetalle(id: Int): Result<Item> =
            runCatching { items.value.first { it.id == id } }

        override suspend fun alternarFavorito(id: Int) {
            items.value = items.value.map { item ->
                if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
            }
        }

        override suspend fun cantidadGuardada(): Int = items.value.size
        override suspend fun cantidadFavoritos(): Int = items.value.count { it.esFavorito }

        override suspend fun limpiarCache() {
            items.value = emptyList()
        }
    }
}
