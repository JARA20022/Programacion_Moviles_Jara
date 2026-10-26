package com.jara.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SelectorCantidad(
    cantidad: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onCambio: (Double) -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    servicio: String = "servicio"
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = { onCambio(-paso) },
            enabled = habilitado && cantidad > 0.0,
            modifier = Modifier.semantics { contentDescription = "Quitar cantidad de $servicio; mínimo activo $minimo" }
        ) { Text("−") }
        Text(
            text = if (paso < 1.0) String.format(Locale.US, "%.1f", cantidad) else cantidad.toInt().toString(),
            modifier = Modifier.widthIn(min = 40.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedButton(
            onClick = { onCambio(paso) },
            enabled = habilitado && cantidad < maximo,
            modifier = Modifier.semantics { contentDescription = "Agregar cantidad de $servicio" }
        ) { Text("+") }
    }
}
