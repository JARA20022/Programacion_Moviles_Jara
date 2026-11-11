package com.tecsup.productos.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun AdminScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val ocupado by viewModel.ocupado.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver al catálogo")
        }
        Text("Panel Admin", style = MaterialTheme.typography.headlineSmall)

        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                Text("Total guardado: ${actual.datos.totalGuardado}")
                Text("Total de favoritos: ${actual.datos.totalFavoritos}")
            }
        }

        Button(
            onClick = viewModel::sincronizar,
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sincronizar")
        }
        OutlinedButton(
            onClick = viewModel::limpiarCache,
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar caché")
        }
        if (ocupado) {
            Cargando()
        }
        mensaje?.let { Text(it) }
    }
}
