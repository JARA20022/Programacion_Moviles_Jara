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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val azulConfirmar = Color(0xFF2864E8)
private val fondoConfirmar = Color(0xFFF5F8FD)

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onCitaAgendada: (Int) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var procesando by remember {
        mutableStateOf(false)
    }

    val horarioDisponible =
        hora in Repositorio.horariosDisponibles(
            medicoId,
            fecha
        )

    val fechaEnEspanol = remember(fecha) {
        runCatching {
            LocalDate.parse(fecha)
                .format(
                    DateTimeFormatter.ofPattern(
                        "EEEE d 'de' MMMM yyyy",
                        Locale.forLanguageTag("es-PE")
                    )
                )
                .replace(
                    "septiembre",
                    "setiembre",
                    ignoreCase = true
                )
                .replaceFirstChar {
                    it.titlecase(
                        Locale.forLanguageTag("es-PE")
                    )
                }
        }.getOrElse {
            fecha
        }
    }

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
                text = "Confirmar cita",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.")
            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = fondoConfirmar
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FotoMedico(
                    medicoId = medico.id,
                    nombre = medico.nombre,
                    tamaño = 58.dp
                )

                Column {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = especialidad?.nombre.orEmpty(),
                        color = Color(0xFF65728A)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                DatoCita(
                    Icons.Filled.CalendarMonth,
                    "Fecha",
                    fechaEnEspanol
                )

                HorizontalDivider(
                    color = Color(0xFFE9EDF4)
                )

                DatoCita(
                    Icons.Filled.AccessTime,
                    "Hora",
                    hora
                )

                HorizontalDivider(
                    color = Color(0xFFE9EDF4)
                )

                DatoCita(
                    Icons.Filled.Place,
                    "Tipo de atención",
                    "Consulta presencial"
                )

                HorizontalDivider(
                    color = Color(0xFFE9EDF4)
                )

                DatoCita(
                    Icons.Filled.LocationOn,
                    "Dirección",
                    "Av. Los Olivos 123, Lima"
                )

                HorizontalDivider(
                    color = Color(0xFFE9EDF4)
                )

                DatoCita(
                    Icons.Filled.Payments,
                    "Precio de consulta",
                    "S/ ${medico.precioConsulta.toInt()}"
                )
            }
        }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = error,
                color = Color(0xFFB00020)
            )
        }

        if (!horarioDisponible) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Esta fecha u hora ya no está disponible. Vuelve a elegir otra.",
                color = Color(0xFFB00020)
            )
        }

        Spacer(Modifier.height(26.dp))

        BotonPrincipal(
            texto = "Agendar cita",
            enabled = horarioDisponible && !procesando,
            onClick = {
                if (procesando) {
                    return@BotonPrincipal
                }

                procesando = true
                val usuarioId = Repositorio.usuarioActual?.id

                if (usuarioId == null) {
                    error = "Inicia sesión para reservar una cita."
                    procesando = false
                } else {
                    val cita = Repositorio.agendarCita(
                        usuarioId = usuarioId,
                        medicoId = medicoId,
                        fecha = fecha,
                        hora = hora
                    )

                    if (cita == null) {
                        error = "No se pudo reservar. Revisa la fecha y el horario."
                        procesando = false
                    } else {
                        onCitaAgendada(cita.id)
                    }
                }
            }
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun DatoCita(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    Color(0xFFEAF2FF),
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = azulConfirmar,
                modifier = Modifier.size(21.dp)
            )
        }

        Column {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF65728A)
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}