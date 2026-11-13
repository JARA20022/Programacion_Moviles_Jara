package com.tecsup.productos.ui.lista

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
internal fun hayInternetCatalogo(): Boolean {
    val contexto = LocalContext.current.applicationContext
    val cambios = remember(contexto) {
        val gestor = contexto.getSystemService(ConnectivityManager::class.java)
        callbackFlow<Boolean> {
            if (gestor == null) {
                trySend(false)
                close()
                return@callbackFlow
            }

            fun tieneInternet(datos: NetworkCapabilities?): Boolean =
                datos?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                    datos.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            val bloqueo = Any()
            var redActual: Network? = null
            var huboEvento = false
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    synchronized(bloqueo) {
                        redActual = network
                    }
                }

                override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                    synchronized(bloqueo) {
                        if (redActual == null || redActual == network) {
                            redActual = network
                            huboEvento = true
                            trySend(tieneInternet(capabilities))
                        }
                    }
                }

                override fun onLost(network: Network) {
                    synchronized(bloqueo) {
                        if (redActual == null || redActual == network) {
                            redActual = null
                            huboEvento = true
                            trySend(false)
                        }
                    }
                }
            }

            gestor.registerDefaultNetworkCallback(callback)
            synchronized(bloqueo) {
                if (!huboEvento) {
                    val activa = gestor.activeNetwork
                    trySend(tieneInternet(activa?.let(gestor::getNetworkCapabilities)))
                }
            }
            awaitClose { gestor.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
    }
    return cambios.collectAsStateWithLifecycle(initialValue = true).value
}
