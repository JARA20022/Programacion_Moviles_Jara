package com.tecsup.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(lugares, key = { it.lugar.id }) { item ->
            val lugar = item.lugar
            val activo = lugar.id == seleccionado
            val colores = MaterialTheme.colorScheme
            Card(
                onClick = { onClick(lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (activo) colores.primaryContainer else colores.surfaceVariant
                ),
                border = if (activo) BorderStroke(2.dp, colores.primary) else null
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(Modifier.size(10.dp).background(Color(0xFF0088CC), CircleShape))
                    Column(Modifier.weight(1f)) {
                        Text(lugar.nombre, fontWeight = FontWeight.Bold)
                        Text(lugar.categoria, color = colores.onSurfaceVariant)
                    }
                    Text(item.metros?.let(::formatearDistancia) ?: "Sin calcular")
                }
            }
        }
    }
}
