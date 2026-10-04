package com.jara.tecsupfit.screens

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
import com.jara.tecsupfit.model.ReservaFit
import com.jara.tecsupfit.ui.components.BotonAtrasFit
import com.jara.tecsupfit.ui.components.TarjetaReserva
import com.jara.tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasFit(
    reservas: List<ReservaFit>,
    onCancelar: (Int) -> Unit,
    barraInferior: @Composable () -> Unit = {},
    onVolver: (() -> Unit)? = null
) {
    // Elegir una reserva abre el diálogo; todavía no cambia su estado.
    var reservaPendienteId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Mis reservas", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { onVolver?.let { BotonAtrasFit(it) } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, titleContentColor = TextoFit
                )
            )
        },
        bottomBar = barraInferior
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (reservas.isEmpty()) {
                item { Text("Todavía no tienes reservas.", color = TextoSecundarioFit) }
            }
            items(reservas.asReversed(), key = { it.id }) { reserva ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TarjetaReserva(reserva)
                    // No se permite cancelar sesiones completadas ni cancelar dos veces.
                    if (reserva.estado == "Confirmada") {
                        OutlinedButton(
                            onClick = { reservaPendienteId = reserva.id },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFB3261E)
                            )
                        ) { Text("Cancelar reserva") }
                    }
                }
            }
        }
    }

    // Consulta el registro actual, en lugar de guardar una copia desactualizada.
    val pendiente = reservas.firstOrNull {
        it.id == reservaPendienteId && it.estado == "Confirmada"
    }
    if (pendiente != null) {
        AlertDialog(
            onDismissRequest = {
                // Atrás o tocar fuera cierra el diálogo y conserva la reserva.
                reservaPendienteId = null
            },
            title = { Text("¿Cancelar reserva?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(pendiente.clase.nombre, fontWeight = FontWeight.Bold)
                    Text("${pendiente.clase.dia}, ${pendiente.clase.hora} · ${pendiente.clase.sala}")
                    Text(if (pendiente.cupos == 1) "1 cupo" else "${pendiente.cupos} cupos")
                    Text("La reserva quedará marcada como Cancelada.")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reservaPendienteId = null
                        // El callback actualiza el estado compartido en NavegacionFit.
                        onCancelar(pendiente.id)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
                ) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(
                    onClick = { reservaPendienteId = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = VerdeFit)
                ) { Text("Conservar reserva") }
            },
            containerColor = Color.White,
            titleContentColor = TextoFit,
            textContentColor = TextoSecundarioFit
        )
    }
}
