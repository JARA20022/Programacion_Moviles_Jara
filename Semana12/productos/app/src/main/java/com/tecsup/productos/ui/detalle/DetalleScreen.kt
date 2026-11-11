package com.tecsup.productos.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.CabeceraProductos
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ColumnaProductos
import com.tecsup.productos.ui.components.ErrorView
import com.tecsup.productos.ui.components.FotoProducto
import java.text.NumberFormat

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    ColumnaProductos(modifier) {
        CabeceraProductos("Detalle", "PRODUCTOS") {
            TextButton(onClick = onVolver) { Text("Volver") }
        }
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                val producto = actual.datos
                FotoProducto(producto, Modifier.fillMaxWidth().height(260.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            producto.subtitulo, color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            producto.titulo, style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        producto.precio?.let { precio ->
                            val formato = NumberFormat.getNumberInstance().apply {
                                minimumFractionDigits = 2
                                maximumFractionDigits = 2
                            }
                            Text(
                                "Precio · ${formato.format(precio)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        producto.descripcion?.takeIf { it.isNotBlank() }?.let {
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
