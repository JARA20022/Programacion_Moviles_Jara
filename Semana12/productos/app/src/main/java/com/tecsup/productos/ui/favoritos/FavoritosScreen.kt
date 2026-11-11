package com.tecsup.productos.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.AvisoCatalogo
import com.tecsup.productos.ui.components.BloqueVacio
import com.tecsup.productos.ui.components.CabeceraProductos
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
import com.tecsup.productos.ui.components.PaginaProductos
import com.tecsup.productos.ui.components.TarjetaProducto

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    PaginaProductos(modifier) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item("cabecera") {
                CabeceraProductos("Mis favoritos", "PRODUCTOS") {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            }
            aviso?.let { mensaje ->
                item("aviso") { AvisoCatalogo(mensaje) }
            }
            when (val actual = estado) {
                UiState.Cargando -> item("cargando") { Cargando() }
                is UiState.Error -> item("error") { ErrorView(actual.mensaje, viewModel::cargar) }
                is UiState.Exito -> {
                    if (actual.datos.isEmpty()) {
                        item("vacio") {
                            BloqueVacio("Aún no hay favoritos", "Toca el corazón de un producto para guardarlo.")
                        }
                    } else {
                        items(actual.datos, key = { it.id }) { producto ->
                            TarjetaProducto(
                                producto = producto,
                                onAbrir = { onAbrirDetalle(producto.id) },
                                onFavorito = { viewModel.quitarFavorito(producto.id) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}
