package com.jara.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Contrato compartido por los dos gráficos de Canvas.
data class DatoGrafico(val etiqueta: String, val valor: Float, val color: Color)

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor.coerceAtLeast(0f).toDouble() }.toFloat()
    val fondo = MaterialTheme.colorScheme.surfaceVariant
    val descripcion = datos.joinToString { "${it.etiqueta}: ${it.valor.toInt()}" }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(204.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.matchParentSize().semantics { contentDescription = descripcion }) {
                val ancho = 24.dp.toPx()
                val margen = ancho / 2
                val area = Size(size.width - ancho, size.height - ancho)
                drawArc(fondo, -90f, 360f, false, Offset(margen, margen), area, style = Stroke(ancho))
                if (total > 0f) {
                    var inicio = -90f
                    datos.forEach { dato ->
                        val angulo = dato.valor.coerceAtLeast(0f) / total * 360f
                        if (angulo > 0f) drawArc(dato.color, inicio, angulo, false, Offset(margen, margen), area, style = Stroke(ancho))
                        inicio += angulo
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toInt().toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text("pedidos", style = MaterialTheme.typography.bodySmall)
            }
        }
        if (total == 0f) Text("Sin pedidos para mostrar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        datos.forEach { dato ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Canvas(Modifier.size(10.dp)) { drawCircle(dato.color) }
                Text(dato.etiqueta, Modifier.weight(1f))
                Text(dato.valor.toInt().toString(), fontWeight = FontWeight.Bold)
            }
        }
    }
}
