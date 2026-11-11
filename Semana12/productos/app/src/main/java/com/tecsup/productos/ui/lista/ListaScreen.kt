package com.tecsup.productos.ui.lista

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
fun ListaScreen(
    viewModel: ListaViewModel,
    onAbrirDetalle: (Int) -> Unit,
    onAbrirFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit,
    onAbrirAdmin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val avisoFavorito by viewModel.avisoFavorito.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Catálogo de productos", style = MaterialTheme.typography.headlineSmall)
        Text("Smartphones", style = MaterialTheme.typography.bodyMedium)
        TextButton(onClick = onCerrarSesion) {
            Text("Cerrar sesión")
        }
        TextButton(onClick = onAbrirFavoritos) {
            Text("Ver favoritos")
        }
        onAbrirAdmin?.let { abrir ->
            TextButton(onClick = abrir) {
                Text("Panel Admin")
            }
        }
        aviso?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = viewModel::cargar) {
                Text("Reintentar")
            }
        }
        avisoFavorito?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }

        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) {
                    Text("No hay productos disponibles.")
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
                                    TextButton(onClick = { viewModel.alternarFavorito(producto.id) }) {
                                        Text(
                                            if (producto.esFavorito) "Quitar favorito" else "Marcar favorito"
                                        )
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
