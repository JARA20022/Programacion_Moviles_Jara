package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.SolicitudSoporteResumen
import com.jara.lavacampus.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SolicitudesSoporteViewModel(
    private val repository: LavaRepository,
    private val soporteId: Int
) : ViewModel() {
    
    private val _filtroEstado = MutableStateFlow("TODOS")
    val filtroEstado = _filtroEstado.asStateFlow()

    val solicitudes = repository.observarSolicitudesSoporte()
        .combine(_filtroEstado) { lista, filtro ->
            if (filtro == "TODOS") lista else lista.filter { it.estadoAtencion == filtro }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cambiarFiltro(estado: String) {
        _filtroEstado.value = estado
    }

    fun responder(detalleId: Int, estadoAtencion: String, respuesta: String, onExito: () -> Unit) {
        if (_cargando.value) return
        _error.value = null

        if (estadoAtencion == "ATENDIDA") {
            val errorRespuesta = Validaciones.errorRespuestaSoporte(respuesta)
            if (errorRespuesta != null) {
                _error.value = errorRespuesta
                return
            }
        }

        _cargando.value = true
        viewModelScope.launch {
            try {
                val fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                repository.responderSolicitud(detalleId, estadoAtencion, respuesta, soporteId, fecha)
                onExito()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ocurrió un error al responder la solicitud."
            } finally {
                _cargando.value = false
            }
        }
    }
    
    fun limpiarError() {
        _error.value = null
    }
}
