package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.compose.foundation.shape.CircleShape
import com.jara.lavacampus.ui.components.importeSoles
import com.jara.lavacampus.viewmodel.ClientesViewModel

@Composable
fun ClientesScreen(vm: ClientesViewModel, onVolver: () -> Unit) {
    val clientes by vm.clientes.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            TextButton(onClick = onVolver) { Text("← Panel") }
            Text("Clientes", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            OutlinedTextField(busqueda, vm::cambiarBusqueda, label = { Text("Buscar nombre, código o correo") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        }
        if (cargando) item { CircularProgressIndicator() }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        if (!cargando && clientes.isEmpty() && error == null) item { Text("No se encontraron clientes.") }
        items(clientes, key = { it.id }) { cliente ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Surface(Modifier.size(48.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(cliente.nombre.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.take(2).joinToString("") { it.take(1).uppercase() }, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(Modifier.weight(1f)) {
                            Text(cliente.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(cliente.codigo, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Text(cliente.correo)
                    Text("${cliente.cantidadPedidos} pedidos · ${importeSoles(cliente.totalGastado)} pagados", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
