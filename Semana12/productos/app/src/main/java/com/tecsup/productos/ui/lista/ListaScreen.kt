package com.tecsup.productos.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    val conectado = hayInternetCatalogo()
    val estadoCuadricula = rememberLazyGridState()
    val actual = estado
    val datos = if (actual is UiState.Exito) actual.datos else emptyList()
    val filtrados = remember(datos, busqueda) {
        val consulta = busqueda.trim()
        datos.filter { it.titulo.contains(consulta, ignoreCase = true) ||
            it.subtitulo.contains(consulta, ignoreCase = true) }
    }

    PaginaProductos(modifier) {
        BoxWithConstraints(
            modifier = Modifier.widthIn(max = 1000.dp).fillMaxSize()
        ) {
            val compacto = maxWidth > maxHeight && maxHeight < 500.dp
            val dosColumnas = maxWidth >= 640.dp
            Column(Modifier.fillMaxSize()) {
                if (!conectado) {
                    AvisoSinInternet(
                        mensaje = if (datos.isEmpty()) {
                            "Sin conexión a internet."
                        } else {
                            "Sin conexión. Mostrando productos guardados."
                        },
                        reintentar = viewModel::cargar
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (compacto && dosColumnas) 2 else 1),
                    state = estadoCuadricula,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(if (compacto) 4.dp else 12.dp)
                ) {
                    item("cabecera", span = { GridItemSpan(maxLineSpan) }) {
                        CabeceraProductos("Productos", "CATÁLOGO") {
                            TextButton(onClick = onCerrarSesion) { Text("Cerrar sesión") }
                        }
                    }
                    if (!compacto) {
                        item("portada", span = { GridItemSpan(maxLineSpan) }) {
                            PortadaSmartphones("Smartphones", "Explora por modelo o marca.")
                        }
                    }
                    item("controles", span = { GridItemSpan(maxLineSpan) }) {
                        val acciones: @Composable () -> Unit = {
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(onClick = onAbrirFavoritos) { Text("Mis favoritos") }
                                onAbrirAdmin?.let { abrir ->
                                    FilledTonalButton(onClick = abrir) { Text("Panel Admin") }
                                }
                            }
                        }
                        val buscar: @Composable (Modifier) -> Unit = { campo ->
                            OutlinedTextField(
                                value = busqueda,
                                onValueChange = { busqueda = it },
                                label = { Text("Buscar modelo o marca") },
                                trailingIcon = {
                                    if (busqueda.isNotEmpty()) {
                                        TextButton(onClick = { busqueda = "" }) { Text("Borrar") }
                                    }
                                },
                                modifier = campo,
                                shape = RoundedCornerShape(18.dp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
                                singleLine = true
                            )
                        }
                        if (compacto && dosColumnas) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                buscar(Modifier.weight(1f))
                                acciones()
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                acciones()
                                buscar(Modifier.fillMaxWidth())
                            }
                        }
                    }
                    if (conectado) {
                        aviso?.let { mensaje ->
                            item("aviso", span = { GridItemSpan(maxLineSpan) }) { AvisoCatalogo(mensaje, viewModel::cargar) }
                        }
                    }
                    avisoFavorito?.let { mensaje ->
                        item("aviso-favorito", span = { GridItemSpan(maxLineSpan) }) { AvisoCatalogo(mensaje) }
                    }
                    when (actual) {
                        UiState.Cargando -> item("cargando", span = { GridItemSpan(maxLineSpan) }) { Cargando() }
                        is UiState.Error -> item("error", span = { GridItemSpan(maxLineSpan) }) {
                            if (conectado) {
                                ErrorView(actual.mensaje, viewModel::cargar)
                            } else {
                                BloqueVacio("Catálogo sin datos", actual.mensaje)
                            }
                        }
                        is UiState.Exito -> {
                            when {
                                datos.isEmpty() -> item("vacio", span = { GridItemSpan(maxLineSpan) }) {
                                    BloqueVacio("Catálogo vacío", "Todavía no hay productos disponibles.")
                                }
                                filtrados.isEmpty() -> item("sin-resultados", span = { GridItemSpan(maxLineSpan) }) {
                                    BloqueVacio("Sin coincidencias", "Prueba con otro modelo o marca.")
                                }
                                else -> {
                                    if (!compacto) item("resultados", span = { GridItemSpan(maxLineSpan) }) {
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
    }
}

@Composable
private fun AvisoSinInternet(mensaje: String, reintentar: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(mensaje, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = reintentar) { Text("Reintentar") }
        }
    }
}
