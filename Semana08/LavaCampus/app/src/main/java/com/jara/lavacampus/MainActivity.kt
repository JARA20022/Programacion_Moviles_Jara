package com.jara.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
            LavaCampusTheme(darkTheme = oscuro) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { padding ->
                    NavLava(sesion, Modifier.padding(padding).consumeWindowInsets(padding))
                }
            }
        }
    }
}
