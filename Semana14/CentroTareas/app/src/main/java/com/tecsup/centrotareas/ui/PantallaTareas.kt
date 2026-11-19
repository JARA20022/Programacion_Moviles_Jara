package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF15589A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
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
                Text(mensaje)
                // TODO Pasos 1 a 4: incorporar los controles de la tarea con progreso.
            }
            TarjetaTarea("Contar números primos") {
                // TODO Paso 5: incorporar la entrada, los botones y el estado de primos.
            }
            TarjetaTarea("Dos cargas") {
                // TODO Pasos 6 y 7: incorporar las cargas simuladas y el reintento.
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
