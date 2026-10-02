package com.jara.lab06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jara.lab06.data.favoritosTienda
import com.jara.lab06.data.productosTienda
import com.jara.lab06.ui.components.TarjetaProducto

@Composable
fun FavoritosScreen(navController: NavHostController) {
    val productosFavoritos = productosTienda.filter {
        it.id in favoritosTienda
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Mis favoritos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        if (productosFavoritos.isEmpty()) {
            item {
                Text(
                    "Aún no tienes favoritos. " +
                            "Agrégalos desde los tres puntos de un producto."
                )
            }
        }

        items(
            items = productosFavoritos,
            key = { it.id }
        ) { producto ->
            TarjetaProducto(
                producto = producto,
                onVerDetalle = {
                    navController.navigate("detalle/${producto.id}")
                }
            )
        }
    }
}