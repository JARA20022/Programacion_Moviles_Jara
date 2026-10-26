package com.jara.lavacampus.ui.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.ui.components.SelectorCantidad
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.viewmodel.NuevoPedidoViewModel
import java.util.Locale

@Composable
fun NuevoPedidoScreen(vm: NuevoPedidoViewModel, onVolver: () -> Unit, onCreado: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    LaunchedEffect(estado.pedidoCreado) {
        if (estado.pedidoCreado != null) onCreado()
    }
    BackHandler(enabled = estado.guardando) { /* Esperar la transacción antes de salir. */ }
    LazyColumn(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onVolver, enabled = !estado.guardando) { Text("← Volver") }
                Text("NUEVO PEDIDO", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text("Menos pendientes.\nMás tiempo para ti.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                Text("Elige el servicio y ajusta la cantidad. El resumen se actualiza al instante.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (!estado.recibirPedidos) {
            item { Text("La lavandería no está recibiendo pedidos", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
        }
        if (estado.cargando) {
            item { CircularProgressIndicator() }
        } else if (estado.servicios.isEmpty()) {
            item { Text("No hay servicios disponibles en este momento.") }
        }
        items(estado.servicios, key = { it.id }) { servicio ->
            val esKg = servicio.unidad == "KG"
            val cantidad = estado.cantidades[servicio.id] ?: 0.0
            val unidad = when (servicio.unidad) { "KG" -> "kg"; "PRENDA" -> "prenda"; else -> "unidad" }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 8.dp, bottomEnd = 24.dp, bottomStart = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(servicio.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(servicio.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${importeSoles(servicio.precio)} / $unidad · ${servicio.horasEntrega} h", fontWeight = FontWeight.SemiBold)
                    Text(if (esKg) "De 1 a 10 kg · incrementos de 0.5" else "De 1 a 30 · incrementos de 1", style = MaterialTheme.typography.bodySmall)
                    SelectorCantidad(
                        cantidad = cantidad,
                        paso = if (esKg) 0.5 else 1.0,
                        minimo = 1.0,
                        maximo = if (esKg) 10.0 else 30.0,
                        onCambio = { delta -> vm.cambiarCantidad(servicio.id, delta) },
                        habilitado = !estado.guardando && estado.pedidoCreado == null,
                        servicio = servicio.nombre
                    )
                    if (cantidad > 0.0) Text("Subtotal: ${importeSoles(Validaciones.subtotal(cantidad, servicio.precio))}", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            OutlinedTextField(
                value = estado.nota,
                onValueChange = vm::cambiarNota,
                label = { Text("Nota para la lavandería (opcional)") },
                supportingText = { Text("${estado.nota.length}/300") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !estado.guardando && estado.pedidoCreado == null,
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(16.dp)
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = TintaCampus, contentColor = Color.White)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("TU RESUMEN", style = MaterialTheme.typography.labelLarge, color = Color(0xFFFFC5AD))
                    if (estado.cantidades.isEmpty()) Text("Agrega un servicio para empezar.")
                    estado.servicios.filter { (estado.cantidades[it.id] ?: 0.0) > 0.0 }.forEach { servicio ->
                        val cantidad = estado.cantidades.getValue(servicio.id)
                        val numero = if (servicio.unidad == "KG") String.format(Locale.US, "%.1f kg", cantidad) else "${cantidad.toInt()} ${if (servicio.unidad == "PRENDA") "prendas" else "unidades"}"
                        Text("${servicio.nombre} · $numero\n${importeSoles(Validaciones.subtotal(cantidad, servicio.precio))}")
                    }
                    Text("Subtotal: ${importeSoles(estado.subtotal)}")
                    if(estado.descuento>0.0) Text("Ahorro por volumen: −${importeSoles(estado.descuento)}",color=Color(0xFFB8F397),fontWeight=FontWeight.Bold)
                    Text("Más de 5 kg: 10 % de descuento solo en lavado por kilo.",style=MaterialTheme.typography.bodySmall)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleLarge)
                        Text(importeSoles(estado.total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Text(estado.fechaEntrega?.let { "Entrega estimada: ${fechaPedido(it)}" } ?: "Entrega estimada: selecciona un servicio")
                    Text("Los domingos no hay atención.", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFC5AD))
                }
            }
        }
        estado.error?.let { mensaje ->
            item { Text(mensaje, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }
        }
        item {
            Button(
                onClick = vm::confirmar,
                enabled = estado.puedeConfirmar,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text(if (estado.guardando) "Guardando pedido…" else "Confirmar pedido") }
        }
    }
}
