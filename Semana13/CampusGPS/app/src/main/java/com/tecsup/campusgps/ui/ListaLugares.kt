package com.tecsup.campusgps.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    dentroDelRadio: Set<Int>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    cabecera: @Composable () -> Unit
) {
    LazyColumn(modifier, contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 16.dp)) {
        item(key = "destino") { Column(verticalArrangement = Arrangement.spacedBy(6.dp)) { cabecera() } }
        item(key = "titulo") {
            Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Lugares", Modifier.weight(1f), fontSize = 23.sp, fontFamily = FontFamily.Serif, color = Tinta)
                Text("${lugares.size}", color = Arcilla, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
        items(lugares, key = { it.lugar.id }) { item ->
            val lugar = item.lugar
            val activo = lugar.id == seleccionado
            val superficie by animateColorAsState(if (activo) Tinta else Papel, label = "Lugar seleccionado")
            val texto = if (activo) Papel else Tinta
            Column(Modifier.animateItem().fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(superficie)
                .clickable { onClick(lugar.id) }) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(lugar.categoria, fontSize = 11.sp, color = if (activo) Melon else Arcilla)
                        Text(lugar.nombre, color = texto, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(item.metros?.let(::formatearDistancia) ?: "Sin calcular",
                            color = texto, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        if (lugar.id in dentroDelRadio) {
                            Text("Dentro del radio", color = if (activo) Melon else Arcilla,
                                fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                HorizontalDivider(color = if (activo) Color.Transparent else Linea)
            }
        }
    }
}
