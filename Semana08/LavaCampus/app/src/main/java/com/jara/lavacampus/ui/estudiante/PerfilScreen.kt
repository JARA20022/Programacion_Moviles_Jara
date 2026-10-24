package com.jara.lavacampus.ui.estudiante

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

import com.jara.lavacampus.viewmodel.InicioViewModel
import com.jara.lavacampus.viewmodel.SesionViewModel
import java.util.Locale

@Composable
fun PerfilScreen(sesion: SesionViewModel, inicio: InicioViewModel, onVolver: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsStateWithLifecycle()
    val resumen by inicio.resumenPerfil.collectAsStateWithLifecycle()
    val cargandoDatos by inicio.cargando.collectAsStateWithLifecycle()
    val errorDatos by inicio.error.collectAsStateWithLifecycle()
    val oscuro by sesion.modoOscuro.collectAsStateWithLifecycle()
    val avisos by sesion.notificaciones.collectAsStateWithLifecycle()
    val guardando by sesion.guardandoPreferencias.collectAsStateWithLifecycle()
    val cargando by sesion.cargando.collectAsStateWithLifecycle()
    val error by sesion.error.collectAsStateWithLifecycle()
    val habilitado = !guardando && !cargando
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            TextButton(onClick = onVolver, enabled = habilitado) { Text("← Inicio") }
            Text("Tu perfil", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
        }
        item {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(usuario?.nombre.orEmpty(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(usuario?.codigo.orEmpty())
                    Text(usuario?.correo.orEmpty())
                }
            }
        }
        if (cargandoDatos) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        errorDatos?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                EstadisticaPerfil("Pedidos activos", resumen.activos.toString())
                EstadisticaPerfil("Pedidos totales", resumen.totales.toString())
                EstadisticaPerfil("Kilos lavados", String.format(Locale("es", "PE"), "%.1f kg", resumen.kilosLavados))
                Text("Los kilos corresponden a pedidos entregados.", style = MaterialTheme.typography.bodySmall)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Modo oscuro")
                Switch(oscuro, sesion::cambiarModoOscuro, enabled = habilitado)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Avisos")
                Switch(avisos, sesion::cambiarNotificaciones, enabled = habilitado)
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = sesion::cambiarPerfil, enabled = habilitado, modifier = Modifier.fillMaxWidth()) { Text("Cambiar de perfil") }
                OutlinedButton(onClick = sesion::cerrarSesion, enabled = habilitado, modifier = Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
            }
        }
    }
}

@Composable
private fun EstadisticaPerfil(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta)
        Text(valor, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
