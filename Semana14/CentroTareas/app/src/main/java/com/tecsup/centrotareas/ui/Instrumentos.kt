package com.tecsup.centrotareas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

internal val Tinta = Color(0xFF2B1932)
internal val Papel = Color(0xFFFFF7EC)
internal val Lima = Color(0xFFD7F36C)
internal val Acero = Color(0xFFB6A3B7)
internal val Ciruela = Color(0xFF633261)

internal val ColoresCentro = lightColorScheme(
    primary = Ciruela, onPrimary = Color.White,
    secondary = Color(0xFF566715),
    surface = Papel, onSurface = Tinta, background = Papel, onBackground = Tinta,
    surfaceVariant = Color(0xFFEDE2E9), onSurfaceVariant = Color(0xFF584B58),
    outline = Color(0xFF81727F), error = Color(0xFF9B2C31)
)

@Composable
internal fun Marca(fraccion: Float) {
    Canvas(Modifier.size(40.dp).background(Lima, CircleShape).padding(8.dp)) {
        val trazo = size.width * .12f
        val puntos = listOf(.20f to .50f, .42f to .72f, .82f to .28f)
        for (i in 0..1) {
            val (x, y) = puntos[i]
            val (finX, finY) = puntos[i + 1]
            val avance = (fraccion * 2 - i).coerceIn(0f, 1f)
            if (avance > 0) drawLine(Tinta, Offset(size.width*x, size.height*y),
                Offset(size.width*(x+(finX-x)*avance), size.height*(y+(finY-y)*avance)), trazo, StrokeCap.Round)
        }
    }
}

@Composable
internal fun Dial(valor: Float, entrada: Float, modifier: Modifier = Modifier) {
    val progreso = valor.coerceIn(0f, 1f)
    Box(modifier.semantics { progressBarRangeInfo = ProgressBarRangeInfo(progreso, 0f..1f) },
        contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val lado = size.minDimension
            val radio = lado * .38f
            val inicio = Offset(center.x - radio, center.y - radio)
            val aro = Size(radio * 2, radio * 2)
            val trazo = Stroke(lado * .054f, cap = StrokeCap.Round)
            drawArc(Color(0xFF58405E), 135f, 270f * entrada, false, inicio, aro, style = trazo)
            if (progreso > 0f) drawArc(Lima, 135f, 270f * progreso, false, inicio, aro, style = trazo)
            for (i in 0..24) {
                val rad = (135.0 + 270.0 * i / 24) * PI / 180
                val vector = Offset(cos(rad).toFloat(), sin(rad).toFloat())
                val externo = lado * .485f
                val interno = externo - lado * (if (i % 4 == 0) .047f else .021f)
                drawLine(if (i / 24f <= progreso) Lima else Acero, center + vector * interno,
                    center + vector * externo, lado * .008f)
            }
        }
        Text("${(progreso * 100).roundToInt()} %", color = Color.White,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    }
}

@Composable
internal fun TituloSeccion(titulo: String) {
    Text(titulo, style = MaterialTheme.typography.headlineMedium,
        fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = Tinta)
}

@Composable
internal fun Resultado(texto: String, error: Boolean = false, accion: (@Composable () -> Unit)? = null) {
    Surface(color = if (error) Color(0xFFFFE3DF) else Color(0xFFE6F2D5),
        contentColor = if (error) Color(0xFF822A2F) else Color(0xFF26462C),
        shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(texto, style = MaterialTheme.typography.bodyLarge)
            accion?.invoke()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Acciones(contenido: @Composable () -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) { contenido() }
}
