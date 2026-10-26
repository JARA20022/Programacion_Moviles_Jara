package com.jara.lavacampus.ui.administrador

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.Casilleros
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.EstadoPedidoChip
import com.jara.lavacampus.ui.components.StepperEstado
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.ui.components.nombreEstado
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.viewmodel.GestionPedidoViewModel

@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val ocupado by vm.ocupado.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val casillerosOcupados by vm.casillerosOcupados.collectAsStateWithLifecycle()
    val elegido by vm.casilleroElegido.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable { mutableStateOf(false) }
    val actual = datos
    val estado = actual?.pedido?.estado?.let(EstadoPedido::valueOf)
    val puedeCancelar = estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO
    LaunchedEffect(puedeCancelar) { if (!puedeCancelar) confirmar = false }
    BackHandler(enabled = ocupado) { /* Esperar a que Room guarde el cambio. */ }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item { TextButton(onClick = onVolver, enabled = !ocupado) { Text("← Bandeja") } }
        if (cargando) item { CircularProgressIndicator() }
        if (actual != null && estado != null) {
            val pedido = actual.pedido
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.CutCornerShape(topEnd=30.dp,bottomStart=10.dp),
                    colors = CardDefaults.cardColors(containerColor = TintaCampus, contentColor = Color.White)
                ) {
                    Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("GESTIÓN DEL PEDIDO", color = Color(0xFFD6F58A), style = MaterialTheme.typography.labelLarge)
                        Text("#${pedido.id.toString().padStart(3, '0')}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
                        EstadoPedidoChip(estado)
                        Text(actual.usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("${actual.usuario.codigo} · ${actual.usuario.correo}")
                        Text("Registrado: ${fechaPedido(pedido.fechaRegistro)}")
                        Text("Entrega estimada: ${fechaPedido(pedido.fechaEntrega)}")
                    }
                }
            }
            item { StepperEstado(estado, vertical = true) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Servicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    actual.detalles.forEach { linea ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(linea.servicio.nombre, fontWeight = FontWeight.Bold)
                            if(linea.detalle.descuento>0.0) Text("Descuento aplicado: ${importeSoles(linea.detalle.descuento)}")
                            Text("${linea.detalle.cantidad} ${linea.servicio.unidad.lowercase()} × ${importeSoles(linea.detalle.precioUnitario)} = ${importeSoles(linea.detalle.subtotal)}")
                        }
                    }
                    if (pedido.nota.isNotBlank()) Text("Nota: ${pedido.nota}")
                }
            }
            if (estado == EstadoPedido.EN_PROCESO || estado == EstadoPedido.LISTO) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Casillero de recojo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        if (estado == EstadoPedido.LISTO) {
                            Text("Asignado: ${pedido.casillero ?: "sin asignar"}")
                        } else {
                            Text("Elegido: ${elegido ?: "ninguno"}. Los ocupados aparecen en gris.")
                            Casilleros.todos.chunked(5).forEach { fila ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    fila.forEach { c ->
                                        FilterChip(
                                            selected = elegido == c,
                                            onClick = { vm.elegirCasillero(c) },
                                            enabled = !ocupado && c !in casillerosOcupados,
                                            label = { Text(c, fontSize = 10.sp) },
                                            modifier = Modifier.weight(1f),
                                            colors = FilterChipDefaults.filterChipColors(disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        if(pedido.descuento>0.0) Text("Subtotal: ${importeSoles(pedido.subtotal)} · Ahorro: ${importeSoles(pedido.descuento)}")
                        Text("Total: ${importeSoles(pedido.total)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Cobrado", style = MaterialTheme.typography.titleMedium)
                            Switch(checked = pedido.pagado, onCheckedChange = vm::marcarPagado, enabled = !ocupado && estado.siguiente() != null)
                        }
                        Text(if (pedido.pagado) "Pago registrado" else "Debes registrar el cobro antes de entregar.")
                    }
                }
            }
            item {
                Text(
                    "Solo se avanza una etapa. Para marcar Listo, elige un casillero libre. Para entregar, activa Cobrado. Solo se cancela antes de estar Listo.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) } }
        mensaje?.let { item { Text(it, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold) } }
        if (estado != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    estado.siguiente()?.let { siguiente ->
                        Button(onClick = vm::avanzar, enabled = !ocupado, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                            Text(if (ocupado) "Guardando…" else "Marcar como ${nombreEstado(siguiente)}")
                        }
                    }
                    OutlinedButton(onClick = { confirmar = true }, enabled = puedeCancelar && !ocupado, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancelar pedido")
                    }
                    if (estado.siguiente() == null) Text("Pedido finalizado. No admite cambios.")
                }
            }
        }
    }
    if (confirmar && puedeCancelar && !ocupado) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Cancelar este pedido?") },
            text = { Text("Pasará a CANCELADO y no podrá reactivarse.") },
            confirmButton = { TextButton(onClick = { confirmar = false; vm.cancelar() }) { Text("Sí, cancelar") } },
            dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Conservar pedido") } }
        )
    }
}
