package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.viewmodel.SolicitudesSoporteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RespuestaSolicitudScreen(
    vm: SolicitudesSoporteViewModel,
    detalleId: Int,
    onVolver: () -> Unit
) {
    val solicitudes by vm.solicitudes.collectAsStateWithLifecycle()
    val solicitud = solicitudes.firstOrNull { it.detalleId == detalleId }
    
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    
    // Si no encuentra la solicitud (por ejemplo, si se cerró sesión o no cargó aún)
    if (solicitud == null) {
        Scaffold(topBar = { TopAppBar(title = { Text("Atender Solicitud") }, navigationIcon = { TextButton(onClick = onVolver) { Text("← Volver") } }) }) {
            Box(Modifier.fillMaxSize().padding(it)) { Text("Solicitud no encontrada.") }
        }
        return
    }

    var respuesta by remember { mutableStateOf("") }
    var estadoSeleccionado by remember { mutableStateOf(solicitud.estadoAtencion) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atender Solicitud") },
                navigationIcon = {
                    TextButton(onClick = onVolver, enabled = !cargando) { Text("← Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Pedido #${solicitud.pedidoId.toString().padStart(3, '0')}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("Estudiante: ${solicitud.nombreEstudiante}")
                    Text("Solicita: ${solicitud.descripcionOtros}")
                    Text("Estado del pedido: ${solicitud.estadoPedido}")
                }
            }

            Text("Estado de atención:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("PENDIENTE", "EN_ATENCION", "ATENDIDA").forEach { est ->
                    FilterChip(
                        selected = estadoSeleccionado == est,
                        onClick = { estadoSeleccionado = est; vm.limpiarError() },
                        label = { Text(est) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Text("Respuesta del soporte:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = respuesta,
                onValueChange = { respuesta = it; vm.limpiarError() },
                label = { Text("Mensaje para el estudiante") },
                placeholder = { Text("Escribe una respuesta de 5 a 500 caracteres") },
                supportingText = { Text("${respuesta.length}/500 - Obligatorio para marcar ATENDIDA") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                enabled = !cargando
            )
            
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            LavaButton(
                texto = "Guardar",
                onClick = { vm.responder(detalleId, estadoSeleccionado, respuesta, onExito = onVolver) },
                color = MaterialTheme.colorScheme.primary,
                cargando = cargando,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
