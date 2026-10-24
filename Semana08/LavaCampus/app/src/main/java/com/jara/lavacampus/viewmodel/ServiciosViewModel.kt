package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.entity.Servicio
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.Unidad
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class FormularioServicio(
    val nombre: String = "", val descripcion: String = "", val unidad: String = "KG",
    val precio: String = "", val horasEntrega: Int = 24, val icono: String = "wash", val activo: Boolean = true
)

class ServiciosViewModel(private val repo: LavaRepository, private val servicioId: Int = 0) : ViewModel() {
    private val _servicios = MutableStateFlow(emptyList<Servicio>())
    val servicios = _servicios.asStateFlow()
    private val _formulario = MutableStateFlow(FormularioServicio())
    val formulario = _formulario.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()
    private val _guardado = MutableStateFlow(false)
    val guardado = _guardado.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    private var formularioCargado = servicioId == 0

    init {
        viewModelScope.launch {
            repo.observarServicios().catch { e ->
                if (e is CancellationException) throw e
                _error.value = "No se pudieron cargar los servicios. Vuelve a abrir la pantalla."
                _cargando.value = false
            }.collect { lista ->
                _servicios.value = lista
                _cargando.value = false
                if (!formularioCargado && servicioId > 0) {
                    val s = lista.firstOrNull { it.id == servicioId }
                    if (s == null) _error.value = "El servicio ya no está disponible."
                    else {
                        _formulario.value = FormularioServicio(s.nombre, s.descripcion, s.unidad, s.precio.toString(), s.horasEntrega, s.icono, s.activo)
                        formularioCargado = true
                    }
                }
            }
        }
    }

    fun editar(formulario: FormularioServicio) {
        if (!_guardando.value && !_guardado.value) _formulario.value = formulario
    }
    fun guardarFormulario() {
        if (_cargando.value || !formularioCargado) return
        val f = _formulario.value
        val precio = f.precio.trim().replace(',', '.').toDoubleOrNull()
        if (precio == null || !precio.isFinite() || precio <= 0.0) { _error.value = "El precio debe ser un número mayor que cero."; return }
        guardar(Servicio(servicioId, f.nombre, f.descripcion, f.unidad, precio, f.horasEntrega, f.activo, f.icono))
    }
    private fun ejecutar(accion: suspend () -> Unit) {
        if (_guardando.value) return
        _guardando.value = true
        _error.value = null
        _mensaje.value = null
        viewModelScope.launch {
            try { accion()
            } catch (e: CancellationException) { throw e
            } catch (e: IllegalArgumentException) { _error.value = e.message
            } catch (e: Exception) { _error.value = "No se pudo guardar el cambio. Intenta nuevamente."
            } finally { _guardando.value = false }
        }
    }
    fun guardar(servicio: Servicio) {
        if (_guardado.value) return
        ejecutar {
            require(servicio.nombre.isNotBlank()) { "Escribe el nombre del servicio." }
            require(servicio.precio.isFinite() && servicio.precio > 0.0) { "El precio debe ser mayor que cero." }
            require(servicio.unidad in Unidad.entries.map { it.name }) { "Selecciona una unidad válida." }
            require(servicio.horasEntrega in listOf(24, 48, 72)) { "Selecciona 24, 48 o 72 horas." }
            require(servicio.icono in listOf("wash", "iron", "shirt")) { "Selecciona un ícono." }
            require(servicio.id >= 0) { "Servicio inválido." }
            repo.enTransaccion {
                if (servicio.id > 0) requireNotNull(repo.obtenerServicio(servicio.id)) { "El servicio ya no existe." }
                repo.guardarServicio(servicio.copy(nombre = servicio.nombre.trim(), descripcion = servicio.descripcion.trim()))
            }
            _guardado.value = true
        }
    }
    fun cambiarActivo(servicio: Servicio, activo: Boolean) {
        ejecutar {
            repo.enTransaccion {
                requireNotNull(repo.obtenerServicio(servicio.id)) { "El servicio ya no existe." }
                repo.cambiarActivo(servicio.id, activo)
            }
            _mensaje.value = if (activo) "Servicio activado." else "Servicio desactivado."
        }
    }
    fun eliminar(servicio: Servicio) {
        ejecutar {
            repo.enTransaccion {
                require(!repo.servicioTieneDetalles(servicio.id)) { "Este servicio ya aparece en pedidos. No se puede eliminar; solo desactivar." }
                requireNotNull(repo.obtenerServicio(servicio.id)) { "El servicio ya no existe." }
                repo.eliminarServicio(servicio)
            }
            _mensaje.value = "Servicio eliminado."
        }
    }
}
