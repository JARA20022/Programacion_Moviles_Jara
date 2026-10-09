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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BluePrimary
import com.saludplus.citas.ui.theme.NavyBlue
import com.saludplus.citas.ui.theme.PastelBlue
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary

@Composable
fun SedesScreen(
    onVolver: () -> Unit,
    onSeleccionarSede: () -> Unit
) {
    val sedes = Repositorio.sedes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = NavyBlue
                )
            }

            Text(
                text = "Seleccionar Sede",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Elige la sede médica donde deseas recibir atención",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = sedes,
                key = { it.id }
            ) { sede ->
                val esSeleccionada = Repositorio.sedeSeleccionada?.id == sede.id

                Card(
                    onClick = {
                        Repositorio.sedeSeleccionada = sede
                        onSeleccionarSede()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceWhite
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (esSeleccionada) 4.dp else 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = PastelBlue,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = sede.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = sede.direccion,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )

                            Text(
                                text = "Distrito: ${sede.distrito}",
                                fontSize = 12.sp,
                                color = BluePrimary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Tel: ${sede.telefono} · ${sede.horario}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Elegir sede",
                            tint = BluePrimary
                        )
                    }
                }
            }
        }
    }
}
