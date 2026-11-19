package com.tecsup.centrotareas.viewmodel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.ModoCarga
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.model.TiemposCarga
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private var trabajo: Job? = null
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                repo.progresoTarea().collect { porcentaje ->
                    _progreso.value = porcentaje / 100f
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando(0)
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta) { porcentaje ->
                    _primos.value = PrimosUi.Calculando(porcentaje)
                }
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                val actual = _primos.value as? PrimosUi.Calculando
                _primos.value = PrimosUi.Cancelado(actual?.porcentaje ?: 0)
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _tiempos = MutableStateFlow(TiemposCarga())
    val tiempos: StateFlow<TiemposCarga> = _tiempos.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cargarUnaPorUna() {
        cargar(ModoCarga.SECUENCIAL) { simular ->
            val perfil = repo.cargarPerfil()
            val pedidos = repo.cargarPedidos(simular)
            perfil to pedidos
        }
    }

    fun cargarALaVez() {
        cargar(ModoCarga.PARALELO) { simular ->
            coroutineScope {
                val perfil = async { repo.cargarPerfil() }
                val pedidos = async { repo.cargarPedidos(simular) }
                perfil.await() to pedidos.await()
            }
        }
    }

    private fun cargar(modo: ModoCarga, obtener: suspend (Boolean) -> Pair<String, List<String>>) {
        if (_carga.value == CargaUi.Cargando) return
        val simular = _simularError.value
        _carga.value = CargaUi.Cargando
        viewModelScope.launch {
            try {
                val inicio = SystemClock.elapsedRealtime()
                val (perfil, pedidos) = obtener(simular)
                val milisegundos = SystemClock.elapsedRealtime() - inicio
                _tiempos.value = _tiempos.value.registrar(modo, milisegundos)
                _carga.value = CargaUi.Exito(modo.etiqueta, perfil, pedidos.size, milisegundos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val motivo = e.localizedMessage ?: "Error desconocido"
                _carga.value = CargaUi.Error("No se pudo cargar: $motivo")
            }
        }
    }
}
