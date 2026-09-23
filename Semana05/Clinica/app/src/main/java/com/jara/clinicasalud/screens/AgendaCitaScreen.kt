package com.jara.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jara.clinicasalud.model.FechaCita
import com.jara.clinicasalud.model.Medico
import com.jara.clinicasalud.ui.components.BotonRegresar
import com.jara.clinicasalud.ui.components.OpcionAgenda
import com.jara.clinicasalud.ui.theme.*

// Mantiene la selección de fecha y hora con remember/mutableStateOf.
// onConfirmar entrega esos valores al estado compartido de navegación.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaCita(
    navController: NavController,
    medico: Medico,
    onConfirmar: (String, String) -> Unit
) {
    val fechas = listOf(
        FechaCita("Jue", "26", "Jueves"),
        FechaCita("Vie", "27", "Viernes"),
        FechaCita("Sáb", "28", "Sábado")
    )

    val horas = listOf("9:00", "10:30", "3:00")

    // Una variable por grupo garantiza una selección única.
    var fechaSeleccionada by remember(medico.id) {
        mutableStateOf("27")
    }
    var horaSeleccionada by remember(medico.id) {
        mutableStateOf("10:30")
    }
    var confirmando by remember(medico.id) {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agendar cita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonRegresar {
                        navController.popBackStack()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Selecciona fecha",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fechas.forEach { fecha ->
                        OpcionAgenda(
                            texto = "${fecha.dia}\n${fecha.numero}",
                            seleccionada = fechaSeleccionada == fecha.numero,
                            onClick = {
                                fechaSeleccionada = fecha.numero
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Selecciona hora",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    horas.forEach { hora ->
                        OpcionAgenda(
                            texto = hora,
                            seleccionada = horaSeleccionada == hora,
                            onClick = {
                                horaSeleccionada = hora
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (!confirmando) {
                        confirmando = true

                        val fecha = fechas.first {
                            it.numero == fechaSeleccionada
                        }

                        val fechaCompleta =
                            "${fecha.diaCompleto} ${fecha.numero}"

                        val horaCompleta = if (horaSeleccionada == "3:00") {
                            "$horaSeleccionada pm"
                        } else {
                            "$horaSeleccionada am"
                        }

                        onConfirmar(fechaCompleta, horaCompleta)
                    }
                },
                enabled = !confirmando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoClinica,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar cita",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

