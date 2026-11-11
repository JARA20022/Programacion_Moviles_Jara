package com.tecsup.productos.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repositorio: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()
    private var observacion: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        observacion?.cancel()
        observacion = viewModelScope.launch {
            _estado.value = UiState.Cargando
            _aviso.value = null
            repositorio.favoritos
                .catch {
                    _estado.value = UiState.Error("No se pudieron leer los favoritos guardados.")
                }
                .collect { lista ->
                    _estado.value = UiState.Exito(lista)
                }
        }
    }

    fun quitarFavorito(id: Int) {
        viewModelScope.launch {
            _aviso.value = null
            try {
                repositorio.alternarFavorito(id)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _aviso.value = "No se pudo quitar el favorito. Inténtalo otra vez."
            }
        }
    }
}
