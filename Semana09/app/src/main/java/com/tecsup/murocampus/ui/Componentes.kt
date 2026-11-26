package com.tecsup.murocampus.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

@Composable
fun Entrada(content: @Composable () -> Unit) {
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(visibleState = visible, enter = fadeIn(tween(220)) +
        slideInVertically(tween(260)) { it / 18 }) { content() }
}

@Composable
fun BarraCampus(secundaria: Boolean, volver: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(
            Color(0xFF28225C), Color(0xFF263A7F), Color(0xFF105B69)
        ))).drawBehind {
            val centro = Offset(size.width * .91f, size.height * 1.13f)
            repeat(4) { i ->
                drawCircle(Color(0x5566F2D6), (16 + i * 13).dp.toPx(), centro, style = Stroke(1.dp.toPx()))
            }
        }.height(58.dp).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (secundaria) {
            FilledTonalIconButton(onClick = volver, modifier = Modifier.size(48.dp)) { Text("‹", fontSize = 28.sp) }
        } else {
            Canvas(Modifier.size(36.dp).clip(RoundedCornerShape(11.dp)).background(Color(0xFF65F3DA))) {
                val tinta = Color(0xFF173F52)
                val puntos = listOf(.20f to .63f, .40f to .34f, .57f to .63f, .80f to .33f)
                puntos.zipWithNext().forEach { (a,b) ->
                    drawLine(tinta, Offset(a.first*size.width,a.second*size.height),
                        Offset(b.first*size.width,b.second*size.height), 3.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round)
                }
            }
        }
        Text("MuroCampus", color = Color(0xFFEEDEFF), style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.6).sp)
    }
}

@Composable
fun BotonCampus(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, activo: Boolean = true) {
    val interaccion = remember { MutableInteractionSource() }
    val presionado by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(if (presionado) .96f else 1f, label = "Pulsación")
    val colores = if (activo) listOf(Color(0xFF65F3DA), Color(0xFF91CBFF))
        else listOf(Color(0xFF35405D), Color(0xFF35405D))
    Box(modifier.graphicsLayer { scaleX = escala; scaleY = escala }.clip(RoundedCornerShape(18.dp))
        .background(Brush.horizontalGradient(colores))
        .clickable(interactionSource = interaccion, indication = LocalIndication.current,
            enabled = activo, role = Role.Button, onClick = onClick)
        .heightIn(min = 48.dp).padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center) {
        Text(texto, color = if (activo) Color(0xFF0B2B3A) else Color(0xFFB2BBD3),
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TarjetaPost(usuario: Int, titulo: String, texto: String, alTocar: () -> Unit) {
    val acentos = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary)
    val acento = acentos[(usuario - 1).mod(acentos.size)]
    OutlinedCard(onClick = alTocar, modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(
            acento.copy(alpha = .075f), Color.Transparent))).padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(acento))
                Text("Usuario $usuario", color = acento, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.weight(1f))
                Text("↗", color = acento, fontSize = 20.sp)
            }
            Text(titulo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium,
                maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun Cargando(mensaje: String = "Cargando publicaciones…") {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Text(mensaje)
        }
    }
}

@Composable
fun ErrorConReintento(mensaje: String, reintentar: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(mensaje)
            BotonCampus("Reintentar", reintentar)
        }
    }
}

@Composable
fun CabeceraEditorial(titulo: String, contexto: String) {
    val forma = RoundedCornerShape(topStart = 26.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 26.dp)
    Column(Modifier.fillMaxWidth().clip(forma)
        .background(Brush.linearGradient(listOf(Color(0xFF343075), Color(0xFF193D62), Color(0xFF13565B))))
        .drawBehind {
            val foco = Offset(size.width + 8.dp.toPx(), size.height)
            for (r in 1..4) drawCircle(Color(0x2970FADC), r * 18.dp.toPx(), foco, style = Stroke(1.dp.toPx()))
        }.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(contexto, color = Color(0xFFA0F4DA), style = MaterialTheme.typography.labelLarge)
        Text(titulo, color = Color(0xFFECE7FF), fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.headlineSmall)
    }
}
