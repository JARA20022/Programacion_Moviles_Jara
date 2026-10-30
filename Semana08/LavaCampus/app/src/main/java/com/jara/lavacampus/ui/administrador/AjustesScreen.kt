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

import com.jara.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(vm: SesionViewModel, onVolver: () -> Unit, onSoportes: () -> Unit = {}) {
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val avisos by vm.notificaciones.collectAsStateWithLifecycle()
    val recibir by vm.recibirPedidos.collectAsStateWithLifecycle()
    val guardando by vm.guardandoPreferencias.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val habilitado = !guardando && !cargando
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            TextButton(onClick = onVolver, enabled = habilitado) { Text("← Panel") }
            Text("Ajustes", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
        }
        item {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(usuario?.nombre.orEmpty(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(usuario?.correo.orEmpty())
                    Text("Administrador")
                }
            }
        }
        item { PreferenciaAdministrador("Modo oscuro", oscuro, habilitado, vm::cambiarModoOscuro) }
        item { PreferenciaAdministrador("Notificaciones", avisos, habilitado, vm::cambiarNotificaciones) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PreferenciaAdministrador("Recibir pedidos", recibir, habilitado, vm::cambiarRecibirPedidos)
                Text(if (recibir) "La lavandería acepta nuevos pedidos." else "Los estudiantes no pueden crear nuevos pedidos.", style = MaterialTheme.typography.bodySmall)
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSoportes, enabled = habilitado, modifier = Modifier.fillMaxWidth()) { Text("Usuarios de soporte") }
                Button(onClick = vm::cambiarPerfil, enabled = habilitado, modifier = Modifier.fillMaxWidth()) { Text("Cambiar de perfil") }
                OutlinedButton(onClick = vm::cerrarSesion, enabled = habilitado, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
            }
        }
    }
}

@Composable
private fun PreferenciaAdministrador(texto: String, valor: Boolean, habilitado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(texto, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        Switch(checked = valor, onCheckedChange = onCambio, enabled = habilitado)
    }
}
