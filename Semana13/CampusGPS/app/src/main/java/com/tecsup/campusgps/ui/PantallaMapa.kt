package com.tecsup.campusgps.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

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

    val categoria by vm.categoria.collectAsState()
    val coordenadas = posicion?.let {
        String.format(Locale.US, "%.5f°, %.5f°", it.latitud, it.longitud)
    } ?: "Ubicación pendiente"

    val mapa: @Composable (Modifier) -> Unit = { marco ->
        Box(marco.clip(RoundedCornerShape(topEnd = 28.dp, bottomStart = 28.dp)).background(Papel)) {
            if (!sinServicios) {
                MapaUbicacion(estado, lugares, seleccionado, cercano?.id, solicitudDeCamara, Modifier.fillMaxSize())
            }
        }
    }
    val indice: @Composable (Modifier) -> Unit = { panel ->
        Column(panel) {
            FiltrosAtlas(vm.categorias, categoria, vm::filtrarPor)
            ListaLugares(lugares, seleccionado, { id -> enfocarLugar(id) }, Modifier.fillMaxSize()) {
                val actual = estado
                if (actual is UbicacionUi.Error) {
                    MensajeError(
                        error = actual,
                        abrirAjustes = {
                            val uri = Uri.fromParts("package", contexto.packageName, null)
                            contexto.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri))
                        },
                        reintentar = { alPulsar() }
                    )
                }
                DestinoAtlas(cercano?.nombre, metrosCercano?.let(::formatearDistancia)) {
                    cercano?.let { enfocarLugar(it.id) }
                }
            }
        }
    }

    Column(
        Modifier.fillMaxSize().background(Tinta).safeDrawingPadding()
    ) {
        CabeceraAtlas(coordenadas, estado == UbicacionUi.Buscando, !sinServicios) { alPulsar() }
        BoxWithConstraints(Modifier.fillMaxSize().background(Papel)) {
            if (maxWidth > maxHeight && maxWidth >= 560.dp) {
                Row(Modifier.fillMaxSize().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!sinServicios) mapa(Modifier.fillMaxHeight().weight(1f))
                    indice(Modifier.fillMaxHeight().weight(1f))
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    if (!sinServicios) mapa(Modifier.fillMaxWidth().weight(0.42f))
                    indice(Modifier.fillMaxWidth().weight(0.58f))
                }
            }
        }
    }
}
