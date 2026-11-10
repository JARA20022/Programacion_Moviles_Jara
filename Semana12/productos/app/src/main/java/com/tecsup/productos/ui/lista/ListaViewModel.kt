package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaViewModel(
    private val cargarProductos: () -> Result<List<Item>>
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        _estado.value = UiState.Cargando
        _estado.value = cargarProductos().fold(
            onSuccess = { UiState.Exito(it) },
            onFailure = { UiState.Error("No se pudo cargar el catálogo.") }
        )
    }
}
