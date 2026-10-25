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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.jara.lavacampus.ui.components.IconoServicio
import com.jara.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, esEdicion: Boolean, onVolver: () -> Unit) {
    val f by vm.formulario.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val guardando by vm.guardando.collectAsStateWithLifecycle()
    val guardado by vm.guardado.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val errores by vm.erroresCampos.collectAsStateWithLifecycle()
    LaunchedEffect(guardado) { if (guardado) onVolver() }
    BackHandler(enabled = guardando) { }
    val habilitado = !cargando && !guardando && !guardado
    LazyColumn(Modifier.fillMaxSize().imePadding(), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            TextButton(onClick = onVolver, enabled = !guardando) { Text("← Servicios") }
            Text(if (esEdicion) "Editar servicio" else "Nuevo servicio", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
        }
        if (cargando) item { CircularProgressIndicator() }
        item { OutlinedTextField(f.nombre, { vm.editar(f.copy(nombre = it)) }, label = { Text("Nombre del servicio") }, isError = "nombre" in errores, supportingText = { Text(errores["nombre"] ?: "${f.nombre.length}/60 · letras y separadores") }, modifier = Modifier.fillMaxWidth(), singleLine = true, enabled = habilitado) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Se cobra por", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("KG" to "Kilo", "PRENDA" to "Prenda", "UNIDAD" to "Unidad").forEach { (valor, texto) ->
                        FilterChip(selected = f.unidad == valor, onClick = { vm.editar(f.copy(unidad = valor)) }, enabled = habilitado, label = { Text(texto) })
                    }
                }
            }
        }
        item { OutlinedTextField(f.precio, { vm.editar(f.copy(precio = it)) }, label = { Text("Precio en soles") }, isError = "precio" in errores, supportingText = { Text(errores["precio"] ?: "S/ 0.01 a S/ 9999.99 · hasta 2 decimales") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), enabled = habilitado) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tiempo de entrega", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(24, 48, 72).forEach { horas ->
                        FilterChip(selected = f.horasEntrega == horas, onClick = { vm.editar(f.copy(horasEntrega = horas)) }, enabled = habilitado, label = { Text("$horas h") })
                    }
                }
            }
        }
        item { OutlinedTextField(f.descripcion, { vm.editar(f.copy(descripcion = it)) }, label = { Text("Descripción") }, isError = "descripcion" in errores, supportingText = { Text(errores["descripcion"] ?: "${f.descripcion.length}/300") }, modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 4, enabled = habilitado) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ícono", fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("wash", "iron", "shirt").forEach { icono ->
                        Card(onClick = { if (habilitado) vm.editar(f.copy(icono = icono)) }, modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = if (f.icono == icono) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)) {
                            Column(Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                IconoServicio(icono)
                                Text(if (f.icono == icono) "Elegido" else "Elegir", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Servicio activo")
                Switch(checked = f.activo, onCheckedChange = { vm.editar(f.copy(activo = it)) }, enabled = habilitado)
            }
        }
        error?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button(onClick = vm::guardarFormulario, enabled = habilitado, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(18.dp)) { Text(if (guardando) "Guardando…" else "Guardar servicio") } }
    }
}
