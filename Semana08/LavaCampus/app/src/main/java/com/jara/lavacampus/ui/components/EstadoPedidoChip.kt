package com.jara.lavacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.lavacampus.model.EstadoPedido

fun nombreEstado(estado: EstadoPedido): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.EN_PROCESO -> "En proceso"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}

fun colorEstado(estado: EstadoPedido): Color = when (estado) {
    EstadoPedido.RECIBIDO -> Color(0xFF7B4A08)
    EstadoPedido.EN_PROCESO -> Color(0xFF334BA5)
    EstadoPedido.LISTO -> Color(0xFF176447)
    EstadoPedido.ENTREGADO -> Color(0xFF535A62)
    EstadoPedido.CANCELADO -> Color(0xFF9C2D35)
}

@Composable
fun EstadoPedidoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = colorEstado(estado),
        contentColor = Color.White
    ) {
        Text(
            text = nombreEstado(estado),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
