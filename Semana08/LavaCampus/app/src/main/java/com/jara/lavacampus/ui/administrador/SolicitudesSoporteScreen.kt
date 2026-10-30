package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.ui.components.fechaPedido
import com.jara.lavacampus.viewmodel.SolicitudesSoporteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudesSoporteScreen(
    vm: SolicitudesSoporteViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit
) {
    val solicitudes by vm.solicitudes.collectAsStateWithLifecycle()
    val filtroEstado by vm.filtroEstado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes de Otros Servicios") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("← Volver") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = listOf("TODOS", "PENDIENTE", "EN_ATENCION", "ATENDIDA").indexOf(filtroEstado),
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("TODOS", "PENDIENTE", "EN_ATENCION", "ATENDIDA").forEach { estado ->
                    Tab(
                        selected = filtroEstado == estado,
                        onClick = { vm.cambiarFiltro(estado) },
                        text = { Text(estado) }
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (solicitudes.isEmpty()) {
                    item {
                        Text(
                            "No hay solicitudes para mostrar.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(solicitudes, key = { it.detalleId }) { solicitud ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Pedido #${solicitud.pedidoId.toString().padStart(3, '0')}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(solicitud.estadoPedido, style = MaterialTheme.typography.labelMedium)
                            }
                            Text("Estudiante: ${solicitud.nombreEstudiante}")
                            Text("Solicita: ${solicitud.descripcionOtros}")
                            Text("Fecha: ${fechaPedido(solicitud.fechaRegistro)}", style = MaterialTheme.typography.bodySmall)
                            
                            val colorEstado = when(solicitud.estadoAtencion) {
                                "PENDIENTE" -> MaterialTheme.colorScheme.error
                                "EN_ATENCION" -> MaterialTheme.colorScheme.primary
                                "ATENDIDA" -> Color(0xFF4CAF50)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            Text("Atención: ${solicitud.estadoAtencion}", color = colorEstado, fontWeight = FontWeight.Bold)
                            
                            LavaButton(
                                texto = "Ver / Atender",
                                onClick = { onAtender(solicitud.detalleId) },
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
