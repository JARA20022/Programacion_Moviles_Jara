package com.jara.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.jara.tecsupfit.navigation.NavegacionFit
import com.jara.tecsupfit.ui.theme.VerdeFit
import com.jara.tecsupfit.ui.theme.TextoFit

// Punto de entrada: el comportamiento queda en navegación y pantallas.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Se fija el tema claro para conservar los colores de la referencia.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = VerdeFit, background = Color.White,
                    surface = Color.White, onSurface = TextoFit
                )
            ) { NavegacionFit() }
        }
    }
}
