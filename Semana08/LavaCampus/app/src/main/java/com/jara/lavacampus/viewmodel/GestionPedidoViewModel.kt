package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.Casilleros
import com.jara.lavacampus.model.EstadoPedido
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(private val pedidoId: Int, private val repo: LavaRepository) : ViewModel() {
    private val _pedido = MutableStateFlow<PedidoConDetalle?>(null)
    val pedido: StateFlow<PedidoConDetalle?> = _pedido.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _ocupado = MutableStateFlow(false)
    val ocupado = _ocupado.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    private val _casilleroElegido = MutableStateFlow<String?>(null)
    val casilleroElegido = _casilleroElegido.asStateFlow()
    private val ocupados = MutableStateFlow(emptyList<String>())
    val casillerosOcupados: StateFlow<List<String>> = combine(ocupados, pedido) { lista, actual ->
        lista.filterNot { actual?.pedido?.estado == EstadoPedido.LISTO.name && it == actual.pedido.casillero }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            repo.observarPedido(pedidoId).catch { e ->
                if (e is CancellationException) throw e
                _error.value = "No se pudo cargar el pedido. Vuelve a abrirlo."
                _cargando.value = false
            }.collect { datos ->
                _pedido.value = datos
                _cargando.value = false
                if (datos == null) _error.value = "El pedido no está disponible."
            }
        }
        viewModelScope.launch {
            repo.observarCasillerosOcupados().catch { e ->
                if (e is CancellationException) throw e
                _error.value = "No se pudieron observar los casilleros. Se comprobarán de nuevo al guardar."
            }.collect { ocupados.value = it }
        }
    }

    fun elegirCasillero(c: String) {
        if (_ocupado.value || _pedido.value?.pedido?.estado != EstadoPedido.EN_PROCESO.name) return
        if (c !in Casilleros.todos) { _error.value = "Elige un casillero entre C-01 y C-30."; return }
        if (c in casillerosOcupados.value) { _error.value = "Ese casillero está ocupado."; return }
        _casilleroElegido.value = c
        _error.value = null
    }

    private fun ejecutar(accion: suspend (PedidoConDetalle) -> String) {
        if (_ocupado.value || _pedido.value == null) return
        _ocupado.value = true
        _error.value = null
        _mensaje.value = null
        viewModelScope.launch {
            try {
                val resultado = repo.enTransaccion {
                    val actual = requireNotNull(repo.obtenerPedido(pedidoId)) { "El pedido no está disponible." }
                    val texto = accion(actual)
                    requireNotNull(repo.obtenerPedido(pedidoId)) to texto
                }
                _pedido.value = resultado.first
                _mensaje.value = resultado.second
            } catch (e: CancellationException) { throw e
            } catch (e: IllegalArgumentException) { _error.value = e.message
            } catch (e: Exception) { _error.value = "No se pudo guardar el cambio. Intenta nuevamente."
            } finally { _ocupado.value = false }
        }
    }

    fun avanzar() {
        val esperado = _pedido.value?.pedido?.estado ?: return
        val elegido = _casilleroElegido.value
        ejecutar { actual ->
            require(actual.pedido.estado == esperado) { "El estado cambió. Revisa el pedido antes de continuar." }
            val siguiente = requireNotNull(EstadoPedido.valueOf(actual.pedido.estado).siguiente()) { "Este pedido ya está finalizado." }
            val casillero = when (siguiente) {
                EstadoPedido.LISTO -> {
                    require(elegido in Casilleros.todos) { "Elige un casillero libre antes de marcar como Listo." }
                    require(elegido !in repo.obtenerCasillerosOcupados()) { "Ese casillero está ocupado. Elige otro." }
                    elegido
                }
                EstadoPedido.ENTREGADO -> {
                    require(actual.pedido.pagado) { "Marca Cobrado antes de entregar el pedido." }
                    null // Al entregar, el casillero queda libre.
                }
                else -> null
            }
            repo.cambiarEstado(pedidoId, siguiente, casillero)
            when (siguiente) {
                EstadoPedido.EN_PROCESO -> "Lavado iniciado."
                EstadoPedido.LISTO -> "Pedido listo en el casillero $casillero."
                EstadoPedido.ENTREGADO -> "Pedido entregado. Casillero liberado."
                else -> "Pedido actualizado."
            }
        }
    }

    fun cancelar() {
        ejecutar { actual ->
            require(actual.pedido.estado in listOf(EstadoPedido.RECIBIDO.name, EstadoPedido.EN_PROCESO.name)) {
                "Solo puedes cancelar pedidos recibidos o en proceso."
            }
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
            "Pedido cancelado."
        }
    }

    fun marcarPagado(b: Boolean) {
        ejecutar { actual ->
            require(actual.pedido.estado !in listOf(EstadoPedido.ENTREGADO.name, EstadoPedido.CANCELADO.name)) {
                "No se modifica el cobro de un pedido finalizado."
            }
            repo.marcarPagado(pedidoId, b)
            if (b) "Cobro registrado." else "Pedido marcado como pendiente de pago."
        }
    }
}
