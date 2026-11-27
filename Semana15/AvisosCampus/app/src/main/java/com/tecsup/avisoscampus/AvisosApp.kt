package com.tecsup.avisoscampus

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.data.AvisosRepositoryPersistente
import com.tecsup.avisoscampus.notificacion.Notificador
import kotlinx.coroutines.*
import java.io.File

class AvisosApp : Application() {
 private val scope=CoroutineScope(SupervisorJob()+Dispatchers.IO)
 val repository: AvisosRepository by lazy {
  val store=PreferenceDataStoreFactory.create(scope=scope,produceFile={File(filesDir,"avisos.preferences_pb")})
  AvisosRepositoryPersistente(store,scope)
 }
 override fun onCreate() { super.onCreate(); Notificador.crearCanal(this) }
}
