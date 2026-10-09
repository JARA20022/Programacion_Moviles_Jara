package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                text = "Confirmar cita",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.")
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = medico.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(especialidad?.nombre.orEmpty())
                }
            }

            Spacer(Modifier.height(24.dp))

            DatoCita("Fecha", fecha)
            DatoCita("Hora", hora)
            DatoCita(
                "Precio de consulta",
                "S/ ${medico.precioConsulta.toInt()}"
            )

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
                    text = "Esta fecha u hora no está disponible. Vuelve a elegir otra.",
                    color = Color(0xFFB00020)
                )
            }

            Spacer(Modifier.height(30.dp))

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
        }
    }
}

@Composable
private fun DatoCita(
    etiqueta: String,
    valor: String
) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}