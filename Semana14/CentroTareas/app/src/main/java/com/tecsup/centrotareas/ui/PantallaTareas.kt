package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

private const val LIMITE_INICIAL = 500000

private val Azul = Color(0xFF15589A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val carga by vm.carga.collectAsState()
    val primos by vm.primos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    var toques by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("CentroTareas") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Azul, titleContentColor = Color.White)
        )
    }) { margen ->
        Column(
            Modifier.fillMaxSize().padding(margen).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaTarea("Tarea con progreso") {
                Button(onClick = { toques++ }) {
                    Text("Tócame: $toques")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = vm::tareaLarga, enabled = !corriendo) {
                        Text("Iniciar")
                    }
                    OutlinedButton(onClick = vm::cancelar, enabled = corriendo) {
                        Text("Cancelar")
                    }
                }
                Text(mensaje)
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
            }
            TarjetaTarea("Contar números primos") {
                ControlesPrimos(primos, vm::contar, vm::cancelarPrimos)
            }
            TarjetaTarea("Dos cargas") {
                ControlesCarga(carga, vm::cargarUnaPorUna, vm::cargarALaVez)
            }
        }
    }
}

@Composable
private fun TarjetaTarea(titulo: String, contenido: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, color = Azul, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}

@Composable
private fun ControlesPrimos(estado: PrimosUi, contar: (Int) -> Unit, cancelar: () -> Unit) {
    var texto by rememberSaveable { mutableStateOf(LIMITE_INICIAL.toString()) }
    val limite = texto.toIntOrNull()
    val calculando = estado == PrimosUi.Calculando

    OutlinedTextField(
        value = texto,
        onValueChange = { nuevo ->
            if (nuevo.all { it in '0'..'9' }) texto = nuevo
        },
        label = { Text("Hasta N") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        enabled = !calculando,
        modifier = Modifier.fillMaxWidth()
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { limite?.let(contar) }, enabled = !calculando && limite != null && limite >= 2) {
            Text("Contar")
        }
        OutlinedButton(onClick = cancelar, enabled = calculando) { Text("Cancelar") }
    }
    when (estado) {
        PrimosUi.Inicial -> Unit
        PrimosUi.Calculando -> Text("Calculando...")
        PrimosUi.Cancelado -> Text("Cancelado")
        is PrimosUi.Listo -> Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFE0F3E4),
            contentColor = Color(0xFF174628)
        ) {
            Text("Primos: ${estado.cantidad} · Tiempo: ${estado.milisegundos} ms",
                modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
private fun ControlesCarga(estado: CargaUi, unaPorUna: () -> Unit, aLaVez: () -> Unit) {
    val disponible = estado != CargaUi.Cargando
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = unaPorUna, enabled = disponible) { Text("Una por una") }
        Button(onClick = aLaVez, enabled = disponible) { Text("A la vez") }
    }
    when (estado) {
        CargaUi.Inicial -> Unit
        CargaUi.Cargando -> CircularProgressIndicator()
        is CargaUi.Exito -> Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFE0F3E4),
            contentColor = Color(0xFF174628)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(estado.modo, style = MaterialTheme.typography.titleMedium)
                Text("Perfil: ${estado.perfil}")
                Text("Pedidos: ${estado.pedidos}")
                Text("Tiempo: ${estado.milisegundos} ms")
            }
        }
        is CargaUi.Error -> Unit // TODO Paso 7: mostrar el error y permitir reintentar.
    }
}
