package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaCrear(vm: PostsViewModel) {
    val estado by vm.creacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }
    // Evita borrar un borrador nuevo al rotar después de un POST exitoso.
    var limpiarAlPublicar by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(estado) {
        if (estado is UiState.Exito && limpiarAlPublicar) {
            titulo = ""
            texto = ""
            limpiarAlPublicar = false
        }
    }
    val disponible = estado != UiState.Cargando
    val publicar = {
        limpiarAlPublicar = true
        vm.publicar(titulo, texto)
    }
    Column(
        Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Crear publicación", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(titulo, { titulo = it }, Modifier.fillMaxWidth(), enabled = disponible,
            label = { Text("Título") }, singleLine = true)
        OutlinedTextField(texto, { texto = it }, Modifier.fillMaxWidth(), enabled = disponible,
            label = { Text("Contenido") }, minLines = 3)
        Button(onClick = publicar, enabled = disponible && titulo.isNotBlank() && texto.isNotBlank()) {
            Text("Publicar")
        }
        when (val actual = estado) {
            null -> Unit
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFFE6F4E9), contentColor = Color(0xFF145329)
            ) {
                Text("Publicado con id ${actual.datos.id}", Modifier.padding(12.dp))
            }
            is UiState.Error -> if (titulo.isNotBlank() && texto.isNotBlank()) {
                ErrorConReintento(actual.mensaje, publicar)
            } else {
                Text(actual.mensaje)
            }
        }
    }
}
