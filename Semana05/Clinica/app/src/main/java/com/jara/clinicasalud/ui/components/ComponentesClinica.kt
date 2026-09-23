package com.jara.clinicasalud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.model.Medico
import com.jara.clinicasalud.ui.theme.*

// Componentes visuales extraídos de MainActivity.
// Reciben datos y acciones por parámetros; el estado compartido permanece
// en NavegacionClinica. Ninguno crea otro NavController.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuLateral(
    rutaActual: String,
    onDestinoSeleccionado: (String) -> Unit
) {
    // Cada elemento relaciona una ruta con su texto visible.
    val destinos = listOf(
        "inicio" to "Inicio",
        "mis_citas" to "Mis citas",
        "historial" to "Historial médico"
    )

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Datos de presentación tomados del diseño de referencia.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(FondoIcono, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JP",
                        color = MoradoClinica,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Juan Pérez",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Paciente",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }
            }

            HorizontalDivider(color = FondoCompletada)

            Spacer(Modifier.height(16.dp))

            destinos.forEach { (ruta, titulo) ->
                val seleccionado = rutaActual == ruta

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = titulo,
                            fontWeight = if (seleccionado) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    icon = {
                        Text(
                            text = "○",
                            fontSize = 26.sp
                        )
                    },
                    selected = seleccionado,
                    onClick = {
                        onDestinoSeleccionado(ruta)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = FondoIcono,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = MoradoClinica,
                        unselectedTextColor = TextoPrincipal,
                        selectedIconColor = MoradoClinica,
                        unselectedIconColor = TextoPrincipal
                    )
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TarjetaMedico(
    medico: Medico,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoMedico()

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = medico.especialidad,
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.width(8.dp))

            CalificacionMedico(medico.calificacion)
        }
    }
}

@Composable
fun TarjetaCita(cita: Cita) {
    val confirmada = cita.estado == "Confirmada"
    val fondoEstado = if (confirmada) FondoConfirmacion else FondoCompletada
    val textoEstado = if (confirmada) VerdeConfirmacion else TextoSecundario

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // IntrinsicSize.Min ajusta la franja a la altura del contenido.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        if (confirmada) MoradoClinica else Color.Transparent
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = cita.medico.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(8.dp))

                // Verde para Confirmada y gris para Completada.
                Text(
                    text = cita.estado,
                    modifier = Modifier
                        .background(fondoEstado, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = textoEstado
                )
            }
        }
    }
}

@Composable
fun BotonMenu(
    onClick: () -> Unit,
    color: Color = TextoPrincipal
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "Abrir menú"
        }
    ) {
        Text(
            text = "☰",
            fontSize = 26.sp,
            color = color
        )
    }
}

@Composable
fun BotonRegresar(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "Regresar"
        }
    ) {
        Text(
            text = "←",
            fontSize = 26.sp,
            color = TextoPrincipal
        )
    }
}

@Composable
fun OpcionAgenda(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = if (seleccionada) MoradoClinica else FondoTarjeta
    val colorTexto = if (seleccionada) Color.White else TextoPrincipal

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(fondo)
            .selectable(
                selected = seleccionada,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = if (seleccionada) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = colorTexto,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CalificacionMedico(calificacion: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "★",
            fontSize = 18.sp,
            color = DoradoEstrella
        )
        Text(
            text = calificacion,
            fontSize = 12.sp,
            color = TextoSecundario
        )
    }
}

@Composable
fun IconoMedico(tamano: Dp = 44.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(FondoIcono, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(tamano * 0.55f)
                .height(tamano * 0.12f)
                .background(MoradoClinica)
        )
        Box(
            modifier = Modifier
                .width(tamano * 0.12f)
                .height(tamano * 0.55f)
                .background(MoradoClinica)
        )
    }
}
