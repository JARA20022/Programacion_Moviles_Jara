package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    abrirAjustes: () -> Unit,
    reintentar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(error.mensaje, color = MaterialTheme.colorScheme.onErrorContainer)
            when (error.tipo) {
                TipoError.SIN_PERMISO -> TextButton(onClick = abrirAjustes) { Text("Abrir ajustes") }
                TipoError.GPS_APAGADO, TipoError.OTRO ->
                    TextButton(onClick = reintentar) { Text("Reintentar") }
                TipoError.SIN_PLAY_SERVICES -> Unit
            }
        }
    }
}
