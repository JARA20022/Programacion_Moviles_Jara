package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.EstadoPedido
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val pedidoId: Int,
    private val estudiante: Usuario,
    private val repo: LavaRepository
) : ViewModel() {
    private val _pedido = MutableStateFlow<PedidoConDetalle?>(null)
    val pedido: StateFlow<PedidoConDetalle?> = _pedido.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _cancelando = MutableStateFlow(false)
    val cancelando = _cancelando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    val puedeCancelar: StateFlow<Boolean> = combine(pedido, cancelando) { actual, ocupado ->
        actual?.pedido?.estado == EstadoPedido.RECIBIDO.name && !ocupado
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch {
            repo.observarPedido(pedidoId)
                .catch { e ->
                    if (e is CancellationException) throw e
                    _cargando.value = false
                    _error.value = "No se pudo cargar el pedido. Vuelve a abrir el detalle."
                }
                .collect { datos ->
                    // Nunca mostrar un pedido perteneciente a otro estudiante.
                    _pedido.value = datos?.takeIf { it.pedido.usuarioId == estudiante.id }
                    _cargando.value = false
                    if (_pedido.value == null) _error.value = "El pedido no está disponible para tu cuenta."
                }
        }
    }

    fun cancelar() {
        if (_cancelando.value) return
        if (_pedido.value?.pedido?.estado != EstadoPedido.RECIBIDO.name) {
            _error.value = "Solo se cancela antes de empezar el lavado"
            return
        }
        _cancelando.value = true
        _error.value = null
        _mensaje.value = null
        viewModelScope.launch {
            try {
                val actualizado=repo.enTransaccion {
                    val actual=requireNotNull(repo.obtenerPedido(pedidoId)) { "El pedido no está disponible para tu cuenta." }
                    require(actual.pedido.usuarioId==estudiante.id) { "El pedido no está disponible para tu cuenta." }
                    require(actual.pedido.estado==EstadoPedido.RECIBIDO.name) { "Solo se cancela antes de empezar el lavado" }
                    repo.cambiarEstado(pedidoId,EstadoPedido.CANCELADO,null)
                    requireNotNull(repo.obtenerPedido(pedidoId)) { "El pedido no está disponible para tu cuenta." }
                }
                _pedido.value=actualizado
                _mensaje.value = "Pedido cancelado correctamente."
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                _error.value=e.message
            } catch (e: Exception) {
                _error.value = "No se pudo cancelar el pedido. Intenta nuevamente."
            } finally {
                _cancelando.value = false
            }
        }
    }
}
