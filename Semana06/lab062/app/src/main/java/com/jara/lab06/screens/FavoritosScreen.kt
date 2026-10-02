package com.jara.lab06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Mis favoritos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF242128)
            )
        }

        if (productosFavoritos.isEmpty()) {
            item {
                Text(
                    text = "Aún no tienes favoritos. " +
                            "Agrégalos desde los tres puntos de un producto.",
                    color = Color(0xFF665176),
                    fontSize = 14.sp
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
