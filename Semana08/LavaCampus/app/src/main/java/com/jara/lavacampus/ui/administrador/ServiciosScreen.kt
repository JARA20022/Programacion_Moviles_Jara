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

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.saveable.rememberSaveable
import com.jara.lavacampus.model.textoPrecio
import com.jara.lavacampus.ui.components.IconoServicio
import com.jara.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onVolver: () -> Unit, onNuevo: () -> Unit, onEditar: (Int) -> Unit) {
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val guardando by vm.guardando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    var eliminarId by rememberSaveable { mutableStateOf<Int?>(null) }
    val objetivo = servicios.firstOrNull { it.id == eliminarId }
    BackHandler(enabled = guardando) { }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            TextButton(onClick = onVolver, enabled = !guardando) { Text("← Panel") }
            Text("Servicios", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text("Administra el catálogo y sus tarifas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onNuevo, enabled = !guardando, modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) { Text("+ Crear servicio") }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        mensaje?.let { item { Text(it, color = MaterialTheme.colorScheme.primary) } }
        if (cargando) item { CircularProgressIndicator() }
        if (!cargando && servicios.isEmpty() && error == null) item { Text("Todavía no hay servicios. Crea el primero.") }
        items(servicios, key = { it.id }) { s ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        IconoServicio(s.icono)
                        Text(s.nombre, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Text(s.textoPrecio(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("Entrega en ${s.horasEntrega} h")
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (s.activo) "Servicio activo" else "Servicio inactivo")
                        Switch(checked = s.activo, onCheckedChange = { vm.cambiarActivo(s, it) }, enabled = !guardando)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { onEditar(s.id) }, enabled = !guardando) { Text("Editar") }
                        TextButton(onClick = { eliminarId = s.id }, enabled = !guardando) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                    }
                }
            }
        }
    }
    if (objetivo != null && !guardando) {
        AlertDialog(onDismissRequest = { eliminarId = null }, title = { Text("¿Eliminar ${objetivo.nombre}?") },
            text = { Text("Solo se puede eliminar si no aparece en ningún pedido.") },
            confirmButton = { TextButton(onClick = { eliminarId = null; vm.eliminar(objetivo) }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { eliminarId = null }) { Text("Conservar") } })
    }
}
