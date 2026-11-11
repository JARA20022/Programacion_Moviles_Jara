package com.tecsup.productos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.domain.model.Sesion
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repositorio: AuthRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()
    private var operacion: Job? = null

    init {
        operacion = viewModelScope.launch {
            try {
                _estado.value = UiState.Exito(repositorio.sesionActual())
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _estado.value = UiState.Error(
                    "No se pudo recuperar la sesión. Revisa tu conexión e ingresa nuevamente."
                )
            }
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (operacion?.isActive == true) return

        val correoLimpio = correo.trim()
        if (correoLimpio.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error("Completa el correo y la contraseña.")
            return
        }

        operacion = viewModelScope.launch {
            _estado.value = UiState.Cargando
            try {
                _estado.value = repositorio.iniciarSesion(correoLimpio, clave).fold(
                    onSuccess = { UiState.Exito(it) },
                    onFailure = {
                        UiState.Error(it.message ?: "No se pudo iniciar sesión.")
                    }
                )
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _estado.value = UiState.Error("No se pudo iniciar sesión. Inténtalo otra vez.")
            }
        }
    }

    fun cerrarSesion() {
        operacion?.cancel()
        repositorio.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }
}
