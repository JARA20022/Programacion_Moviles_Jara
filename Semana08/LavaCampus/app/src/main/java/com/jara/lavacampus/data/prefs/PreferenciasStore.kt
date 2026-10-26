package com.jara.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(context: Context) {

    private val store = context.applicationContext.dataStore

    private object Claves {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        val USUARIO_ID = intPreferencesKey("usuarioId")
        val ULTIMO_ESTUDIANTE = intPreferencesKey("ultimoEstudianteId")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_PEDIDOS = booleanPreferencesKey("recibirPedidos")
    }

    private val preferencias = store.data.catch { error ->
        if (error is IOException) {
            emit(emptyPreferences())
        } else {
            throw error
        }
    }

    val perfilActivo: Flow<String> = preferencias.map {
        it[Claves.PERFIL_ACTIVO] ?: ""
    }

    val usuarioId: Flow<Int> = preferencias.map {
        it[Claves.USUARIO_ID] ?: 0
    }

    val ultimoEstudianteId: Flow<Int> = preferencias.map { it[Claves.ULTIMO_ESTUDIANTE] ?: 0 }

    val modoOscuro: Flow<Boolean> = preferencias.map {
        it[Claves.MODO_OSCURO] ?: false
    }

    val notificaciones: Flow<Boolean> = preferencias.map {
        it[Claves.NOTIFICACIONES] ?: true
    }

    val recibirPedidos: Flow<Boolean> = preferencias.map {
        it[Claves.RECIBIR_PEDIDOS] ?: true
    }

    suspend fun guardarSesion(rol: String, usuarioId: Int, estudianteAnteriorId: Int? = null) {
        require(rol == "CLIENTE" || rol == "ADMIN") {
            "El rol debe ser CLIENTE o ADMIN."
        }
        require(usuarioId > 0) {
            "El identificador del usuario debe ser mayor que cero."
        }

        store.edit {
            it[Claves.PERFIL_ACTIVO] = rol
            it[Claves.USUARIO_ID] = usuarioId
            if (rol == "CLIENTE") it[Claves.ULTIMO_ESTUDIANTE] = usuarioId
            else if (estudianteAnteriorId != null && estudianteAnteriorId > 0) it[Claves.ULTIMO_ESTUDIANTE] = estudianteAnteriorId
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        store.edit {
            it[Claves.MODO_OSCURO] = b
        }
    }

    suspend fun guardarNotificaciones(b: Boolean) {
        store.edit {
            it[Claves.NOTIFICACIONES] = b
        }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        store.edit {
            it[Claves.RECIBIR_PEDIDOS] = b
        }
    }

    suspend fun cerrarSesion() {
        store.edit {
            it.remove(Claves.PERFIL_ACTIVO)
            it.remove(Claves.USUARIO_ID)
        }
    }
}
