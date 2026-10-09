package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FotoMedico

@Composable
fun MisDoctoresScreen(
    onVolver: () -> Unit,
    onSeleccionarMedico: (Int) -> Unit
) {
    val medicos = Repositorio.medicos

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Nuestros Médicos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = medicos,
                key = { it.id }
            ) { medico ->
                val especialidad = Repositorio.obtenerEspecialidad(medico.especialidadId)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
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
                                tamaño = 56.dp
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = medico.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF142C68)
                                )

                                Text(
                                    text = especialidad?.nombre ?: "Especialidad",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF2864E8),
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "★ ${medico.calificacion} · ${medico.experiencia} años de experiencia",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFEB9A24)
                                )

                                Text(
                                    text = "Consulta: S/ ${medico.precioConsulta.toInt()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = { onSeleccionarMedico(medico.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2864E8)
                            )
                        ) {
                            Text("Ver disponibilidad")
                        }
                    }
                }
            }
        }
    }
}
