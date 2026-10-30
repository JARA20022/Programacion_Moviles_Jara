package com.jara.lavacampus.ui.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.AppTopBar
import com.jara.lavacampus.ui.components.EstadoPedidoChip
import com.jara.lavacampus.ui.components.StepperEstado
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.viewmodel.DetallePedidoViewModel
import java.util.Locale

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val cancelando by vm.cancelando.collectAsStateWithLifecycle()
    val puedeCancelar by vm.puedeCancelar.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(puedeCancelar) { if (!puedeCancelar) confirmar = false }
    BackHandler(enabled = cancelando) { /* Esperar a que termine la operación. */ }
    val detalle = datos

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            AppTopBar("Detalle del pedido",onVolver,habilitado=!cancelando)
        }
        if (cargando) {
            item { CircularProgressIndicator() }
        }
        error?.let { texto ->
            item { Text(texto, color = MaterialTheme.colorScheme.error) }
        }
        mensaje?.let { texto ->
            item { Text(texto, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
        }
        if (detalle != null) {
            val pedido = detalle.pedido
            val estado = EstadoPedido.valueOf(pedido.estado)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = TintaCampus, contentColor = Color.White)
                ) {
                    Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("EL RECORRIDO DE TU PEDIDO", style = MaterialTheme.typography.labelLarge, color = Color(0xFFFFC5AD))
                        Text("#${pedido.id.toString().padStart(3, '0')}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
                        EstadoPedidoChip(estado)
                        Text("Registrado: ${fechaPedido(pedido.fechaRegistro)}")
                        Text("Entrega estimada: ${fechaPedido(pedido.fechaEntrega)}")
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Seguimiento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        StepperEstado(estado = estado, vertical = true)
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Casillero de recojo", fontWeight = FontWeight.Bold)
                        Text(
                            if (estado == EstadoPedido.LISTO) pedido.casillero?.let { "Recógelo en el casillero $it" }
                                ?: "Consulta tu casillero en lavandería"
                            else "Se asignará al estar listo"
                        )
                    }
                }
            }
            item { Text("Servicios del pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            items(detalle.detalles, key = { it.detalle.id }) { linea ->
                val d = linea.detalle
                val unidad = when (linea.servicio.unidad) { "KG" -> "kg"; "PRENDA" -> "prendas"; else -> "unidades" }
                val cantidad = if (d.cantidad % 1.0 == 0.0) d.cantidad.toInt().toString() else String.format(Locale.US, "%.1f", d.cantidad)
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(linea.servicio.nombre, fontWeight = FontWeight.Bold)
                    Text("$cantidad $unidad × ${importeSoles(d.precioUnitario)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    // Usar los importes históricos, nunca el precio actual del servicio.
                    Text(importeSoles(d.subtotal), style = MaterialTheme.typography.titleMedium)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.headlineSmall)
                    Text(importeSoles(pedido.total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        if (pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                        modifier = Modifier.padding(20.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            if (pedido.nota.isNotBlank()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Tu nota", fontWeight = FontWeight.Bold)
                        Text(pedido.nota)
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = { confirmar = true },
                        enabled = puedeCancelar,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) { Text(if (cancelando) "Cancelando…" else "Cancelar pedido") }
                    if (estado != EstadoPedido.RECIBIDO) {
                        Text("Solo se cancela antes de empezar el lavado", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
    if (confirmar && puedeCancelar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Cancelar este pedido?") },
            text = { Text("El pedido pasará a CANCELADO. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { confirmar = false; vm.cancelar() }) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Conservar pedido") }
            }
        )
    }
}
