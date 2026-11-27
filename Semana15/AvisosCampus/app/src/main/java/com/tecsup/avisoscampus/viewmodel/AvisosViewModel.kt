package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AvisosViewModel(private val repo: AvisosRepository): ViewModel() {
 val avisos=repo.avisos
 private val _permiso=MutableStateFlow(Permiso.SinPedir)
 val permiso=_permiso.asStateFlow()
 private val _mensaje=MutableStateFlow<String?>(null)
 val mensaje=_mensaje.asStateFlow()
 private val _eventos=MutableSharedFlow<Aviso>()
 val eventos=_eventos.asSharedFlow()
 private val _faltan=MutableStateFlow<Int?>(null)
 val faltan=_faltan.asStateFlow()
 private val _escuchandoAvion=MutableStateFlow(false)
 val escuchandoAvion=_escuchandoAvion.asStateFlow()
 private var trabajo: Job?=null
 fun permisoRespondido(concedido: Boolean) {
  _permiso.value=if(concedido) Permiso.Concedido else Permiso.Denegado
  if(concedido) _mensaje.value=null
 }
 private suspend fun emitir(t: String,d: String,o: Origen) {
  try { _eventos.emit(repo.registrar(t,d,o)) } catch(e: CancellationException) { throw e } catch(_: Exception) { _mensaje.value="No se pudo guardar el aviso. Vuelve a intentarlo." }
 }
 fun avisarAhora() { viewModelScope.launch { emitir("Tarea lista","Tu entrega fue registrada. Puedes consultar este aviso en el historial de AvisosCampus cuando lo necesites.",Origen.BOTON) } }
 fun recordarEn(segundos: Int=10) {
  if(trabajo?.isActive==true) return
  trabajo=viewModelScope.launch {
   try { for(i in segundos downTo 1) { _faltan.value=i; delay(1000) }; emitir("Recordatorio","Revisa tu laboratorio pendiente. Organiza tu entrega y verifica los requisitos antes de enviarla.",Origen.RECORDATORIO) } finally { _faltan.value=null }
  }
 }
 fun cancelarRecordatorio() { trabajo?.cancel() }
 fun cambiarEscucha(valor: Boolean) { _escuchandoAvion.value=valor }
 fun alCambiarModoAvion(activo: Boolean) { viewModelScope.launch { emitir("Modo avión",if(activo) "Activado" else "Desactivado",Origen.MODO_AVION) } }
 fun noSePudoMostrar() { _mensaje.value="Sin permiso: el aviso se guardó en el historial, pero no se mostró" }
 fun cerrarMensaje() { _mensaje.value=null }
 fun limpiarHistorial() { viewModelScope.launch {
  try { repo.limpiar() } catch(e: CancellationException) { throw e } catch(_: Exception) { _mensaje.value="No se pudo borrar el historial. Inténtalo de nuevo." }
 } }
}
