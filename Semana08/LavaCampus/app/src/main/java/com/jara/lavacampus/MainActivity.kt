package com.jara.lavacampus

import android.os.Bundle
import com.jara.lavacampus.ui.components.StudioFondo
import androidx.compose.ui.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jara.lavacampus.ui.navigation.NavLava
import com.jara.lavacampus.ui.theme.LavaCampusTheme
import com.jara.lavacampus.viewmodel.LavaViewModelFactory
import com.jara.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val factory = remember { LavaViewModelFactory(application as LavaApp) }
            val sesion: SesionViewModel = viewModel(factory = factory)
            val oscuro by sesion.modoOscuro.collectAsStateWithLifecycle()
            val restaurando by sesion.restaurando.collectAsStateWithLifecycle()
            // Primera apertura de esta edición visual: inicia en oscuro una sola vez.
            // Después se respeta siempre el selector de apariencia del usuario.
            val preferenciasVisuales = remember { getSharedPreferences("lavacampus_visual", MODE_PRIVATE) }
            LaunchedEffect(restaurando, oscuro) {
                if (!restaurando && !preferenciasVisuales.getBoolean("aurora_v2", false)) {
                    if (oscuro) preferenciasVisuales.edit().putBoolean("aurora_v2", true).apply()
                    else sesion.cambiarModoOscuro(true)
                }
            }
            SideEffect {
                val transparente = android.graphics.Color.TRANSPARENT
                val estilo = if(oscuro) SystemBarStyle.dark(transparente)
                    else SystemBarStyle.light(transparente, transparente)
                enableEdgeToEdge(statusBarStyle=estilo,navigationBarStyle=estilo)
            }
            LavaCampusTheme(darkTheme = oscuro) {
                StudioFondo {
                Scaffold(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { padding ->
                    NavLava(sesion, Modifier.padding(padding).consumeWindowInsets(padding))
                }
                }
            }
        }
    }
}
