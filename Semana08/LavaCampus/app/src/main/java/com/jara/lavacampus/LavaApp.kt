package com.jara.lavacampus

import android.app.Application
import android.util.Log
import com.jara.lavacampus.data.local.DatosIniciales
import com.jara.lavacampus.data.local.LavaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LavaApp : Application() {

    val db: LavaDatabase by lazy { LavaDatabase.obtener(this) }

    private val alcance = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        alcance.launch {
            try {
                DatosIniciales.sembrar(db)
            } catch (error: Exception) {
                Log.e("LavaApp", "No se pudieron cargar los datos iniciales", error)
            }
        }
    }
}
