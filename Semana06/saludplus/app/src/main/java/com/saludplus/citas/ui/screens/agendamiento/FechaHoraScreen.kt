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

    var error by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    val semanaMostrada = semanasAdelante.coerceIn(0, 12)

    val dias = remember(hoy, semanaMostrada) {
        val inicio = hoy.plusWeeks(semanaMostrada.toLong())
        val formatoDia = DateTimeFormatter.ofPattern("EEE", idioma)

        // Siete días consecutivos contienen cinco días de lunes a viernes.
        (0L..6L)
            .map { desplazamiento ->
                inicio.plusDays(desplazamiento)
            }
            .filter { fecha ->
                fecha.dayOfWeek != DayOfWeek.SATURDAY &&
                        fecha.dayOfWeek != DayOfWeek.SUNDAY
            }
            .map { fecha ->
                DiaCalendario(
                    fecha = fecha,
                    nombre = fecha
                        .format(formatoDia)
                        .replace(".", "")
                        .replaceFirstChar { it.titlecase(idioma) }
                )
            }
    }

    val tituloMes = dias.first().fecha
        .format(DateTimeFormatter.ofPattern("MMMM yyyy", idioma))
        .replaceFirstChar { it.titlecase(idioma) }

    val fechaVisible = dias.any {
        it.fecha.toString() == fechaSeleccionada
    }

    val horarios = fechaSeleccionada?.let { fecha ->
        if (fechaVisible) {
            Repositorio.horariosDisponibles(medicoId, fecha)
        } else {
            emptyList()
        }
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text = Repositorio
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
                    contentDescription = "Semana anterior"
                )
            }

            Text(
                text = tituloMes,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
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
                    contentDescription = "Semana siguiente"
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(dias, key = { it.fecha.toString() }) { dia ->
                val fecha = dia.fecha.toString()
                val seleccionado = fechaSeleccionada == fecha

                Card(
                    onClick = {
                        fechaSeleccionada = fecha
                        horaSeleccionada = null
                        error = ""
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

        if (!fechaVisible) {
            Text("Selecciona primero un día.")
        } else if (horarios.isEmpty()) {
            Text("No quedan horarios disponibles para este día.")
        } else {
            val filas = (horarios.size + 2) / 3
            val alturaCuadricula = (
                    filas * 51 + (filas - 1) * 10
                    ).dp

            // Altura definida para colocar la cuadrícula dentro del scroll.
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
                        modifier = Modifier.height(51.dp),
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
            }
        }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = error,
                color = Color(0xFFB00020)
            )
        }

        Spacer(Modifier.height(20.dp))

        val fecha = fechaSeleccionada
        val hora = horaSeleccionada

        BotonPrincipal(
            texto = "Continuar",
            enabled = fechaVisible &&
                    fecha != null &&
                    hora != null &&
                    hora in horarios,
            onClick = {
                if (
                    fecha != null &&
                    hora != null &&
                    dias.any { it.fecha.toString() == fecha } &&
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

        Spacer(Modifier.height(16.dp))
    }
}