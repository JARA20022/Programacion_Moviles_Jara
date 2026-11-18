package com.tecsup.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionNoDisponible
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import com.tecsup.campusgps.util.formatearDistancia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {
    private val sinServicios = UbicacionUi.Error(
        "Este dispositivo no tiene Google Play Services", TipoError.SIN_PLAY_SERVICES
    )
    private val _estado = MutableStateFlow<UbicacionUi>(
        if (ubicacionRepo.playServicesDisponible()) UbicacionUi.Inicial else sinServicios
    )
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) })
    val lugares = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = sinServicios
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion().onSuccess {
                _estado.value = UbicacionUi.Lista(it)
                actualizarDistancias(it)
            }.onFailure { _estado.value = errorDe(it) }
        }
    }

    private fun actualizarDistancias(pos: Posicion) {
        val ordenados = lugaresRepo.obtenerLugares().map { lugar ->
            LugarConDistancia(lugar, distanciaMetros(pos, lugar))
        }.sortedBy { it.metros }
        _lugares.value = ordenados
        Log.d("CampusGPS", ordenados.joinToString(" | ") { item ->
            val distancia = item.metros?.let(::formatearDistancia) ?: "Sin calcular"
            "${item.lugar.nombre}: $distancia"
        })
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás", TipoError.SIN_PERMISO)
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when {
        e is SecurityException -> UbicacionUi.Error("Falta el permiso de ubicación", TipoError.SIN_PERMISO)
        !ubicacionRepo.playServicesDisponible() -> sinServicios
        e is UbicacionNoDisponible -> UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
        else -> UbicacionUi.Error("No se pudo obtener la ubicación", TipoError.OTRO)
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    val masCercano: StateFlow<Lugar?> = lugares.map { lista ->
        lista.firstOrNull { it.metros != null }?.lugar
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
