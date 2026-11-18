package com.tecsup.campusgps.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }
    var mapaListo by remember { mutableStateOf(false) }
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    val punto = posicion?.let { LatLng(it.latitud, it.longitud) }

    if (mapaListo) {
        LaunchedEffect(estado) {
            if (punto != null) camara.animate(CameraUpdateFactory.newLatLngZoom(punto, 16f))
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        onMapLoaded = { mapaListo = true }
    ) {
        if (punto != null) {
            Marker(state = rememberUpdatedMarkerState(position = punto), title = "Estás aquí")
        }
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                Marker(
                    state = rememberUpdatedMarkerState(position = LatLng(lugar.latitud, lugar.longitud)),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
    }
}
