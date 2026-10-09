package com.saludplus.citas.ui.screens.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoEspecialidad
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.CardSurface
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.PetroleumVariant
import com.saludplus.citas.ui.theme.SurfaceSecondary
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onAbrirMenu: () -> Unit = {},
    onSedes: () -> Unit,
    onMisDoctores: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onNotificaciones: () -> Unit,
    onAgendarCita: () -> Unit = {}
) {
    val usuario = Repositorio.usuarioActual
    val nombre = usuario?.nombre?.trim()?.substringBefore(" ") ?: "Paciente"
    val sede = Repositorio.sedeSeleccionada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        // TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAbrirMenu) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú principal",
                    tint = PetroleumPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "¡Hola, $nombre!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "¿Qué deseas hacer hoy?",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            IconButton(onClick = onNotificaciones) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notificaciones",
                    tint = PetroleumPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Sede status card
            Card(
                onClick = onSedes,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardSurface
                ),
                border = BorderStroke(1.dp, BorderLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(SurfaceSecondary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PetroleumPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (sede != null) "Sede seleccionada" else "Sede no seleccionada",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PetroleumVariant
                        )
                        Text(
                            text = sede?.nombre ?: "Elige una sede desde el menú",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Cambiar sede",
                        tint = PetroleumPrimary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Primary Access Cards (2 columns)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaAccesoHome(
                    titulo = "Sedes",
                    icono = Icons.Default.LocationOn,
                    fondo = CardSurface,
                    colorIcono = PetroleumPrimary,
                    onClick = onSedes,
                    modifier = Modifier.weight(1f)
                )

                TarjetaAccesoHome(
                    titulo = "Doctores",
                    icono = Icons.Default.MedicalServices,
                    fondo = CardSurface,
                    colorIcono = PetroleumVariant,
                    onClick = onMisDoctores,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaAccesoHome(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    fondo = CardSurface,
                    colorIcono = PetroleumPrimary,
                    onClick = onResultados,
                    modifier = Modifier.weight(1f)
                )

                TarjetaAccesoHome(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    fondo = CardSurface,
                    colorIcono = PetroleumVariant,
                    onClick = onPerfil,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(28.dp))

            // Specialties section in 2 columns
            Text(
                text = "Especialidades médicas",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(Modifier.height(14.dp))

            val especialidadesChunked = Repositorio.especialidades.chunked(2)

            especialidadesChunked.forEach { filaItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (especialidad in filaItems) {
                        Card(
                            onClick = { onEspecialidad(especialidad.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(118.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CardSurface
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                IconoEspecialidad(
                                    nombre = especialidad.nombre,
                                    tamaño = 42.dp
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = especialidad.nombre,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    if (filaItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaAccesoHome(
    titulo: String,
    icono: ImageVector,
    fondo: Color,
    colorIcono: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondo
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(SurfaceSecondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = titulo,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
