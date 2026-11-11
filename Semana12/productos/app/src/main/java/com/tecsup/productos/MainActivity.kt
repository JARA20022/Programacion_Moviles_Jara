package com.tecsup.productos

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.navigation.AppNav
import com.tecsup.productos.ui.theme.ProductosTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var esperandoPermiso by mutableStateOf(false)
    private var solicitudEnCurso = false
    private val solicitarNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(PERMISO_CONSULTADO, true)
                .apply()
            solicitudEnCurso = false
            esperandoPermiso = false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = AppContainer(applicationContext)
        esperandoPermiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !getPreferences(Context.MODE_PRIVATE).getBoolean(PERMISO_CONSULTADO, false) &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED

        setContent {
            ProductosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val contenido = Modifier.padding(innerPadding)
                    if (esperandoPermiso) {
                        Cargando(modifier = contenido)
                    } else {
                        AppNav(contenedor = container, modifier = contenido)
                    }
                }
            }
        }

        solicitudEnCurso = savedInstanceState?.getBoolean(PERMISO_EN_CURSO) == true
        if (esperandoPermiso && !solicitudEnCurso) {
            lifecycleScope.launch {
                lifecycle.withStarted {
                    if (esperandoPermiso) {
                        solicitudEnCurso = true
                        solicitarNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(PERMISO_EN_CURSO, solicitudEnCurso)
    }

    private companion object {
        const val PERMISO_CONSULTADO = "notificaciones_permiso_consultado"
        const val PERMISO_EN_CURSO = "notificaciones_permiso_en_curso"
    }
}
