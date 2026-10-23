package com.jara.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.ui.components.importeSoles

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = datos.maxOfOrNull { it.valor }?.coerceAtLeast(0f) ?: 0f
    val fondo = MaterialTheme.colorScheme.surfaceVariant
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        if (maximo == 0f) Text("Todavía no hay ingresos por servicio esta semana.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        datos.forEach { dato ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(dato.etiqueta, Modifier.weight(1f))
                    Text(importeSoles(dato.valor.toDouble()), fontWeight = FontWeight.Bold)
                }
                Canvas(Modifier.fillMaxWidth().height(22.dp).semantics {
                    contentDescription = "${dato.etiqueta}: ${importeSoles(dato.valor.toDouble())}"
                }) {
                    val radio = CornerRadius(6.dp.toPx())
                    drawRoundRect(fondo, cornerRadius = radio)
                    if (maximo > 0f && dato.valor > 0f) {
                        drawRoundRect(dato.color, size = Size(size.width * (dato.valor / maximo).coerceIn(0f, 1f), size.height), cornerRadius = radio)
                    }
                }
            }
        }
    }
}
