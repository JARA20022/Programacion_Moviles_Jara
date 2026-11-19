package com.tecsup.campusgps.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tecsup.campusgps.R
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    solicitudDeCamara: Int,
    radioMetros: Int,
    modifier: Modifier = Modifier
) {
    val oscuro = isSystemInDarkTheme()
    val contexto = LocalContext.current
    val propiedades = remember(contexto, oscuro) {
        val estilo = if (oscuro) MapStyleOptions.loadRawResourceStyle(contexto, R.raw.mapa_nocturno) else null
        MapProperties(mapStyleOptions = estilo)
    }
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }
    var mapaListo by remember { mutableStateOf(false) }
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    val punto = posicion?.let { LatLng(it.latitud, it.longitud) }

    if (mapaListo) {
        LaunchedEffect(seleccionado, solicitudDeCamara) {
            val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            if (lugar != null) {
                val destino = LatLng(lugar.latitud, lugar.longitud)
                camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 17f))
            }
        }
        LaunchedEffect(estado) {
            if (punto != null) camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 16f))
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = propiedades,
        onMapLoaded = { mapaListo = true }
    ) {
        if (punto != null) {
            Circle(
                center = punto,
                radius = radioMetros.toDouble(),
                strokeColor = if (oscuro) Melon else Arcilla,
                fillColor = Melon.copy(alpha = if (oscuro) 0.13f else 0.22f),
                strokeWidth = 3f
            )
            Marker(state = rememberUpdatedMarkerState(position = punto), title = "Estás aquí")
        }
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val color = if (lugar.id == masCercanoId) {
                    BitmapDescriptorFactory.HUE_GREEN
                } else {
                    BitmapDescriptorFactory.HUE_AZURE
                }
                val marcador = rememberUpdatedMarkerState(position = LatLng(lugar.latitud, lugar.longitud))
                Marker(
                    state = marcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(color)
                )
                LaunchedEffect(seleccionado, solicitudDeCamara, mapaListo) {
                    if (mapaListo && lugar.id == seleccionado) marcador.showInfoWindow()
                }
            }
        }
    }
}
