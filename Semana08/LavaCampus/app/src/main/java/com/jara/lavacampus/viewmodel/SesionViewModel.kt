package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.data.prefs.PreferenciasStore
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.Rol
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: LavaRepository,
    private val preferencias: PreferenciasStore,
    private val prepararDatos: suspend () -> Unit
) : ViewModel() {
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _restaurando = MutableStateFlow(true)
    val restaurando: StateFlow<Boolean> = _restaurando.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferencias.modoOscuro.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )

    val notificaciones = preferencias.notificaciones.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val recibirPedidos = preferencias.recibirPedidos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    private val _guardandoPreferencias = MutableStateFlow(false)
    val guardandoPreferencias = _guardandoPreferencias.asStateFlow()

    private fun guardarPreferencia(accion: suspend () -> Unit) {
        if (_guardandoPreferencias.value || _cargando.value) return
        _guardandoPreferencias.value = true
        _error.value = null
        viewModelScope.launch {
            try { accion()
            } catch (e: CancellationException) { throw e
            } catch (e: Exception) { _error.value = "No se pudo guardar el ajuste. Intenta nuevamente."
            } finally { _guardandoPreferencias.value = false }
        }
    }
    fun cambiarModoOscuro(b: Boolean) = guardarPreferencia { preferencias.guardarModoOscuro(b) }
    fun cambiarNotificaciones(b: Boolean) = guardarPreferencia { preferencias.guardarNotificaciones(b) }
    fun cambiarRecibirPedidos(b: Boolean) {
        if (_usuarioActual.value?.rol != Rol.ADMIN.name) return
        guardarPreferencia { preferencias.guardarRecibirPedidos(b) }
    }

    fun cambiarPerfil() {
        if (_cargando.value || _restaurando.value || _guardandoPreferencias.value) return
        val actual = _usuarioActual.value ?: return
        _cargando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val destino = if (actual.rol == Rol.CLIENTE.name) Rol.ADMIN else Rol.CLIENTE
                val recordado = if (destino == Rol.CLIENTE) {
                    repository.obtenerUsuario(preferencias.ultimoEstudianteId.first())?.takeIf { it.rol == Rol.CLIENTE.name }
                } else null
                val otro = requireNotNull(recordado ?: repository.obtenerUsuarioPorRol(destino.name)) {
                    "No existe una cuenta para el otro perfil."
                }
                preferencias.guardarSesion(otro.rol, otro.id, if (actual.rol == Rol.CLIENTE.name) actual.id else null)
                _usuarioActual.value = otro
            } catch (e: CancellationException) { throw e
            } catch (e: Exception) { _error.value = "No se pudo cambiar de perfil. Intenta nuevamente."
            } finally { _cargando.value = false }
        }
    }

    init {
        viewModelScope.launch {
            try {
                prepararDatos()
                val rol = preferencias.perfilActivo.first()
                val id = preferencias.usuarioId.first()
                if (rol.isNotEmpty()) {
                    val usuario = repository.obtenerUsuario(id)
                    if (
                        usuario != null && usuario.rol == rol &&
                        Rol.entries.any { it.name == rol }
                    ) {
                        _usuarioActual.value = usuario
                    } else {
                        preferencias.cerrarSesion()
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _error.value = "No se pudo recuperar la sesión. Intenta ingresar nuevamente."
            } finally {
                _restaurando.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (_cargando.value || _restaurando.value) return
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña."
            return
        }

        _error.value = null
        _cargando.value = true
        viewModelScope.launch {
            try {
                prepararDatos()
                val usuario = repository.login(correo.trim(), clave)
                when {
                    usuario == null -> {
                        _error.value = "Correo o contraseña incorrectos"
                    }
                    usuario.rol != rolSeleccionado.name -> {
                        _error.value = "La cuenta no corresponde al perfil seleccionado."
                    }
                    else -> {
                        preferencias.guardarSesion(usuario.rol, usuario.id)
                        _usuarioActual.value = usuario
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _error.value = "No se pudo iniciar sesión. Intenta nuevamente."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun cerrarSesion() {
        if (_cargando.value) return
        _cargando.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                preferencias.cerrarSesion()
                _usuarioActual.value = null
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _error.value = "No se pudo cerrar la sesión. Intenta nuevamente."
            } finally {
                _cargando.value = false
            }
        }
    }
}
