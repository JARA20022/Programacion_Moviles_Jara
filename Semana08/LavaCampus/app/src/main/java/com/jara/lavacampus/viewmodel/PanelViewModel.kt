package com.jara.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.colorEstado
import com.jara.lavacampus.ui.components.graficos.DatoGrafico
import com.jara.lavacampus.ui.components.nombreEstado
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class PanelViewModel(repo: LavaRepository) : ViewModel() {
    val hoy: String = LocalDate.now().toString()
    val inicioSemana: String = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    private val fuentes = MutableStateFlow(emptySet<String>())
    private val errores = MutableStateFlow(emptySet<String>())
    val cargando: StateFlow<Boolean> = fuentes.map { it.size < 8 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val error: StateFlow<String?> = errores.map { nombres ->
        if (nombres.isEmpty()) null else "No se pudo cargar: ${nombres.joinToString()}. Vuelve a abrir el panel."
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private fun <T> seguir(flujo: Flow<T>, nombre: String, inicial: T): StateFlow<T> = flujo
        .onEach {
            fuentes.value = fuentes.value + nombre
            errores.value = errores.value - nombre
        }
        .catch { e ->
            if (e is CancellationException) throw e
            fuentes.value = fuentes.value + nombre
            errores.value = errores.value + nombre
            emit(inicial)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), inicial)

    val pedidosHoy: StateFlow<Int> = seguir(repo.observarPedidosDeFecha(hoy), "pedidos de hoy", 0)
    val enProceso: StateFlow<Int> = seguir(repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO), "en proceso", 0)
    val porRecoger: StateFlow<Int> = seguir(repo.observarCantidadPorEstado(EstadoPedido.LISTO), "por recoger", 0)
    private val recibidos = seguir(repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO), "recibidos", 0)
    private val entregados = seguir(repo.observarCantidadPorEstado(EstadoPedido.ENTREGADO), "entregados", 0)
    val ingresosSemana: StateFlow<Double> = seguir(repo.observarIngresosDesde(inicioSemana), "ingresos", 0.0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(recibidos, enProceso, porRecoger, entregados) { r, p, l, e ->
        listOf(
            EstadoPedido.RECIBIDO to r, EstadoPedido.EN_PROCESO to p,
            EstadoPedido.LISTO to l, EstadoPedido.ENTREGADO to e
        ).map { (estado, cantidad) -> DatoGrafico(nombreEstado(estado), cantidad.toFloat(), colorEstado(estado)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val ingresosPorServicio = seguir(repo.observarIngresosPorServicio(inicioSemana), "servicios", emptyList())
    val porServicio: StateFlow<List<DatoGrafico>> = ingresosPorServicio.map { lista ->
        val colores = listOf(Color(0xFF6751A4), Color(0xFFAB4028), Color(0xFF176447), Color(0xFF334BA5))
        lista.mapIndexed { indice, ingreso -> DatoGrafico(ingreso.nombre, ingreso.total.toFloat(), colores[indice % colores.size]) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Vista de consulta del botón Ver; la gestión de estos pedidos llega en el paso 12.
    val listos: StateFlow<List<PedidoConDetalle>> = seguir(
        repo.observarPedidos().map { lista -> lista.filter { it.pedido.estado == EstadoPedido.LISTO.name } },
        "lista de recojo", emptyList()
    )
}
