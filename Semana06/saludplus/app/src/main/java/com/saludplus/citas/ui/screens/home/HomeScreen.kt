package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoEspecialidad

private val Azul = Color(0xFF2563EB)

@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onMisCitas: () -> Unit,
    onPerfil: () -> Unit,
    onResultados: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onSedes: () -> Unit,
    onMisDoctores: () -> Unit
) {
    val nombre = Repositorio.usuarioActual
        ?.nombre
        ?.trim()
        ?.substringBefore(" ")
        ?: "Paciente"

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 12.dp,
                        top = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "¡Hola, $nombre!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = onNotificaciones) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones"
                    )
                }
            }
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaAcceso(
                    titulo = "Agendar cita",
                    icono = Icons.Default.CalendarMonth,
                    fondo = Color(0xFFE6F1FF),
                    colorIcono = Azul,
                    onClick = onAgendarCita,
                    modifier = Modifier.weight(1f)
                )

                TarjetaAcceso(
                    titulo = "Mis citas",
                    icono = Icons.Default.EventNote,
                    fondo = Color(0xFFE4F8EE),
                    colorIcono = Color(0xFF22A06B),
                    onClick = onMisCitas,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaAcceso(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    fondo = Color(0xFFF1E8FF),
                    colorIcono = Color(0xFF9859D4),
                    onClick = onPerfil,
                    modifier = Modifier.weight(1f)
                )

                TarjetaAcceso(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    fondo = Color(0xFFFFF0DB),
                    colorIcono = Color(0xFFE8912D),
                    onClick = onResultados,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaAcceso(
                    titulo = "Sedes",
                    icono = Icons.Default.LocationOn,
                    fondo = Color(0xFFE3F2FD),
                    colorIcono = Color(0xFF0288D1),
                    onClick = onSedes,
                    modifier = Modifier.weight(1f)
                )

                TarjetaAcceso(
                    titulo = "Mis doctores",
                    icono = Icons.Default.MedicalServices,
                    fondo = Color(0xFFF3E5F5),
                    colorIcono = Color(0xFF7B1FA2),
                    onClick = onMisDoctores,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Especialidades destacadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = Repositorio.especialidadesDestacadas(),
                    key = { it.id }
                ) { especialidad ->
                    Card(
                        onClick = {
                            onEspecialidad(especialidad.id)
                        },
                        modifier = Modifier.size(
                            width = 130.dp,
                            height = 108.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F8FC)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            IconoEspecialidad(
                                nombre = especialidad.nombre,
                                tamaño = 40.dp
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = especialidad.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TarjetaAcceso(
    titulo: String,
    icono: ImageVector,
    fondo: Color,
    colorIcono: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(116.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondo
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(30.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
