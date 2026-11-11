package com.tecsup.productos.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver al catálogo")
        }
        Text("Mis favoritos", style = MaterialTheme.typography.headlineSmall)
        aviso?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }

        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) {
                    Text("Aún no tienes favoritos. Marca un producto desde el catálogo.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(actual.datos, key = { it.id }) { producto ->
                            Card(
                                onClick = { onAbrirDetalle(producto.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(producto.titulo, style = MaterialTheme.typography.titleMedium)
                                    Text(producto.subtitulo, style = MaterialTheme.typography.bodyMedium)
                                    TextButton(onClick = { viewModel.quitarFavorito(producto.id) }) {
                                        Text("Quitar favorito")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
