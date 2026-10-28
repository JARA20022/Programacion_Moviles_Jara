package com.jara.lavacampus.ui.components

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.data.local.relation.PedidoConDetalle
import com.jara.lavacampus.model.EstadoPedido
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun importeSoles(valor: Double): String = String.format(Locale("es", "PE"), "S/ %.2f", valor)

fun fechaPedido(fecha: String): String = runCatching {
    LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "PE")))
}.getOrDefault(fecha)

@Composable
fun PedidoCard(pedido: PedidoConDetalle, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val datos=pedido.pedido
    val estado=EstadoPedido.valueOf(datos.estado)
    Card(onClick=onClick,modifier=modifier.fillMaxWidth(),shape=RoundedCornerShape(26.dp),
        colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                Surface(shape=RoundedCornerShape(16.dp),color=MaterialTheme.colorScheme.primaryContainer) {
                    androidx.compose.foundation.layout.Box(Modifier.padding(14.dp)) { IconoStudio("servicios",color=MaterialTheme.colorScheme.onPrimaryContainer) }
                }
                Column(Modifier.weight(1f)) {
                    Text("Pedido #${datos.id.toString().padStart(3,'0')}",style=MaterialTheme.typography.titleMedium)
                    Text(fechaPedido(datos.fechaRegistro),style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconoStudio("flecha",color=MaterialTheme.colorScheme.primary)
            }
            Text(pedido.detalles.joinToString(" · "){it.servicio.nombre},style=MaterialTheme.typography.bodyMedium,maxLines=2,overflow=TextOverflow.Ellipsis)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                EstadoPedidoChip(estado)
                Text(importeSoles(datos.total),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
            }
            if(datos.descuento>0)Text("Ahorraste ${importeSoles(datos.descuento)}",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.tertiary)
            if(estado==EstadoPedido.LISTO && datos.casillero!=null) {
                Surface(modifier=Modifier.fillMaxWidth(),color=MaterialTheme.colorScheme.tertiaryContainer,contentColor=MaterialTheme.colorScheme.onTertiaryContainer,shape=RoundedCornerShape(14.dp)) {
                    Text("Recógelo en el casillero ${datos.casillero}",Modifier.padding(14.dp),fontWeight=FontWeight.SemiBold)
                }
            }
        }
    }
}
