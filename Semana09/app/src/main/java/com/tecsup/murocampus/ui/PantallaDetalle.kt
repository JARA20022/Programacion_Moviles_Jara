package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    var editando by rememberSaveable(id) { mutableStateOf(false) }
    val terminar = { vm.terminarEdicion(); editando = false }
    BackHandler(enabled = editando, onBack = terminar)
    when (val actual = estado) {
        UiState.Cargando -> Cargando("Cargando detalle…")
        is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.abrir(id) }
        is UiState.Exito -> {
            val (post, comentarios) = actual.datos
            if (editando) {
                PantallaEditar(vm, post, terminar)
                return
            }
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text("Usuario ${post.userId}", color = MaterialTheme.colorScheme.primary)
                    Text(post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(post.body)
                    OutlinedButton(onClick = { vm.terminarEdicion(); editando = true }) {
                        Text("Editar título")
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Comentarios (${comentarios.size})", style = MaterialTheme.typography.titleMedium)
                }
                items(comentarios, key = { it.id }) { comentario ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(comentario.email, color = MaterialTheme.colorScheme.primary)
                            Text(comentario.body)
                        }
                    }
                }
            }
        }
    }
}
