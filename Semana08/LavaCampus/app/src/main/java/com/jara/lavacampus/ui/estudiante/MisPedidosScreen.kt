package com.jara.lavacampus.ui.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.PedidoCard
import com.jara.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onVolver: () -> Unit,
    onNuevoPedido: () -> Unit,
    onDetalle: (Int) -> Unit,
    onTarifas: () -> Unit,
    onRepetir: (Int) -> Unit
) {
    val activos by vm.activos.collectAsStateWithLifecycle()
    val historial by vm.historial.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val cancelandoId by vm.cancelandoId.collectAsStateWithLifecycle()
    var pestaña by rememberSaveable { mutableStateOf(0) }
    var confirmarId by rememberSaveable { mutableStateOf<Int?>(null) }
    val objetivo = activos.firstOrNull { it.pedido.id == confirmarId && it.pedido.estado == EstadoPedido.RECIBIDO.name }
    LaunchedEffect(objetivo?.pedido?.id) { if (objetivo == null) confirmarId = null }
    BackHandler(enabled = cancelandoId != null) { /* Esperar a que termine la cancelación. */ }
    val lista = if (pestaña == 0) activos else historial

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { if (cancelandoId == null) onNuevoPedido() }) {
                Text("+  Nuevo pedido", fontWeight = FontWeight.Bold)
            }
        },
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onVolver, enabled = cancelandoId == null) { Text("← Inicio") }
                    TextButton(onClick = onTarifas, enabled = cancelandoId == null) { Text("Ver tarifas") }
                }
                Text("Tus pedidos", Modifier.padding(horizontal = 22.dp, vertical = 12.dp), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                Row(Modifier.fillMaxWidth().padding(horizontal=22.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                    FilterChip(selected=pestaña==0,onClick={pestaña=0},label={Text("Activos (${activos.size})")},modifier=Modifier.weight(1f))
                    FilterChip(selected=pestaña==1,onClick={pestaña=1},label={Text("Historial (${historial.size})")},modifier=Modifier.weight(1f))
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding),
            contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 108.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
            mensaje?.let { item { Text(it, color = MaterialTheme.colorScheme.primary) } }
            if (cargando) {
                item { CircularProgressIndicator() }
            } else if (lista.isEmpty() && error == null) {
                item {
                    Text(
                        if (pestaña == 0) "No tienes pedidos activos. Crea uno cuando lo necesites."
                        else "Tus pedidos entregados y cancelados aparecerán aquí.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(lista, key = { it.pedido.id }) { pedido ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    PedidoCard(pedido = pedido, onClick = { if (cancelandoId == null) onDetalle(pedido.pedido.id) })
                    if(pedido.pedido.estado in listOf("ENTREGADO","CANCELADO")) {
                        TextButton(onClick={onRepetir(pedido.pedido.id)},enabled=cancelandoId==null){Text("Pedir de nuevo →")}
                    }
                    if (pedido.pedido.estado == EstadoPedido.RECIBIDO.name) {
                        TextButton(onClick = { confirmarId = pedido.pedido.id }, enabled = cancelandoId == null) {
                            Text(if (cancelandoId == pedido.pedido.id) "Cancelando…" else "Cancelar pedido")
                        }
                    }
                }
            }
        }
    }
    if (objetivo != null && cancelandoId == null) {
        AlertDialog(
            onDismissRequest = { confirmarId = null },
            title = { Text("¿Cancelar el pedido #${objetivo.pedido.id}?") },
            text = { Text("Pasará a Historial como CANCELADO. Esta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = { confirmarId = null; vm.cancelar(objetivo) }) { Text("Sí, cancelar") } },
            dismissButton = { TextButton(onClick = { confirmarId = null }) { Text("Conservar pedido") } }
        )
    }
}
