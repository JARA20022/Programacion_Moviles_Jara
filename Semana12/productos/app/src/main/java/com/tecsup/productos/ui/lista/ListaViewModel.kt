package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repositorio: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _avisoFavorito = MutableStateFlow<String?>(null)
    val avisoFavorito: StateFlow<String?> = _avisoFavorito.asStateFlow()
    private var carga: Job? = null

    init {
        cargar()
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            _avisoFavorito.value = null
            try {
                repositorio.alternarFavorito(id)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _avisoFavorito.value = "No se pudo actualizar el favorito. Inténtalo otra vez."
            }
        }
    }

    fun cargar() {
        carga?.cancel()
        carga = viewModelScope.launch {
            _estado.value = UiState.Cargando
            _aviso.value = null

            try {
                val error = repositorio.refrescar().exceptionOrNull()
                repositorio.items.collect { lista ->
                    if (error != null && lista.isEmpty()) {
                        _estado.value = UiState.Error(
                            error.message ?: "No se pudo descargar el catálogo y no hay datos guardados."
                        )
                        _aviso.value = null
                    } else {
                        _estado.value = UiState.Exito(lista)
                        _aviso.value = when {
                            error == null -> null
                            error is IOException || error.cause is IOException ->
                                "Sin conexión. Mostrando productos guardados."
                            else -> "No se pudo actualizar. Mostrando productos guardados."
                        }
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _estado.value = UiState.Error("No se pudo leer el catálogo guardado. Inténtalo otra vez.")
                _aviso.value = null
            }
        }
    }
}
