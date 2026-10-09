package com.saludplus.citas.ui.screens.resultados

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.CoralAction
import com.saludplus.citas.ui.theme.CoralLight
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
fun ResultadosScreen() {
    val usuario = Repositorio.usuarioActual
    val usuarioId = usuario?.id ?: 0
    val nombrePaciente = usuario?.nombre ?: "Paciente"

    val resultados = Repositorio.resultadosDelUsuario(usuarioId)
    val disponibles = resultados.count { it.estado == "Disponible" }
    val citasUsuario = if (usuario != null) Repositorio.citasDelUsuario(usuario.id) else emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Resultados Médicos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlue
        )

        Text(
            text = "Paciente: $nombrePaciente",
            fontSize = 13.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SurfaceWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Biotech,
                        contentDescription = null,
                        tint = IndigoPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = "$disponibles exámenes disponibles",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkBlue
                    )

                    Text(
                        text = "Total de exámenes registrados: ${resultados.size}",
                        fontSize = 12.sp,
                        color = VioletSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección Laboratorio
            item {
                Text(
                    text = "Resultados de laboratorio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlue
                )
            }

            if (resultados.isEmpty()) {
                item {
                    Text(
                        text = "No hay resultados de laboratorio registrados.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            } else {
                items(resultados, key = { it.id }) { res ->
                    val esDisponible = res.estado == "Disponible"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Biotech,
                                        contentDescription = null,
                                        tint = VioletSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = res.examen,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (esDisponible) SoftLime else CoralLight,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = res.estado,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (esDisponible) SuccessGreen else CoralAction
                                    )
                                }
                            }

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Fecha: ${res.fecha}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )

                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(Modifier.height(8.dp))

                            if (esDisponible) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Resultado",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "${res.resultado} ${res.unidad}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InkBlue
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Rango de referencia",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = res.referencia,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Resultado pendiente de procesamiento en laboratorio",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CoralAction
                                )
                            }
                        }
                    }
                }
            }

            // Sección Resumen de Citas
            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Resumen de citas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlue
                )
            }

            if (citasUsuario.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Text(
                            text = "Aún no tienes citas registradas.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(citasUsuario, key = { "cita_${it.id}" }) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
                    val sede = Repositorio.sedes.find { s -> s.id == cita.sedeId || s.id == medico?.sedeId }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(LavenderLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = IndigoPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = medico?.nombre ?: "Médico",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${especialidad?.nombre ?: ""} · ${sede?.nombre ?: ""}",
                                    fontSize = 12.sp,
                                    color = VioletSecondary
                                )
                                Text(
                                    text = "${cita.fecha} - ${cita.hora} (${cita.estado})",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
