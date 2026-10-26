package com.jara.lavacampus.ui.estudiante

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.EstadoPedidoChip
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.ui.components.PedidoCard
import com.jara.lavacampus.ui.components.StepperEstado
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.ui.theme.TerracotaCampus
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onDetalle: (Int) -> Unit,
    onSalir: () -> Unit,
    onPerfil: () -> Unit,
    recibirPedidos: Boolean,
    cerrandoSesion: Boolean = false,
    errorSesion: String? = null
) {
    val activo by vm.pedidoActivo.collectAsStateWithLifecycle()
    val recientes by vm.recientes.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("LC / TU CAMPUS", style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = onSalir, enabled = !cerrandoSesion) { Text("Cerrar sesión") }
            }
            Text(
                "Hola, ${vm.estudiante.nombre}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Código ${vm.estudiante.codigo}",
                modifier = Modifier.padding(top = 6.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        errorSesion?.let { mensaje ->
            item { Text(mensaje, color = MaterialTheme.colorScheme.error) }
        }

        item {
            val pedido = activo
            when {
                cargando -> CircularProgressIndicator()
                error != null -> Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
                pedido != null -> TarjetaActiva(pedido) { onDetalle(pedido.pedido.id) }
                else -> Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("No tienes pedidos activos", fontWeight = FontWeight.Bold)
                        Text("Tu próximo servicio empieza con un nuevo pedido.")
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!recibirPedidos) Text("La lavandería no está recibiendo pedidos", color = MaterialTheme.colorScheme.error)
                androidx.compose.material3.Button(
                    onClick = onNuevoPedido, enabled = recibirPedidos, modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(18.dp), shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = TerracotaCampus)
                ) { Text("Nuevo pedido") }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onPerfil, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Mi perfil")
                        Text("Ajustes y cuenta →")
                    }
                }
                OutlinedButton(onClick = onMisPedidos, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Mis pedidos")
                        Text("Ver historial →")
                    }
                }
                OutlinedButton(onClick = onTarifas, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Tarifas")
                        Text("Ver servicios →")
                    }
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Últimos pedidos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = onMisPedidos) { Text("Ver todos") }
            }
        }
        if (!cargando && error == null) {
            if (recientes.isEmpty()) {
                item { Text("Todavía no has registrado pedidos.") }
            }
            items(recientes, key = { it.pedido.id }) { pedido ->
                PedidoCard(pedido, onClick = { onDetalle(pedido.pedido.id) })
            }
        }
    }
}

@Composable
private fun TarjetaActiva(pedido: PedidoConDetalle, onDetalle: () -> Unit) {
    val datos = pedido.pedido
    val estado = EstadoPedido.valueOf(datos.estado)
    Card(
        onClick = onDetalle,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 8.dp),
        colors = CardDefaults.cardColors(containerColor = TintaCampus, contentColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("TU PEDIDO EN SEGUIMIENTO", fontSize = 10.sp, letterSpacing = 1.sp, color = Color(0xFFFFC5AD))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("#${datos.id.toString().padStart(3, '0')}", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
                EstadoPedidoChip(estado)
            }
            Text("Entrega estimada: ${fechaPedido(datos.fechaEntrega)}")
            StepperEstado(estado, sobreOscuro = true)
            if (estado == EstadoPedido.LISTO) {
                Text(
                    text = datos.casillero?.let { "Recógelo en el casillero $it" }
                        ?: "Tu pedido está listo. Consulta tu casillero en lavandería.",
                    color = Color(0xFFFFC5AD),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(importeSoles(datos.total), fontWeight = FontWeight.Bold)
                Text("Ver detalle →", color = Color(0xFFFFC5AD))
            }
        }
    }
}
