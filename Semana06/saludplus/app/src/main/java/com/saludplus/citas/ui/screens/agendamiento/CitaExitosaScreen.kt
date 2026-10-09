package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.CreamBackground
import com.saludplus.citas.ui.theme.IndigoPrimary
import com.saludplus.citas.ui.theme.InkBlue
import com.saludplus.citas.ui.theme.SoftLime
import com.saludplus.citas.ui.theme.SuccessGreen
import com.saludplus.citas.ui.theme.TextSecondary

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val sede = cita?.let { Repositorio.sedes.find { s -> s.id == it.sedeId } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(SoftLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (cita == null) "No se encontró la cita" else "¡Cita agendada con éxito!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlue,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        if (cita != null && medico != null) {
            Text(
                text = "${medico.nombre}\n${cita.fecha} a las ${cita.hora}\n${sede?.nombre ?: ""}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        } else {
            Text(
                text = "Vuelve a tus citas para revisar tus reservas.",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(36.dp))

        BotonPrincipal(
            texto = "Ver mis citas",
            containerColor = IndigoPrimary,
            onClick = onVerMisCitas
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onIrInicio,
            modifier = Modifier.height(52.dp)
        ) {
            Text(
                text = "Volver a Inicio",
                color = InkBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
