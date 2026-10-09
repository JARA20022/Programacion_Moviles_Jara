package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.saludplus.citas.ui.theme.LavenderLight
import com.saludplus.citas.ui.theme.SoftLime
import com.saludplus.citas.ui.theme.SuccessGreen
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import com.saludplus.citas.ui.theme.VioletSecondary

@Composable
fun MisCitasScreen(onAgendarCita: () -> Unit) {
    val usuario = Repositorio.usuarioActual
    val citas = usuario?.let {
        Repositorio.citasDelUsuario(it.id)
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Agenda de Citas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlue
        )

        Spacer(Modifier.height(16.dp))

        if (citas.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(LavenderLight, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = null,
                        tint = IndigoPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Aún no tienes citas agendadas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlue
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Reserva una cita eligiendo tu sede y médico de preferencia.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(28.dp))

                BotonPrincipal(
                    texto = "Agendar una cita",
                    containerColor = IndigoPrimary,
                    onClick = onAgendarCita,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(citas, key = { it.id }) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
                    val sede = Repositorio.sedes.find { s -> s.id == cita.sedeId || s.id == medico?.sedeId }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = medico?.nombre ?: "Médico",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Box(
                                    modifier = Modifier
                                        .background(SoftLime, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = cita.estado,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                            }

                            Text(
                                text = especialidad?.nombre.orEmpty(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VioletSecondary
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Sede: ${sede?.nombre ?: "Sede Principal"}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Text(
                                text = "Fecha: ${cita.fecha}  ·  Hora: ${cita.hora}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = IndigoPrimary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
