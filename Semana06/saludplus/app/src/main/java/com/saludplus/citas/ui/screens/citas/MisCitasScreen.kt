package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun MisCitasScreen(onAgendarCita: () -> Unit) {
    val usuario = Repositorio.usuarioActual
    val citas = usuario?.let {
        Repositorio.citasDelUsuario(it.id)
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        if (citas.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Aún no tienes citas agendadas.")

                Spacer(Modifier.height(20.dp))

                BotonPrincipal(
                    texto = "Agendar una cita",
                    onClick = onAgendarCita
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas, key = { it.id }) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let {
                        Repositorio.obtenerEspecialidad(it.especialidadId)
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = medico?.nombre ?: "Médico",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(especialidad?.nombre.orEmpty())
                            Spacer(Modifier.height(10.dp))
                            Text("Fecha: ${cita.fecha}")
                            Text("Hora: ${cita.hora}")
                            Text("Estado: ${cita.estado}")
                        }
                    }
                }
            }
        }
    }
}