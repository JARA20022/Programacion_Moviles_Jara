package com.jara.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.lavacampus.model.EstadoPedido

@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    sobreOscuro: Boolean = false,
    vertical: Boolean = false
) {
    if (estado == EstadoPedido.CANCELADO) {
        Text("Pedido cancelado", modifier = modifier, color = MaterialTheme.colorScheme.error)
        return
    }

    val pasos = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.EN_PROCESO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO
    )
    val actual = pasos.indexOf(estado)
    val acento = if (sobreOscuro) Color(0xFFFFC5AD) else MaterialTheme.colorScheme.primary
    val tenue = if (sobreOscuro) Color(0xFF82908F) else MaterialTheme.colorScheme.outline
    val texto = if (sobreOscuro) Color.White else MaterialTheme.colorScheme.onSurface

    if (vertical) {
        Column(modifier.fillMaxWidth()) {
            pasos.forEachIndexed { indice, paso ->
                val alcanzado = indice <= actual
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = if (alcanzado) acento else Color.Transparent,
                        border = BorderStroke(2.dp, if (alcanzado) acento else tenue)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                (indice + 1).toString(),
                                color = if (alcanzado) {
                                    if (sobreOscuro) Color(0xFF20262A) else MaterialTheme.colorScheme.onPrimary
                                } else texto,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column {
                        Text(nombreEstado(paso), color = if (alcanzado) texto else tenue, fontWeight = FontWeight.Bold)
                        Text(
                            when { indice < actual -> "Completado"; indice == actual -> "Estado actual"; else -> "Pendiente" },
                            color = tenue,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (indice < pasos.lastIndex) {
                    Canvas(Modifier.size(width = 32.dp, height = 24.dp)) {
                        drawLine(
                            color = if (indice < actual) acento else tenue,
                            start = Offset(size.width / 2, 0f),
                            end = Offset(size.width / 2, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
            }
        }
        return
    }

    Row(modifier.fillMaxWidth()) {
        pasos.forEachIndexed { indice, paso ->
            val alcanzado = indice <= actual
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Box(Modifier.fillMaxWidth().height(26.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.matchParentSize()) {
                        val mitad = size.width / 2
                        val y = size.height / 2
                        if (indice > 0) {
                            drawLine(
                                color = if (indice <= actual) acento else tenue,
                                start = Offset(0f, y),
                                end = Offset(mitad, y),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                        if (indice < pasos.lastIndex) {
                            drawLine(
                                color = if (indice < actual) acento else tenue,
                                start = Offset(mitad, y),
                                end = Offset(size.width, y),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.size(26.dp),
                        shape = CircleShape,
                        color = if (alcanzado) acento else if (sobreOscuro) {
                            Color(0xFF20262A)
                        } else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(2.dp, if (alcanzado) acento else tenue)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (indice + 1).toString(),
                                color = if (alcanzado) {
                                    if (sobreOscuro) Color(0xFF20262A) else MaterialTheme.colorScheme.onPrimary
                                } else texto,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = if (paso == EstadoPedido.EN_PROCESO) "En\nproceso" else nombreEstado(paso),
                    color = if (alcanzado) texto else tenue,
                    fontWeight = if (indice == actual) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
