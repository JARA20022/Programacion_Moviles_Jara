package com.tecsup.productos.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
import java.util.Locale

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver al catálogo")
        }
        Text("Detalle del producto", style = MaterialTheme.typography.titleMedium)

        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                val producto = actual.datos
                Text(producto.titulo, style = MaterialTheme.typography.headlineSmall)
                Text("Marca: ${producto.subtitulo}")
                producto.precio?.let { precio ->
                    val precioFormateado = String.format(Locale.getDefault(), "%.2f", precio)
                    Text("Precio: $precioFormateado")
                }
                producto.descripcion?.takeIf { it.isNotBlank() }?.let { descripcion ->
                    Text(descripcion, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
