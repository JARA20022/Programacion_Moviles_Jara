package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoEspecialidad
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BluePrimary
import com.saludplus.citas.ui.theme.NavyBlue
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary

@Composable
fun EspecialidadesScreen(
    onVolver: () -> Unit,
    onSeleccionarEspecialidad: (Int) -> Unit
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val especialidades = Repositorio.buscarEspecialidades(busqueda)
    val sedeActual = Repositorio.sedeSeleccionada

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BluePrimary,
        focusedLeadingIconColor = BluePrimary,
        focusedLabelColor = BluePrimary
    )

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
                text = "Especialidades",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
        }

        if (sedeActual != null) {
            Text(
                text = "Sede: ${sedeActual.nombre}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BluePrimary,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it.take(60) },
            label = { Text("Buscar especialidad") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = coloresCampo,
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        if (especialidades.isEmpty()) {
            Text(
                text = "No se encontraron especialidades.",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = especialidades,
                    key = { it.id }
                ) { especialidad ->
                    Card(
                        onClick = { onSeleccionarEspecialidad(especialidad.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceWhite
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconoEspecialidad(
                                nombre = especialidad.nombre,
                                tamaño = 48.dp
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 14.dp)
                            ) {
                                Text(
                                    text = especialidad.nombre,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Spacer(Modifier.height(2.dp))

                                Text(
                                    text = especialidad.descripcion,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = BluePrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
