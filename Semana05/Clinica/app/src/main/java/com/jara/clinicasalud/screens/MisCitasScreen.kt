package com.jara.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.ui.components.BotonMenu
import com.jara.clinicasalud.ui.components.TarjetaCita
import com.jara.clinicasalud.ui.theme.*

// Muestra en LazyColumn las citas del estado compartido.
// Los colores de cada estado los dibuja el componente TarjetaCita.
// Versión final de mejora-ia: la cancelación requiere confirmación.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitas(
    citas: List<Cita>,
    onAbrirMenu: () -> Unit,
    onCancelar: (Int) -> Unit
) {
    // Guardamos el ID de la cita elegida para mostrar el diálogo.
    var citaPendienteId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis citas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonMenu(onClick = onAbrirMenu)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (citas.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no tienes citas registradas.",
                        color = TextoSecundario
                    )
                }
            } else {
                // Las últimas citas registradas aparecen primero.
                items(citas.asReversed(), key = { it.id }) { cita ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TarjetaCita(cita)

                        // Solo una cita confirmada ofrece la acción de cancelar.
                        if (cita.estado == "Confirmada") {
                            OutlinedButton(
                                onClick = { citaPendienteId = cita.id },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFB3261E)
                                )
                            ) {
                                Text("Cancelar cita")
                            }
                        }
                    }
                }
            }
        }
    }
    // Consultar la lista actual evita operar sobre una copia desactualizada.
    val citaPendiente = citas.firstOrNull {
        it.id == citaPendienteId && it.estado == "Confirmada"
    }

    if (citaPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                // Atrás o tocar fuera conserva la cita.
                citaPendienteId = null
            },
            title = {
                Text("¿Cancelar cita?")
            },
            text = {
                Text(
                    "¿Deseas cancelar la cita con " + citaPendiente.medico.nombre +
                            " del " + citaPendiente.fecha + " a las " +
                            citaPendiente.hora + "?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Se cierra el diálogo y se actualiza el estado compartido.
                        citaPendienteId = null
                        onCancelar(citaPendiente.id)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFB3261E)
                    )
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaPendienteId = null }) {
                    Text("Conservar cita")
                }
            },
            containerColor = Color.White,
            titleContentColor = TextoPrincipal,
            textContentColor = TextoSecundario
        )
    }

}

