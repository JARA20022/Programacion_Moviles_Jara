package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BluePrimary
import com.saludplus.citas.ui.theme.CoralLight
import com.saludplus.citas.ui.theme.ErrorRed
import com.saludplus.citas.ui.theme.NavyBlue
import com.saludplus.citas.ui.theme.PastelBlue
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class DiaCalendario(
    val fecha: LocalDate,
    val nombre: String,
    val esLaborable: Boolean,
    val esEmergencia: Boolean
)

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (String, String) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val hoy = LocalDate.now()
    val idioma = Locale.forLanguageTag("es-PE")

    var semanasAdelante by rememberSaveable(medicoId) {
        mutableIntStateOf(0)
    }

    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf<String?>(null)
    }

    var horaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf<String?>(null)
    }

    var error by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    val semanaMostrada = semanasAdelante.coerceIn(0, 12)

    val dias = remember(hoy, semanaMostrada, medico) {
        if (medico == null) emptyList()
        else {
            val inicio = hoy.plusWeeks(semanaMostrada.toLong())
            val formatoDia = DateTimeFormatter.ofPattern("EEE", idioma)

            (0L..6L).map { desplazamiento ->
                val fechaCalculada = inicio.plusDays(desplazamiento)
                val horarioMed = medico.horarios.find { it.dia == fechaCalculada.dayOfWeek }
                val esLaborable = horarioMed != null
                val esEmergencia = horarioMed?.emergencia == true

                DiaCalendario(
                    fecha = fechaCalculada,
                    nombre = fechaCalculada
                        .format(formatoDia)
                        .replace(".", "")
                        .replaceFirstChar { it.titlecase(idioma) },
                    esLaborable = esLaborable,
                    esEmergencia = esEmergencia
                )
            }
        }
    }

    val tituloMes = if (dias.isNotEmpty()) {
        dias.first().fecha
            .format(DateTimeFormatter.ofPattern("MMMM yyyy", idioma))
            .replaceFirstChar { it.titlecase(idioma) }
    } else ""

    val fechaValida = dias.any { it.fecha.toString() == fechaSeleccionada && it.esLaborable }

    val horarios = fechaSeleccionada?.let { fecha ->
        if (fechaValida) {
            Repositorio.horariosDisponibles(medicoId, fecha)
        } else {
            emptyList()
        }
    } ?: emptyList()

    val diaSeleccionadoObj = dias.find { it.fecha.toString() == fechaSeleccionada }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = NavyBlue
                )
            }

            Text(
                text = "Seleccionar fecha y hora",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
        }

        Spacer(Modifier.height(16.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.", color = ErrorRed)
            return@Column
        }

        // Card médico
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FotoMedico(
                    medicoId = medico.id,
                    nombre = medico.nombre,
                    tamaño = 60.dp
                )

                Column {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = Repositorio.obtenerEspecialidad(medico.especialidadId)?.nombre.orEmpty(),
                        fontSize = 13.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "CMP: ${medico.codigoProfesional} · S/ ${medico.precioConsulta.toInt()}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Selector semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                enabled = semanaMostrada > 0,
                onClick = {
                    semanasAdelante = (semanaMostrada - 1).coerceAtLeast(0)
                    fechaSeleccionada = null
                    horaSeleccionada = null
                    error = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Semana anterior",
                    tint = if (semanaMostrada > 0) NavyBlue else TextSecondary
                )
            }

            Text(
                text = tituloMes,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )

            IconButton(
                enabled = semanaMostrada < 12,
                onClick = {
                    semanasAdelante = (semanaMostrada + 1).coerceAtMost(12)
                    fechaSeleccionada = null
                    horaSeleccionada = null
                    error = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Semana siguiente",
                    tint = if (semanaMostrada < 12) NavyBlue else TextSecondary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Días row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(dias, key = { it.fecha.toString() }) { dia ->
                val fechaStr = dia.fecha.toString()
                val seleccionado = fechaSeleccionada == fechaStr

                Card(
                    onClick = {
                        if (dia.esLaborable) {
                            fechaSeleccionada = fechaStr
                            horaSeleccionada = null
                            error = ""
                        } else {
                            error = "El doctor no atiende este día."
                        }
                    },
                    modifier = Modifier
                        .width(64.dp)
                        .height(82.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            seleccionado -> BluePrimary
                            dia.esLaborable && dia.esEmergencia -> CoralLight
                            dia.esLaborable -> PastelBlue
                            else -> BackgroundMain
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (seleccionado) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia.nombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = when {
                                seleccionado -> SurfaceWhite
                                dia.esLaborable && dia.esEmergencia -> ErrorRed
                                dia.esLaborable -> NavyBlue
                                else -> TextSecondary
                            }
                        )

                        Text(
                            text = dia.fecha.dayOfMonth.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                seleccionado -> SurfaceWhite
                                dia.esLaborable -> TextPrimary
                                else -> TextSecondary
                            }
                        )

                        if (dia.esEmergencia) {
                            Text(
                                text = "Emerg.",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (seleccionado) SurfaceWhite else ErrorRed
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Horarios disponibles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )

            if (diaSeleccionadoObj?.esEmergencia == true) {
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .background(CoralLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Atención de Emergencia",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (fechaSeleccionada == null) {
            Text(
                text = "Selecciona un día disponible para ver sus horarios.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        } else if (!fechaValida) {
            Text(
                text = "El doctor no atiende en el día seleccionado.",
                fontSize = 13.sp,
                color = ErrorRed
            )
        } else if (horarios.isEmpty()) {
            Text(
                text = "No quedan horarios disponibles para este día.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        } else {
            val filas = (horarios.size + 2) / 3
            val alturaCuadricula = (filas * 50 + (filas - 1) * 10).dp

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaCuadricula),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                gridItems(horarios, key = { it }) { hora ->
                    val seleccionado = horaSeleccionada == hora

                    Card(
                        onClick = {
                            horaSeleccionada = hora
                            error = ""
                        },
                        modifier = Modifier.height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionado) BluePrimary else SurfaceWhite
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (seleccionado) 3.dp else 1.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hora,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (seleccionado) SurfaceWhite else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = error,
                color = ErrorRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(24.dp))

        val fecha = fechaSeleccionada
        val hora = horaSeleccionada

        BotonPrincipal(
            texto = "Continuar",
            containerColor = BluePrimary,
            enabled = fechaValida && fecha != null && hora != null && hora in horarios,
            onClick = {
                if (
                    fecha != null &&
                    hora != null &&
                    hora in Repositorio.horariosDisponibles(medicoId, fecha)
                ) {
                    error = ""
                    onContinuar(fecha, hora)
                } else {
                    horaSeleccionada = null
                    error = "El horario ya no está disponible. Elige otro."
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}
