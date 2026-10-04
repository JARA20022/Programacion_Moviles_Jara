package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Términos y condiciones",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Clínica SaludPlus permite consultar especialidades, " +
                    "elegir médicos y solicitar citas desde esta aplicación."
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Los datos ingresados se utilizan en esta demostración " +
                    "para identificar al paciente y mostrar sus citas."
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Las reservas se guardan temporalmente en el dispositivo " +
                    "mientras la aplicación permanece abierta."
        )

        Spacer(Modifier.height(24.dp))

        BotonPrincipal(texto = "Volver al registro", onClick = onVolver)
    }
}