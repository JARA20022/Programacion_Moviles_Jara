package com.jara.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jara.clinicasalud.navigation.NavegacionClinica
import com.jara.clinicasalud.ui.theme.ClinicaSaludTheme

// La actividad inicia Compose; las pantallas y la navegación están separadas.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClinicaSaludTheme {
                NavegacionClinica()
            }
        }
    }
}
