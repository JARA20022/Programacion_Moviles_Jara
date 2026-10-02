package com.jara.lab06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jara.lab06.data.categoriasTienda
import com.jara.lab06.data.productosTienda
import com.jara.lab06.ui.components.TarjetaProducto

@Composable
fun InicioScreen(navController: NavHostController) {
    var categoriaSeleccionada by remember {
        mutableStateOf("Todas")
    }

    val productosVisibles = productosTienda.filter {
        categoriaSeleccionada == "Todas" ||
                it.categoria == categoriaSeleccionada
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Bienvenido a mi tienda",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Encuentra productos para tus clases.",
                    color = Color.Gray
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoriasTienda) { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = {
                            Text(categoria)
                        }
                    )
                }
            }
        }

        categoriasTienda.filter { it != "Todas" }.forEach { seccion ->
            val productosSeccion = productosVisibles.filter {
                it.categoria == seccion
            }

            if (productosSeccion.isNotEmpty()) {
                item {
                    Text(
                        text = seccion,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = productosSeccion,
                    key = { it.id }
                ) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onVerDetalle = {
                            navController.navigate(
                                "detalle/${producto.id}"
                            )
                        }
                    )
                }
            }
        }
    }
}