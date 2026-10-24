package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.model.ClienteResumen
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

class ClientesViewModel(repo: LavaRepository) : ViewModel() {
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val todos = repo.observarClientes().catch { e ->
        if (e is CancellationException) throw e
        _error.value = "No se pudieron cargar los clientes. Vuelve a abrir la pantalla."
        emit(emptyList())
    }.onEach { _cargando.value = false }
    val clientes: StateFlow<List<ClienteResumen>> = combine(todos, busqueda) { lista, texto ->
        val consulta = normalizar(texto.trim())
        lista.filter { normalizar("${it.nombre} ${it.codigo} ${it.correo}").contains(consulta) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private fun normalizar(texto: String) = Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "").lowercase(java.util.Locale.ROOT)
    fun cambiarBusqueda(texto: String) { _busqueda.value = texto }
}
