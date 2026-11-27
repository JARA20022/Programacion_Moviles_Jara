package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
 val avisos = repo.avisos
 private val _permiso = MutableStateFlow(Permiso.SinPedir)
 val permiso = _permiso.asStateFlow()
 fun permisoRespondido(concedido: Boolean) { _permiso.value=if(concedido) Permiso.Concedido else Permiso.Denegado
 if(concedido) _mensaje.value=null
 }
 private val _eventos = MutableSharedFlow<Aviso>()
 val eventos = _eventos.asSharedFlow()
 private suspend fun emitir(t: String, d: String, o: Origen) { _eventos.emit(repo.registrar(t,d,o)) }
 fun avisarAhora() { viewModelScope.launch { emitir("Tarea lista","Tu entrega fue registrada",Origen.BOTON) } }
 private var trabajo: Job? = null
 private val _faltan = MutableStateFlow<Int?>(null)
 val faltan = _faltan.asStateFlow()
 fun recordarEn(segundos: Int = 10) {
  if(trabajo?.isActive==true) return
  trabajo=viewModelScope.launch {
   try { for(i in segundos downTo 1) { _faltan.value=i; delay(1000) }; emitir("Recordatorio","Revisa tu laboratorio pendiente",Origen.RECORDATORIO) }
   finally { _faltan.value=null }
  }
 }
 fun cancelarRecordatorio() { trabajo?.cancel() }
 private val _escuchandoAvion = MutableStateFlow(false)
 val escuchandoAvion = _escuchandoAvion.asStateFlow()
 fun cambiarEscucha(valor: Boolean) { _escuchandoAvion.value=valor }
 fun alCambiarModoAvion(activo: Boolean) { viewModelScope.launch { emitir("Modo avión",if(activo) "Activado" else "Desactivado",Origen.MODO_AVION) } }
 private val _mensaje = MutableStateFlow<String?>(null)
 val mensaje = _mensaje.asStateFlow()
 fun noSePudoMostrar() { _mensaje.value="Sin permiso: el aviso se guardó en el historial, pero no se mostró" }
}
