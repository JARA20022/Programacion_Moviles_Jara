package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class DiaCalendario(
    val fecha: LocalDate,
    val nombre: String
)

private val azulFecha = Color(0xFF2864E8)
private val fondoFecha = Color(0xFFF3F6FB)

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

    val dias = remember(hoy, semanasAdelante) {
        generateSequence(
            hoy.plusWeeks(semanasAdelante.toLong())
        ) {
            it.plusDays(1)
        }
            .filter {
                it.dayOfWeek != DayOfWeek.SATURDAY &&
                        it.dayOfWeek != DayOfWeek.SUNDAY
            }
            .filter {
                !it.isAfter(hoy.plusDays(90))
            }
            .take(5)
            .map { fecha ->
                DiaCalendario(
                    fecha = fecha,
                    nombre = fecha
                        .format(
                            DateTimeFormatter.ofPattern(
                                "EEE",
                                idioma
                            )
                        )
                        .replace(".", "")
                        .replaceFirstChar {
                            it.titlecase(idioma)
                        }
                )
            }
            .toList()
    }

    val tituloMes = dias.firstOrNull()
        ?.fecha
        ?.format(
            DateTimeFormatter.ofPattern(
                "MMMM yyyy",
                idioma
            )
        )
        ?.replaceFirstChar {
            it.titlecase(idioma)
        }
        .orEmpty()

    val horarios = fechaSeleccionada?.let { fecha ->
        Repositorio.horariosDisponibles(
            medicoId,
            fecha
        )
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = "Seleccionar fecha y hora",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.")
            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = fondoFecha
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FotoMedico(
                    medicoId = medico.id,
                    nombre = medico.nombre,
                    tamaño = 56.dp
                )

                Column {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        Repositorio
                            .obtenerEspecialidad(medico.especialidadId)
                            ?.nombre
                            .orEmpty()
                    )
                }
            }
        }

        Spacer(Modifier.height(19.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    semanasAdelante--
                    fechaSeleccionada = null
                    horaSeleccionada = null
                },
                enabled = semanasAdelante > 0
            ) {
                Icon(
                    Icons.Filled.ChevronLeft,
                    contentDescription = "Semana anterior"
                )
            }

            Text(
                text = tituloMes,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    semanasAdelante++
                    fechaSeleccionada = null
                    horaSeleccionada = null
                },
                enabled = semanasAdelante < 12
            ) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = "Semana siguiente"
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = dias,
                key = { it.fecha.toString() }
            ) { dia ->
                val fecha = dia.fecha.toString()
                val seleccionado =
                    fechaSeleccionada == fecha

                Card(
                    onClick = {
                        fechaSeleccionada = fecha
                        horaSeleccionada = null
                    },
                    modifier = Modifier
                        .width(60.dp)
                        .height(74.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (seleccionado) {
                            azulFecha
                        } else {
                            fondoFecha
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia.nombre,
                            color = if (seleccionado) {
                                Color.White
                            } else {
                                Color.DarkGray
                            }
                        )

                        Text(
                            text = dia.fecha.dayOfMonth.toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (seleccionado) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Horarios disponibles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        if (fechaSeleccionada == null) {
            Text("Selecciona primero un día.")
        } else if (horarios.isEmpty()) {
            Text("No quedan horarios disponibles para este día.")
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                horarios.chunked(3).forEach { grupo ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        grupo.forEach { hora ->
                            val seleccionado =
                                horaSeleccionada == hora

                            Card(
                                onClick = {
                                    horaSeleccionada = hora
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(51.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (seleccionado) {
                                        azulFecha
                                    } else {
                                        fondoFecha
                                    }
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        color = if (seleccionado) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    )
                                }
                            }
                        }

                        repeat(3 - grupo.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        val fecha = fechaSeleccionada
        val hora = horaSeleccionada

        BotonPrincipal(
            texto = "Continuar",
            enabled = fecha != null &&
                    hora != null &&
                    hora in horarios,
            onClick = {
                if (
                    fecha != null &&
                    hora != null &&
                    hora in Repositorio.horariosDisponibles(
                        medicoId,
                        fecha
                    )
                ) {
                    onContinuar(fecha, hora)
                }
            }
        )

        Spacer(Modifier.height(16.dp))
    }
}