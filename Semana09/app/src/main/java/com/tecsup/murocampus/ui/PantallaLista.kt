package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaLista(vm: PostsViewModel, alAbrir: (Int) -> Unit = {}) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(selected = filtro == null, onClick = { vm.filtrar(null) }, label = { Text("Todos") })
            }
            items(10) { indice ->
                val id = indice + 1
                FilterChip(selected = filtro == id, onClick = { vm.filtrar(id) }, label = { Text("Usuario $id") })
            }
        }
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) Text("Sin publicaciones", Modifier.padding(16.dp))
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(actual.datos, key = { it.id }) { post ->
                        TarjetaPost(post.userId, post.title, post.body) { alAbrir(post.id) }
                    }
                }
            }
        }
    }
}
