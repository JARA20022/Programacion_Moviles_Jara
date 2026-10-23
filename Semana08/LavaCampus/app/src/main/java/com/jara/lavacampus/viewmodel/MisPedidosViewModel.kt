package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.entity.Servicio
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisPedidosViewModel(
    private val estudiante: Usuario,
    private val repo: LavaRepository
) : ViewModel() {
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    private val _cancelandoId = MutableStateFlow<Int?>(null)
    val cancelandoId = _cancelandoId.asStateFlow()

    private val pedidos = repo.observarPedidosDeUsuario(estudiante.id)
        .map { lista -> lista.filter { it.pedido.usuarioId == estudiante.id } }
        .onEach { _error.value = null }
        .catch { e ->
            if (e is CancellationException) throw e
            _error.value = "No se pudieron cargar tus pedidos. Vuelve a abrir esta pantalla."
            emit(emptyList())
        }
        .onEach { _cargando.value = false }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), replay = 1)

    val activos: StateFlow<List<PedidoConDetalle>> = pedidos.map { lista ->
        lista.filter { it.pedido.estado in setOf(
            EstadoPedido.RECIBIDO.name, EstadoPedido.EN_PROCESO.name, EstadoPedido.LISTO.name
        ) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val historial: StateFlow<List<PedidoConDetalle>> = pedidos.map { lista ->
        lista.filter { it.pedido.estado in setOf(EstadoPedido.ENTREGADO.name, EstadoPedido.CANCELADO.name) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _cargandoServicios = MutableStateFlow(true)
    val cargandoServicios = _cargandoServicios.asStateFlow()
    private val _errorServicios = MutableStateFlow<String?>(null)
    val errorServicios = _errorServicios.asStateFlow()

    val serviciosActivos: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .onEach { _errorServicios.value = null }
        .catch { e ->
            if (e is CancellationException) throw e
            _errorServicios.value = "No se pudieron cargar las tarifas. Vuelve a abrir esta pantalla."
            emit(emptyList())
        }
        .onEach { _cargandoServicios.value = false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun cancelar(pedido: PedidoConDetalle) {
        if (_cancelandoId.value != null) return
        if (pedido.pedido.usuarioId != estudiante.id) {
            _error.value = "Este pedido no pertenece a tu cuenta."
            return
        }
        if (pedido.pedido.estado != EstadoPedido.RECIBIDO.name) {
            _error.value = "Solo se cancela antes de empezar el lavado"
            return
        }
        _cancelandoId.value = pedido.pedido.id
        _error.value = null
        _mensaje.value = null
        viewModelScope.launch {
            try {
                val actual = repo.observarPedido(pedido.pedido.id).first()
                when {
                    actual == null || actual.pedido.usuarioId != estudiante.id ->
                        _error.value = "El pedido no está disponible para tu cuenta."
                    actual.pedido.estado != EstadoPedido.RECIBIDO.name ->
                        _error.value = "Solo se cancela antes de empezar el lavado"
                    else -> {
                        repo.cambiarEstado(actual.pedido.id, EstadoPedido.CANCELADO, null)
                        _mensaje.value = "Pedido #${actual.pedido.id} cancelado. Puedes verlo en Historial."
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudo cancelar el pedido. Intenta nuevamente."
            } finally {
                _cancelandoId.value = null
            }
        }
    }
}
