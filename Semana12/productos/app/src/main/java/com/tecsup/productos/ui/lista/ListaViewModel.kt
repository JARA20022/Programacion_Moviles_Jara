package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
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
    private var carga: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        carga?.cancel()
        carga = viewModelScope.launch {
            _estado.value = UiState.Cargando

            val error = repositorio.refrescar().exceptionOrNull()
            if (error != null) {
                _estado.value = UiState.Error(error.message ?: "No se pudo cargar el catálogo.")
                return@launch
            }

            repositorio.items.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }
}
