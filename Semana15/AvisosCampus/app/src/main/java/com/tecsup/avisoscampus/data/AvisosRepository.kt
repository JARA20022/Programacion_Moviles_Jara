package com.tecsup.avisoscampus.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

interface AvisosRepository {
 val avisos: StateFlow<List<Aviso>>
 suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso
 suspend fun limpiar()
}
class AvisosRepositoryPersistente(private val store: DataStore<Preferences>, scope: CoroutineScope) : AvisosRepository {
 private val historial = stringPreferencesKey("historial_v1")
 private val secuencia = intPreferencesKey("siguiente_id")
 private fun leer(raw: String?): List<Aviso> {
  if(raw==null) return emptyList()
  val array=JSONArray(raw)
  return (0 until array.length()).map { i ->
   val o=array.getJSONObject(i)
   Aviso(o.getInt("id"),o.getString("titulo"),o.getString("texto"),Origen.valueOf(o.getString("origen")),o.getLong("hora"))
  }
 }
 private fun guardar(lista: List<Aviso>): String {
  val array=JSONArray()
  lista.forEach { a -> array.put(JSONObject().put("id",a.id).put("titulo",a.titulo).put("texto",a.texto).put("origen",a.origen.name).put("hora",a.hora)) }
  return array.toString()
 }
 override val avisos = store.data.catch { e -> if(e is IOException) emit(emptyPreferences()) else throw e }
  .map { leer(it[historial]) }.stateIn(scope,SharingStarted.Eagerly,emptyList())
 override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
  var resultado: Aviso?=null
  store.edit { prefs ->
   val id=prefs[secuencia] ?: 1
   val aviso=Aviso(id,titulo,texto,origen)
   prefs[secuencia]=if(id==Int.MAX_VALUE) 1 else id+1
   prefs[historial]=guardar((listOf(aviso)+leer(prefs[historial])).take(200))
   resultado=aviso
  }
  return checkNotNull(resultado)
 }
 override suspend fun limpiar() { store.edit { it[historial]="[]" } }
}
