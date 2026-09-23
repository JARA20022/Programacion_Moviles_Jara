package com.jara.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.tecsupfit.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import com.jara.tecsupfit.model.ReservaFit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionFit(reserva: ReservaFit?, onVerReservas: () -> Unit) {
    Scaffold(containerColor = Color.White) { innerPadding ->
        // La referencia no incluye topBar ni bottomBar en la confirmación.
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (reserva == null) {
                Text("Reserva no disponible", color = TextoFit)
            } else {
                Box(
                    Modifier.size(80.dp).background(FondoVerdeFit, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("✓", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = VerdeEstado) }
                Spacer(Modifier.height(24.dp))
                Text("¡Cupo reservado!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextoFit)
                Spacer(Modifier.height(8.dp))
                Text(reserva.clase.nombre, color = TextoSecundarioFit)
                Text(
                    "${reserva.clase.dia}, ${reserva.clase.hora} · ${reserva.clase.sala}",
                    color = TextoSecundarioFit, fontSize = 13.sp
                )
                if (reserva.cupos > 1) {
                    Text("${reserva.cupos} cupos", color = TextoSecundarioFit, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(36.dp))
            Button(
                onClick = onVerReservas,
                modifier = Modifier.widthIn(min = 220.dp).heightIn(min = 48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FondoTarjetaFit, contentColor = TextoFit)
            ) { Text("Ver mis reservas") }
        }
    }
}
