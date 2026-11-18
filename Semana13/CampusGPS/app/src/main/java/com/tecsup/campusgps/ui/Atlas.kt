package com.tecsup.campusgps.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

internal val Tinta = Color(0xFF30223B)
internal val Papel = Color(0xFFF8F4EB)
internal val Arcilla = Color(0xFF93391E)
internal val Melon = Color(0xFFFFB68E)
internal val Linea = Color(0xFFD8CDC0)

@Composable
internal fun CabeceraAtlas(coordenadas: String, buscando: Boolean, disponible: Boolean, ubicar: () -> Unit) {
    val giro by animateFloatAsState(if (buscando) 120f else -20f, tween(480), label = "Brújula")
    Row(
        Modifier.fillMaxWidth().background(Tinta).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Canvas(Modifier.size(34.dp).rotate(giro)) {
            val r = size.minDimension / 2
            drawCircle(Melon, r - 1.dp.toPx(), style = Stroke(1.dp.toPx()))
            val aguja = Path().apply {
                moveTo(r, r * 0.22f)
                lineTo(r * 1.35f, r * 1.55f)
                lineTo(r, r * 1.28f)
                lineTo(r * 0.65f, r * 1.55f)
                close()
            }
            drawPath(aguja, Melon)
        }
        Column(Modifier.weight(1f)) {
            Text("CampusGPS", color = Papel, fontSize = 25.sp, fontFamily = FontFamily.Serif)
            Text(coordenadas, color = Melon, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
        }
        Button(onClick = ubicar, enabled = disponible && !buscando, shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Melon, contentColor = Tinta)) {
            Text(if (buscando) "Buscando…" else "Ubicarme")
        }
    }
}

@Composable
internal fun FiltrosAtlas(categorias: List<String>, activa: String?, elegir: (String?) -> Unit) {
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        (listOf<String?>(null) + categorias).forEach { categoria ->
            val marcado = categoria == activa
            val fondo by animateColorAsState(if (marcado) Tinta else Color(0xFFEDE6D9), label = "Categoría")
            Box(
                Modifier.heightIn(min = 48.dp).selectable(selected = marcado, role = Role.Tab,
                    onClick = { elegir(categoria) }).padding(vertical = 7.dp)
                    .clip(CircleShape).background(fondo).padding(horizontal = 15.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(categoria ?: "Todos", color = if (marcado) Papel else Tinta,
                    fontSize = 13.sp, fontWeight = if (marcado) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
internal fun DestinoAtlas(nombre: String?, distancia: String?, ir: () -> Unit) {
    val fondo by animateColorAsState(if (nombre == null) Color(0xFFECE5D9) else Color(0xFFF9DDC9),
        label = "Destino cercano")
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(fondo)
        .clickable(enabled = nombre != null, role = Role.Button, onClick = ir).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Ir al más cercano", color = Arcilla, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            if (nombre != null && distancia != null) Text("$nombre · $distancia", color = Tinta, fontSize = 15.sp)
        }
        Text("↗", color = Arcilla, fontSize = 22.sp)
    }
}
