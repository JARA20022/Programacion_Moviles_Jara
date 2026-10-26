package com.jara.lavacampus.viewmodel

import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.model.RelojCampus
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.EstadoPedido
import java.text.Normalizer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FiltroBandeja(val titulo: String, val estado: EstadoPedido?) {
    TODOS("Todos", null), ATRASADOS("Atrasados", null), RECIBIDO("Recibido", EstadoPedido.RECIBIDO),
    EN_PROCESO("En proceso", EstadoPedido.EN_PROCESO), LISTO("Listo", EstadoPedido.LISTO)
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class BandejaViewModel(private val repo: LavaRepository, filtroInicial: String = "TODOS") : ViewModel() {
    private val _filtro = MutableStateFlow(FiltroBandeja.entries.firstOrNull { it.name == filtroInicial } ?: FiltroBandeja.TODOS)
    val filtro = _filtro.asStateFlow()
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    private val _ocupadoId = MutableStateFlow<Int?>(null)
    val ocupadoId = _ocupadoId.asStateFlow()

    val atrasados: StateFlow<Set<Int>> = RelojCampus.hoy().flatMapLatest { repo.observarIdsAtrasados(it) }
        .map { it.toSet() }.catch { e ->
            if(e is CancellationException) throw e
            _error.value = "No se pudieron consultar los atrasados."; emit(emptySet())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())
    private val todos = repo.observarPedidos()
        .catch { e ->
            if (e is CancellationException) throw e
            _error.value = "No se pudo cargar la bandeja. Vuelve a abrirla."
            emit(emptyList())
        }
        .onEach { _cargando.value = false }

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(todos, filtro, busqueda, atrasados) { lista, elegido, texto, vencidos ->
        val consulta = normalizar(texto.trim())
        val numero = consulta.removePrefix("#")
        lista.filter { datos ->
            (elegido.estado == null || datos.pedido.estado == elegido.estado.name) &&
                (elegido != FiltroBandeja.ATRASADOS || datos.pedido.id in vencidos) &&
                (consulta.isBlank() || normalizar(datos.usuario.nombre).contains(consulta) ||
                    (numero.isNotBlank() && (datos.pedido.id.toString().contains(numero) || datos.pedido.id.toString().padStart(3, '0').contains(numero))))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private fun normalizar(texto: String): String = Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "").lowercase(java.util.Locale.ROOT)
    fun cambiarBusqueda(texto: String) {
        if (Validaciones.textoEditable(texto, Validaciones.MAX_BUSQUEDA)) _busqueda.value = texto
    }
    fun cambiarFiltro(filtro: FiltroBandeja) { _filtro.value = filtro }

    fun iniciar(pedido: PedidoConDetalle) {
        if (_ocupadoId.value != null) return
        _ocupadoId.value = pedido.pedido.id
        _error.value = null
        _mensaje.value = null
        viewModelScope.launch {
            try {
                repo.enTransaccion {
                    val actual = requireNotNull(repo.obtenerPedido(pedido.pedido.id)) { "El pedido ya no está disponible." }
                    require(actual.pedido.estado == EstadoPedido.RECIBIDO.name) { "El pedido cambió de estado. Revisa la bandeja." }
                    repo.cambiarEstado(actual.pedido.id, EstadoPedido.EN_PROCESO, null)
                }
                _mensaje.value = "Pedido #${pedido.pedido.id}: lavado iniciado."
            } catch (e: CancellationException) { throw e
            } catch (e: IllegalArgumentException) { _error.value = e.message
            } catch (e: Exception) { _error.value = "No se pudo iniciar el pedido. Intenta nuevamente."
            } finally { _ocupadoId.value = null }
        }
    }
}
