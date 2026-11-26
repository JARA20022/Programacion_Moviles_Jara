package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaEditar(vm: PostsViewModel, post: Post, alCerrar: () -> Unit) {
    val estado by vm.edicion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable(post.id) { mutableStateOf(post.title) }
    val ocupado = estado == UiState.Cargando
    Column(
        Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Editar título", style = MaterialTheme.typography.headlineMedium)
        when (val actual = estado) {
            is UiState.Exito -> {
                Text("Respuesta del servidor", color = MaterialTheme.colorScheme.primary)
                Text(actual.datos.title, style = MaterialTheme.typography.titleLarge)
                Text("Publicación #${actual.datos.id}")
                Button(onClick = alCerrar) { Text("Volver al detalle") }
            }
            else -> {
                OutlinedTextField(titulo, { titulo = it }, modifier = Modifier.fillMaxWidth(),
                    enabled = !ocupado, label = { Text("Título") })
                Button(onClick = { vm.guardarTitulo(post.id, titulo) },
                    enabled = !ocupado && titulo.isNotBlank() && titulo.trim() != post.title) {
                    Text("Guardar título")
                }
                if (ocupado) LinearProgressIndicator(Modifier.fillMaxWidth())
                if (actual is UiState.Error) Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = alCerrar) { Text("Volver al detalle") }
            }
        }
    }
}
