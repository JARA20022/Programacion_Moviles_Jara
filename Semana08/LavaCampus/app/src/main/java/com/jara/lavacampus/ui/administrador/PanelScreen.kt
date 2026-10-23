package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.ui.components.graficos.GraficoBarras
import com.jara.lavacampus.ui.components.graficos.GraficoDona
import com.jara.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onSalir: () -> Unit,
    onBandeja: () -> Unit,
    onVerListos: () -> Unit,
    cerrandoSesion: Boolean = false,
    errorSesion: String? = null
) {
    val hoy by vm.pedidosHoy.collectAsStateWithLifecycle()
    val proceso by vm.enProceso.collectAsStateWithLifecycle()
    val recoger by vm.porRecoger.collectAsStateWithLifecycle()
    val ingresos by vm.ingresosSemana.collectAsStateWithLifecycle()
    val estados by vm.porEstado.collectAsStateWithLifecycle()
    val servicios by vm.porServicio.collectAsStateWithLifecycle()
    val listos by vm.listos.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(22.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("LAVACAMPUS / ADMIN", style = MaterialTheme.typography.labelMedium)
                    TextButton(onClick = onSalir, enabled = !cerrandoSesion) {
                        Text(if (cerrandoSesion) "Saliendo…" else "Cerrar sesión")
                    }
                }
                Text("Control del día", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                Text(fechaPedido(vm.hoy), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            androidx.compose.material3.Button(onClick = onBandeja, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                Text("Abrir bandeja de pedidos")
            }
        }
        if (cargando) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        errorSesion?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ContadorPanel("Pedidos\nde hoy", hoy, Color(0xFFAB4028), Modifier.weight(1f))
                ContadorPanel("En\nproceso", proceso, Color(0xFF334BA5), Modifier.weight(1f))
                ContadorPanel("Por\nrecoger", recoger, Color(0xFF176447), Modifier.weight(1f))
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF514284), contentColor = Color.White)
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("INGRESOS DE LA SEMANA", style = MaterialTheme.typography.labelLarge)
                    Text(importeSoles(ingresos), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
                    Text("Desde el lunes ${fechaPedido(vm.inicioSemana)}")
                    Text("Total de pedidos pagados", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (recoger > 0 || listos.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD9F2DF), contentColor = Color(0xFF164C31)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("$recoger ${if (recoger == 1) "pedido listo" else "pedidos listos"} para recoger", fontWeight = FontWeight.Bold)
                        Text("Consulta quiénes tienen ropa esperando en los casilleros.")
                        TextButton(onClick = onVerListos) { Text("Ver", color = Color(0xFF164C31)) }
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Text("Pedidos por estado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    GraficoDona(estados)
                    Text("Recibidos, en proceso, listos y entregados. No incluye cancelados.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Text("Ingresos por servicio", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Importes cobrados de esta semana", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    GraficoBarras(servicios)
                }
            }
        }
    }
}

@Composable
private fun ContadorPanel(titulo: String, valor: Int, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color, contentColor = Color.White), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo, fontSize = 11.sp, lineHeight = 15.sp)
            Text(valor.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        }
    }
}
