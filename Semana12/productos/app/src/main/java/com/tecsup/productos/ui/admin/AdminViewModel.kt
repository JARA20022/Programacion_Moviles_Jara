package com.tecsup.productos.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class ResumenAdmin(val totalGuardado: Int, val totalFavoritos: Int)

class AdminViewModel(
    private val repositorio: ItemRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<ResumenAdmin>>(UiState.Cargando)
    val estado: StateFlow<UiState<ResumenAdmin>> = _estado.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()
    private var observacion: Job? = null
    private var operacion: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        observacion?.cancel()
        observacion = viewModelScope.launch {
            _estado.value = UiState.Cargando
            repositorio.items
                .catch {
                    _estado.value = UiState.Error("No se pudieron cargar los totales.")
                }
                .collect { lista ->
                    _estado.value = UiState.Exito(
                        ResumenAdmin(
                            totalGuardado = lista.size,
                            totalFavoritos = lista.count { it.esFavorito }
                        )
                    )
                }
        }
    }

    fun sincronizar() = ejecutarAccion {
        repositorio.refrescar().fold(
            onSuccess = { cantidad -> "Se guardaron $cantidad elementos." },
            onFailure = { error -> error.message ?: "No se pudo sincronizar el catálogo." }
        )
    }

    fun limpiarCache() = ejecutarAccion {
        repositorio.limpiarCache()
        "Caché vaciada."
    }

    private fun ejecutarAccion(accion: suspend () -> String) {
        if (operacion?.isActive == true) return

        operacion = viewModelScope.launch {
            _ocupado.value = true
            _mensaje.value = null
            try {
                _mensaje.value = accion()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _mensaje.value = "No se pudo completar la operación. Inténtalo otra vez."
            } finally {
                _ocupado.value = false
            }
        }
    }
}
