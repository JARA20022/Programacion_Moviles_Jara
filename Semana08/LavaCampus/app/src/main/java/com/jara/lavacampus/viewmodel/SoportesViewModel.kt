package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SoportesViewModel(
    private val repository: LavaRepository,
    private val adminId: Int
) : ViewModel() {
    val soportes = repository.observarSoportes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun limpiarError() { _error.value = null }

    fun registrarSoporte(codigo: String, nombre: String, apellido: String, correo: String, clave: String, claveConfirmacion: String, onExito: () -> Unit) {
        if (_cargando.value) return
        
        if (codigo.isBlank() || nombre.isBlank() || apellido.isBlank() || correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa todos los campos obligatorios."
            return
        }

        if (clave != claveConfirmacion) {
            _error.value = "Las contraseñas no coinciden."
            return
        }
        
        val errorCodigo = Validaciones.errorCodigoSoporte(codigo)
        if (errorCodigo != null) {
            _error.value = errorCodigo
            return
        }
        
        val errorNombre = Validaciones.errorNombre(nombre)
        if (errorNombre != null) {
            _error.value = errorNombre
            return
        }

        val errorApellido = Validaciones.errorNombre(apellido)
        if (errorApellido != null) {
            _error.value = errorApellido
            return
        }
        
        if (!Validaciones.correoValido(correo)) {
            _error.value = "El correo no tiene un formato válido."
            return
        }

        val errorClave = Validaciones.errorClaveSoporte(clave)
        if (errorClave != null) {
            _error.value = errorClave
            return
        }

        _cargando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                repository.registrarSoporte(codigo, nombre, apellido, correo, clave, adminId)
                onExito()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ocurrió un error al registrar al usuario."
            } finally {
                _cargando.value = false
            }
        }
    }
}
