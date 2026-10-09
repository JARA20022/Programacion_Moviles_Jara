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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private data class DiaCalendario(
    val nombre: String,
    val numero: String,
    val fecha: String
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

    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf<String?>(null)
    }

    var horaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf<String?>(null)
    }

    val dias = remember(hoy) {
        generateSequence(hoy) { it.plusDays(1) }
            .filter {
                it.dayOfWeek != DayOfWeek.SATURDAY &&
                        it.dayOfWeek != DayOfWeek.SUNDAY
            }
            .take(5)
            .map { fecha ->
                DiaCalendario(
                    nombre = fecha
                        .format(DateTimeFormatter.ofPattern("EEE", idioma))
                        .replace(".", "")
                        .replaceFirstChar { it.titlecase(idioma) },
                    numero = fecha.dayOfMonth.toString(),
                    fecha = fecha.toString()
                )
            }
            .toList()
    }

    val tituloMes = LocalDate.parse(dias.first().fecha)
        .format(DateTimeFormatter.ofPattern("MMMM yyyy", idioma))
        .replaceFirstChar { it.titlecase(idioma) }

    val horarios = fechaSeleccionada?.let { fecha ->
        Repositorio.horariosDisponibles(medicoId, fecha)
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = "Seleccionar fecha y hora",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(18.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.")
            return@Column
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = medico.nombre,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = Repositorio
                        .obtenerEspecialidad(medico.especialidadId)
                        ?.nombre
                        .orEmpty()
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = tituloMes,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(dias, key = { it.fecha }) { dia ->
                val seleccionado =
                    fechaSeleccionada == dia.fecha

                Card(
                    onClick = {
                        fechaSeleccionada = dia.fecha
                        horaSeleccionada = null
                    },
                    modifier = Modifier
                        .width(64.dp)
                        .height(76.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (seleccionado) {
                            Color(0xFF2563EB)
                        } else {
                            Color(0xFFF2F4F8)
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
                                Color.Black
                            }
                        )

                        Text(
                            text = dia.numero,
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

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Horarios disponibles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        if (fechaSeleccionada == null) {
            Text("Selecciona primero un día.")
            Spacer(Modifier.weight(1f))
        } else if (horarios.isEmpty()) {
            Text("No quedan horarios disponibles para este día.")
            Spacer(Modifier.weight(1f))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                gridItems(horarios, key = { it }) { hora ->
                    val seleccionado =
                        horaSeleccionada == hora

                    Card(
                        onClick = {
                            horaSeleccionada = hora
                        },
                        modifier = Modifier.height(52.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionado) {
                                Color(0xFF2563EB)
                            } else {
                                Color(0xFFF2F4F8)
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