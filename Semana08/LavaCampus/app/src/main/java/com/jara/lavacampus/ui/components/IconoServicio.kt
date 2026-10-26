package com.jara.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun IconoServicio(icono: String, modifier: Modifier = Modifier) {
    val tinta = MaterialTheme.colorScheme.primary
    Surface(
        modifier = modifier.size(64.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 6.dp, bottomEnd = 18.dp, bottomStart = 6.dp)
    ) {
        Canvas(Modifier.padding(14.dp)) {
            val w = size.width
            val h = size.height
            val trazo = Stroke(2.dp.toPx())
            when (icono) {
                "iron" -> {
                    val base = Path().apply {
                        moveTo(w * .08f, h * .78f); lineTo(w * .34f, h * .42f)
                        lineTo(w * .82f, h * .42f); lineTo(w * .94f, h * .78f); close()
                    }
                    drawPath(base, tinta, style = trazo)
                    drawLine(tinta, Offset(w * .4f, h * .42f), Offset(w * .4f, h * .22f), trazo.width)
                    drawLine(tinta, Offset(w * .4f, h * .22f), Offset(w * .75f, h * .22f), trazo.width)
                }
                "shirt" -> {
                    val camisa = Path().apply {
                        moveTo(w * .34f, h * .12f); lineTo(w * .1f, h * .27f)
                        lineTo(w * .2f, h * .47f); lineTo(w * .32f, h * .41f)
                        lineTo(w * .32f, h * .9f); lineTo(w * .7f, h * .9f)
                        lineTo(w * .7f, h * .41f); lineTo(w * .82f, h * .47f)
                        lineTo(w * .92f, h * .27f); lineTo(w * .68f, h * .12f)
                        quadraticBezierTo(w * .51f, h * .34f, w * .34f, h * .12f); close()
                    }
                    drawPath(camisa, tinta, style = trazo)
                }
                else -> {
                    drawRoundRect(tinta, Offset(w * .14f, h * .06f), Size(w * .72f, h * .88f), CornerRadius(4.dp.toPx()), style = trazo)
                    drawCircle(tinta, w * .22f, Offset(w * .5f, h * .6f), style = trazo)
                    drawCircle(tinta, w * .035f, Offset(w * .3f, h * .22f))
                    drawLine(tinta, Offset(w * .46f, h * .22f), Offset(w * .7f, h * .22f), trazo.width)
                }
            }
        }
    }
}
