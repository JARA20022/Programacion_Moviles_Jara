package com.tecsup.productos.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.AvisoCatalogo
import com.tecsup.productos.ui.components.BloqueVacio
import com.tecsup.productos.ui.components.CabeceraProductos
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
import com.tecsup.productos.ui.components.PaginaProductos
import com.tecsup.productos.ui.components.PortadaSmartphones
import com.tecsup.productos.ui.components.TarjetaProducto

@Composable
@OptIn(ExperimentalLayoutApi::class)
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
    var busqueda by rememberSaveable { mutableStateOf("") }
    val foco = LocalFocusManager.current
    val actual = estado
    val datos = if (actual is UiState.Exito) actual.datos else emptyList()
    val filtrados = remember(datos, busqueda) {
        val consulta = busqueda.trim()
        datos.filter { it.titulo.contains(consulta, ignoreCase = true) ||
            it.subtitulo.contains(consulta, ignoreCase = true) }
    }

    PaginaProductos(modifier) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item("cabecera") {
                CabeceraProductos("Productos", "CATÁLOGO") {
                    TextButton(onClick = onCerrarSesion) { Text("Cerrar sesión") }
                }
            }
            item("portada") {
                PortadaSmartphones("Smartphones", "Explora por modelo o marca.")
            }
            item("acciones") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = onAbrirFavoritos) { Text("Mis favoritos") }
                    onAbrirAdmin?.let { abrir ->
                        FilledTonalButton(onClick = abrir) { Text("Panel Admin") }
                    }
                }
            }
            item("busqueda") {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar modelo o marca") },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            TextButton(onClick = { busqueda = "" }) { Text("Borrar") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
                    singleLine = true
                )
            }
            aviso?.let { mensaje ->
                item("aviso") { AvisoCatalogo(mensaje, viewModel::cargar) }
            }
            avisoFavorito?.let { mensaje ->
                item("aviso-favorito") { AvisoCatalogo(mensaje) }
            }
            when (actual) {
                UiState.Cargando -> item("cargando") { Cargando() }
                is UiState.Error -> item("error") { ErrorView(actual.mensaje, viewModel::cargar) }
                is UiState.Exito -> {
                    when {
                        datos.isEmpty() -> item("vacio") {
                            BloqueVacio("Catálogo vacío", "Todavía no hay productos disponibles.")
                        }
                        filtrados.isEmpty() -> item("sin-resultados") {
                            BloqueVacio("Sin coincidencias", "Prueba con otro modelo o marca.")
                        }
                        else -> {
                            item("resultados") {
                                Text(
                                    if (filtrados.size == 1) "1 producto" else "${filtrados.size} productos",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            items(filtrados, key = { it.id }) { producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    onAbrir = { onAbrirDetalle(producto.id) },
                                    onFavorito = { viewModel.alternarFavorito(producto.id) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
