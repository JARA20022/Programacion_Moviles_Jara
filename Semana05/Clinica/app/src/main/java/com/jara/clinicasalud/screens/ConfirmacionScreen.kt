package com.jara.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.ui.theme.*

// Muestra la cita que recibe por parámetro.
// onVolverInicio ejecuta la acción definida en NavegacionClinica.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionCita(
    cita: Cita?,
    onVolverInicio: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirmación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cita != null) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(FondoConfirmacion, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 48.sp,
                        color = VerdeConfirmacion
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "¡Cita agendada!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = cita.medico.nombre,
                    fontSize = 15.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 14.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "La cita ya no está disponible en esta sesión.",
                    fontSize = 16.sp,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onVolverInicio,
                modifier = Modifier
                    .widthIn(min = 180.dp)
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FondoTarjeta,
                    contentColor = TextoPrincipal
                )
            ) {
                Text("Volver al inicio", fontSize = 14.sp)
            }
        }
    }
}

