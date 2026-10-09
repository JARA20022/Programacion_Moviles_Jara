package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.CardSurface
import com.saludplus.citas.ui.theme.ErrorContainer
import com.saludplus.citas.ui.theme.ErrorRed
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.PetroleumVariant
import com.saludplus.citas.ui.theme.SurfaceSecondary
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onSeleccionarMedico: (Int) -> Unit
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medicos = Repositorio.medicosPorEspecialidad(especialidadId)
    val sedeActual = Repositorio.sedeSeleccionada
    val idioma = Locale.forLanguageTag("es-PE")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = PetroleumPrimary
                )
            }

            Text(
                text = especialidad?.nombre ?: "Médicos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PetroleumPrimary
            )
        }

        if (sedeActual != null) {
            Text(
                text = "Sede: ${sedeActual.nombre}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PetroleumVariant,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        if (medicos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay doctores disponibles para esta especialidad en la sede elegida.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = medicos,
                    key = { it.id }
                ) { medico ->
                    val sedeMedico = Repositorio.sedes.find { it.id == medico.sedeId }
                    val tieneEmergencia = medico.horarios.any { it.emergencia }

                    val textoHorarios = medico.horarios.joinToString("; ") { hor ->
                        val diaNombre = hor.dia.getDisplayName(TextStyle.SHORT, idioma).replaceFirstChar { it.titlecase(idioma) }
                        "$diaNombre ${hor.horaInicio} - ${hor.horaFin}"
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardSurface
                        ),
                        border = BorderStroke(1.dp, BorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FotoMedico(
                                    medicoId = medico.id,
                                    nombre = medico.nombre,
                                    tamaño = 72.dp
                                )

                                Spacer(Modifier.width(14.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = medico.nombre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            color = TextPrimary
                                        )

                                        if (tieneEmergencia) {
                                            Box(
                                                modifier = Modifier
                                                    .background(ErrorContainer, RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "Emergencia",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ErrorRed
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(2.dp))

                                    Text(
                                        text = especialidad?.nombre ?: "Especialidad",
                                        fontSize = 15.sp,
                                        color = PetroleumVariant,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier.padding(end = 2.dp)
                                        )
                                        Text(
                                            text = "${medico.calificacion} · ${medico.experiencia} años exp.",
                                            fontSize = 13.sp,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(12.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = "Código profesional: ${medico.codigoProfesional}",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Sede: ${sedeMedico?.nombre ?: "Sede principal"}",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = PetroleumPrimary,
                                    modifier = Modifier.padding(end = 4.dp).size(18.dp)
                                )
                                Text(
                                    text = "Teléfono: ${medico.telefono}",
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (textoHorarios.isNotEmpty()) {
                                Spacer(Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SurfaceSecondary, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Horarios: $textoHorarios",
                                        fontSize = 12.sp,
                                        color = PetroleumPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(Modifier.height(14.dp))

                            BotonPrincipal(
                                texto = "Ver disponibilidad",
                                containerColor = PetroleumPrimary,
                                onClick = { onSeleccionarMedico(medico.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
