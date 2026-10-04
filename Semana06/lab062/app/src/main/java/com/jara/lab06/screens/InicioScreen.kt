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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jara.lab06.data.categoriasTienda
import com.jara.lab06.data.productosTienda
import com.jara.lab06.ui.components.TarjetaProducto

@OptIn(ExperimentalMaterial3Api::class)
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoriasTienda) { categoria ->
                    val selected = categoriaSeleccionada == categoria
                    FilterChip(
                        selected = selected,
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = {
                            Text(
                                text = categoria,
                                fontSize = 14.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFEEE5F5),
                            selectedLabelColor = Color(0xFF5B2C83),
                            containerColor = Color(0xFFF4EFF8),
                            labelColor = Color(0xFF242128)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selected,
                            borderColor = Color(0xFFDEDEE2),
                            selectedBorderColor = Color(0xFF5B2C83)
                        )
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF242128),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
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
