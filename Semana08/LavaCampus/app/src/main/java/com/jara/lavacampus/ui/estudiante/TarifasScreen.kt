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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.textoPrecio
import com.jara.lavacampus.ui.components.IconoServicio
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun TarifasScreen(vm: MisPedidosViewModel, onVolver: () -> Unit) {
    val servicios by vm.serviciosActivos.collectAsStateWithLifecycle()
    val cargando by vm.cargandoServicios.collectAsStateWithLifecycle()
    val error by vm.errorServicios.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextButton(onClick = onVolver) { Text("← Volver") }
                Text("SERVICIOS Y TARIFAS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text("Cuida tu ropa.\nOrganiza tu tiempo.", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                Text("Precios vigentes y tiempos de entrega para tu próximo pedido.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        if (cargando) {
            item { CircularProgressIndicator() }
        } else if (servicios.isEmpty() && error == null) {
            item { Text("No hay servicios activos en este momento.") }
        }
        items(servicios, key = { it.id }) { servicio ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.CutCornerShape(topEnd=24.dp,bottomStart=10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(servicio.icono)
                        Text(servicio.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Text(servicio.textoPrecio(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Text(servicio.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Entrega en ${servicio.horasEntrega} h", fontWeight = FontWeight.SemiBold)
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TintaCampus, contentColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("PLANIFICA TU VISITA", style = MaterialTheme.typography.labelLarge, color = Color(0xFFD6F58A))
                    Text("Horario de atención", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Lunes a sábado · 8:00 a 18:00")
                    Text("Recojo en casilleros del pabellón A.")
                    Text("Mínimo 1 kg por servicio de lavado.\nMáximo 3 pedidos activos por estudiante.")
                    Text("Si la entrega cae domingo, pasa al lunes.", color = Color(0xFFD6F58A))
                }
            }
        }
    }
}
