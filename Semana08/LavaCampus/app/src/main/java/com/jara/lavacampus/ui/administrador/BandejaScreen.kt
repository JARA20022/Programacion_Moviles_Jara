package com.jara.lavacampus.ui.administrador

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.EstadoPedidoChip
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.viewmodel.BandejaViewModel
import com.jara.lavacampus.viewmodel.FiltroBandeja

@Composable
fun BandejaScreen(vm: BandejaViewModel, onVolver: () -> Unit, onGestion: (Int) -> Unit) {
    val atrasados by vm.atrasados.collectAsStateWithLifecycle()
    val pedidos by vm.pedidos.collectAsStateWithLifecycle()
    val filtro by vm.filtro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val ocupadoId by vm.ocupadoId.collectAsStateWithLifecycle()
    BackHandler(enabled = ocupadoId != null) { /* Esperar la actualización. */ }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextButton(onClick = onVolver, enabled = ocupadoId == null) { Text("← Panel") }
                Text("OPERACIONES", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text("Bandeja de pedidos", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                OutlinedTextField(
                    value = busqueda, onValueChange = vm::cambiarBusqueda,
                    label = { Text("Buscar estudiante o número") },
                    singleLine = true, modifier = Modifier.fillMaxWidth(), shape = androidx.compose.foundation.shape.CutCornerShape(topEnd=14.dp)
                )
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FiltroBandeja.entries.forEach { opcion ->
                        FilterChip(selected = filtro == opcion, onClick = { vm.cambiarFiltro(opcion) }, label = { Text(opcion.titulo) })
                    }
                }
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        mensaje?.let { item { Text(it, color = MaterialTheme.colorScheme.primary) } }
        if (cargando) item { CircularProgressIndicator() }
        if (!cargando && pedidos.isEmpty() && error == null) item { Text("No hay pedidos con ese filtro o búsqueda.") }
        items(pedidos, key = { it.pedido.id }) { datos ->
            val pedido = datos.pedido
            val estado = EstadoPedido.valueOf(pedido.estado)
            Card(
                onClick = { if (ocupadoId == null) onGestion(pedido.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.CutCornerShape(topEnd=24.dp,bottomStart=10.dp),
                colors = CardDefaults.cardColors(containerColor = if (pedido.id in atrasados) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("#${pedido.id.toString().padStart(3, '0')}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        EstadoPedidoChip(estado)
                    }
                    if (pedido.id in atrasados) Text("ATRASADO · requiere atención", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    Text(datos.usuario.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(datos.usuario.codigo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    datos.detalles.forEach { linea ->
                        Text("${linea.servicio.nombre} · ${linea.detalle.cantidad} ${linea.servicio.unidad.lowercase()}")
                    }
                    Text("Entrega: ${fechaPedido(pedido.fechaEntrega)}")
                    Text(importeSoles(pedido.total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    if (estado == EstadoPedido.LISTO) Text("Casillero ${pedido.casillero ?: "sin asignar"} · ${if (pedido.pagado) "Cobrado" else "Pago pendiente"}")
                    val accion = when (estado) {
                        EstadoPedido.RECIBIDO -> "Iniciar"
                        EstadoPedido.EN_PROCESO -> "Marcar listo"
                        EstadoPedido.LISTO -> "Entregar"
                        else -> null
                    }
                    if (accion != null) {
                        Button(
                            onClick = { if (estado == EstadoPedido.RECIBIDO) vm.iniciar(datos) else onGestion(pedido.id) },
                            enabled = ocupadoId == null, modifier = Modifier.fillMaxWidth()
                        ) { Text(if (ocupadoId == pedido.id) "Guardando…" else accion) }
                    } else {
                        Text("Ver detalle →", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
