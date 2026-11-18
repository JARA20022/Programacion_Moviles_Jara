package com.tecsup.campusgps.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val sinServicios = (estado as? UbicacionUi.Error)?.tipo == TipoError.SIN_PLAY_SERVICES
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val cercano = masCercano
    val metrosCercano = lugares.firstOrNull { it.lugar.id == cercano?.id }?.metros
    var solicitudDeCamara by remember { mutableIntStateOf(0) }

    fun enfocarLugar(id: Int) {
        vm.seleccionar(id)
        solicitudDeCamara++
    }

    val contexto = LocalContext.current
    val permisos = remember {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { concedido -> concedido }) vm.buscar()
        else vm.permisoDenegado()
    }

    fun alPulsar() {
        val tienePermiso = permisos.any { permiso ->
            ContextCompat.checkSelfPermission(contexto, permiso) == PackageManager.PERMISSION_GRANTED
        }
        if (tienePermiso) vm.buscar() else lanzador.launch(permisos)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }) }) { margen ->
        Column(
            Modifier.fillMaxSize().padding(margen).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { alPulsar() }, enabled = !sinServicios && estado != UbicacionUi.Buscando) {
                Text("Obtener mi ubicación")
            }
            Text("Latitud: ${posicion?.latitud ?: "—"}")
            Text("Longitud: ${posicion?.longitud ?: "—"}")
            when (val actual = estado) {
                UbicacionUi.Buscando -> Text("Buscando ubicación…")
                is UbicacionUi.Error -> MensajeError(
                    error = actual,
                    abrirAjustes = {
                        val uri = Uri.fromParts("package", contexto.packageName, null)
                        contexto.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
                    },
                    reintentar = { alPulsar() }
                )
                else -> Unit
            }
            OutlinedButton(
                enabled = cercano != null,
                onClick = { cercano?.let { enfocarLugar(it.id) } }
            ) {
                Text("Ir al más cercano")
            }
            if (cercano != null && metrosCercano != null) {
                Text("Más cercano: ${cercano.nombre} (${formatearDistancia(metrosCercano)})")
            }
            if (!sinServicios) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = cercano?.id,
                    solicitudDeCamara = solicitudDeCamara,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { id -> enfocarLugar(id) },
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
        }
    }
}
