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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

data class ResumenPerfil(val activos: Int = 0, val totales: Int = 0, val kilosLavados: Double = 0.0)

class InicioViewModel(
    val estudiante: Usuario,
    repository: LavaRepository
) : ViewModel() {
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // PedidoDao entrega la lista por id DESC: primero el pedido más reciente.
    // Se comparte una sola observación de Room para ambos estados de pantalla.
    private val pedidos = repository.observarPedidosDeUsuario(estudiante.id)
        .catch { error ->
            if (error is CancellationException) throw error
            _error.value = "No se pudieron cargar tus pedidos. Vuelve a ingresar."
            emit(emptyList())
        }
        .onEach { _cargando.value = false }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), replay = 1)

    val pedidoActivo: StateFlow<PedidoConDetalle?> = pedidos.map { lista ->
        lista.firstOrNull {
            it.pedido.estado in setOf(
                EstadoPedido.RECIBIDO.name,
                EstadoPedido.EN_PROCESO.name,
                EstadoPedido.LISTO.name
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val resumenPerfil: StateFlow<ResumenPerfil> = pedidos.map { lista ->
        ResumenPerfil(
            activos = lista.count { it.pedido.estado in setOf("RECIBIDO", "EN_PROCESO", "LISTO") },
            totales = lista.size,
            kilosLavados = lista.filter { it.pedido.estado == "ENTREGADO" }.sumOf { pedido ->
                pedido.detalles.filter { it.servicio.unidad == "KG" }.sumOf { it.detalle.cantidad }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumenPerfil())

    val recientes: StateFlow<List<PedidoConDetalle>> = pedidos.map { lista ->
        lista.take(2)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
