package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }) }) { margen ->
        Column(
            Modifier.fillMaxSize().padding(margen).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // TODO Paso 2: solicitar el permiso antes de buscar.
            Button(onClick = vm::buscar, enabled = estado != UbicacionUi.Buscando) {
                Text("Obtener mi ubicación")
            }
            Text("Latitud: ${posicion?.latitud ?: "—"}")
            Text("Longitud: ${posicion?.longitud ?: "—"}")
            when (val actual = estado) {
                UbicacionUi.Buscando -> Text("Buscando ubicación…")
                is UbicacionUi.Error -> Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
                else -> Unit
            }
        }
    }
}
