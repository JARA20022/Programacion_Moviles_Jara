package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.ModoCarga
import com.tecsup.centrotareas.model.TiemposCarga

@Composable
internal fun TablaTiempos(tiempos: TiemposCarga) {
    Surface(color = Pizarra, contentColor = Hielo, shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Comparación de tiempos", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            val filas = maxOf(tiempos.secuenciales.size, tiempos.paralelos.size)
            if (filas == 0) {
                Text("Completa una carga para registrar su tiempo.", color = Niebla)
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Prueba", Modifier.weight(.8f), color = Niebla)
                    Text(ModoCarga.SECUENCIAL.etiqueta, Modifier.weight(1.15f), color = Menta)
                    Text(ModoCarga.PARALELO.etiqueta, Modifier.weight(1.15f), color = Coral)
                }
                for (i in filas - 1 downTo 0) {
                    HorizontalDivider(color = Niebla.copy(alpha = .24f))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text((i + 1).toString(), Modifier.weight(.8f))
                        Text(tiempos.secuenciales.getOrNull(i)?.let { "$it ms" } ?: "—",
                            Modifier.weight(1.15f), fontFamily = FontFamily.Monospace)
                        Text(tiempos.paralelos.getOrNull(i)?.let { "$it ms" } ?: "—",
                            Modifier.weight(1.15f), fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}
