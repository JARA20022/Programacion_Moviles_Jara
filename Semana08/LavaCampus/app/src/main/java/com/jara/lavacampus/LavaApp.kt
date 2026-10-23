package com.jara.lavacampus

import android.app.Application
import android.util.Log
import com.jara.lavacampus.data.local.DatosIniciales
import com.jara.lavacampus.data.local.LavaDatabase
import com.jara.lavacampus.data.prefs.PreferenciasStore
import com.jara.lavacampus.data.repository.LavaRepository
import com.jara.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LavaApp : Application() {
    val db: LavaDatabase by lazy { LavaDatabase.obtener(this) }
    val repository: LavaRepository by lazy { LavaRepositoryRoom(db) }
    val preferencias: PreferenciasStore by lazy { PreferenciasStore(this) }

    private val alcance = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val cargaInicial by lazy {
        alcance.async { DatosIniciales.sembrar(db) }
    }

    suspend fun esperarDatosIniciales() {
        cargaInicial.await()
    }

    override fun onCreate() {
        super.onCreate()
        alcance.launch {
            try {
                esperarDatosIniciales()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Log.e("LavaApp", "No se pudieron cargar los datos iniciales", error)
            }
        }
    }
}
