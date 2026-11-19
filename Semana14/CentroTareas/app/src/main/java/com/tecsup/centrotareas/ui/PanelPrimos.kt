package com.tecsup.centrotareas.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.PrimosUi

@Composable
internal fun PanelPrimos(estado: PrimosUi, toques: Int, tocar: () -> Unit) {
    val (porcentaje, titulo) = when (estado) {
        PrimosUi.Inicial -> 0 to "Listo para contar"
        is PrimosUi.Calculando -> estado.porcentaje to "Contando primos"
        is PrimosUi.Cancelado -> estado.porcentaje to "Conteo cancelado"
        is PrimosUi.Listo -> 100 to "Conteo completo"
    }
    val acento by animateColorAsState(if (estado is PrimosUi.Cancelado) Coral else Menta,
        animationSpec = tween(220), label = "Estado del conteo")
    val forma = RoundedCornerShape(topStart = 24.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 24.dp)
    Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF103C43), Pizarra)), forma)
        .border(1.dp, acento.copy(alpha = .45f), forma).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(titulo, color = Hielo, style = MaterialTheme.typography.titleMedium)
        Text("$porcentaje %", color = acento, style = MaterialTheme.typography.displayMedium,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Canvas(Modifier.fillMaxWidth().height(48.dp).semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(porcentaje / 100f, 0f..1f)
        }) {
            val paso = size.width / 32
            val llenado = porcentaje * 32 / 100f
            for (i in 0 until 32) {
                val x = (i + .5f) * paso
                val h = size.height * (.24f + .68f * i / 31)
                val base = size.height - paso.coerceAtMost(8f) / 2
                val grosor = paso * .48f
                drawLine(Color(0xFF44616A), Offset(x, base), Offset(x, base - h), grosor, StrokeCap.Round)
                val fraccion = (llenado - i).coerceIn(0f, 1f)
                if (fraccion > 0f) {
                    drawLine(acento, Offset(x, base), Offset(x, base - h * fraccion), grosor, StrokeCap.Round)
                }
            }
        }
        TextButton(onClick = tocar) { Text("Tócame · $toques", color = Hielo) }
    }
}
