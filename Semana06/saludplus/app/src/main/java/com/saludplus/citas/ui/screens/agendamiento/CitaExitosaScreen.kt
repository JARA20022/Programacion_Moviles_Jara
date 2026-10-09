package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)

    val medico = cita?.let {
        Repositorio.obtenerMedico(it.medicoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (cita != null) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF22A06B)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (cita == null) {
                "No se encontró la cita"
            } else {
                "¡Cita agendada!"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (cita == null) {
                "Vuelve a Mis citas o a Inicio para continuar."
            } else {
                "${medico?.nombre.orEmpty()}\n${cita.fecha} a las ${cita.hora}"
            },
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        BotonPrincipal(
            texto = "Ver mis citas",
            onClick = onVerMisCitas
        )

        OutlinedButton(onClick = onIrInicio) {
            Text("Volver a Inicio")
        }
    }
}