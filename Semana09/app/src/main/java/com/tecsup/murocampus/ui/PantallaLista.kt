package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaLista(vm: PostsViewModel, alAbrir: (Int) -> Unit = {}) {
    val estado by vm.resultados.collectAsStateWithLifecycle()
    val consulta by vm.consulta.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current
    val lista = rememberLazyListState()
    LaunchedEffect(consulta, filtro) { lista.scrollToItem(0) }
    LazyColumn(state = lista, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
        start = 16.dp, end = 16.dp, top = 16.dp, bottom = 92.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item(key = "encabezado") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Publicaciones", modifier = Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold)
                (estado as? UiState.Exito)?.let {
                    Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.primaryContainer) {
                        Text("${it.datos.size}", Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item(key = "buscar") {
            OutlinedTextField(consulta, vm::buscar, modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium, singleLine = true, label = { Text("Buscar por título") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { foco.clearFocus() }),
                trailingIcon = {
                    if (consulta.isNotEmpty()) TextButton(onClick = { vm.buscar("") }) { Text("Borrar") }
                })
        }
        item(key = "filtros") {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(11) { i ->
                    val id = i.takeIf { it > 0 }
                    FilterChip(selected = filtro == id, onClick = { vm.filtrar(id) },
                        label = { Text(if (id == null) "Todos" else "Usuario $id") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary))
                }
            }
        }
        when (val actual = estado) {
            UiState.Cargando -> item { Box(Modifier.fillMaxWidth().height(180.dp)) { Cargando() } }
            is UiState.Error -> item { ErrorConReintento(actual.mensaje, vm::cargar) }
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) item {
                    Text(if (consulta.isBlank()) "Sin publicaciones" else "Sin coincidencias", Modifier.padding(12.dp))
                }
                items(actual.datos, key = { it.id }) { post ->
                    TarjetaPost(post.userId, post.title, post.body) { alAbrir(post.id) }
                }
            }
        }
    }
}
