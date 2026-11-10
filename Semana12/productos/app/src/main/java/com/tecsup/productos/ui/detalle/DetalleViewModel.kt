package com.tecsup.productos.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val id: Int,
    private val repositorio: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()
    private var carga: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        if (carga?.isActive == true) return

        carga = viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = repositorio.obtenerDetalle(id).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = {
                    UiState.Error(it.message ?: "No se pudo cargar el detalle.")
                }
            )
        }
    }
}
