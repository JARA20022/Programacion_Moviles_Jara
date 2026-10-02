package com.jara.lab06.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jara.lab06.data.productosTienda
import com.jara.lab06.screens.DetalleProductoScreen
import com.jara.lab06.screens.InicioScreen

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            InicioScreen(navController)
        }

        // Recibimos el número del producto seleccionado.
        composable(
            route = "detalle/{productoId}",
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { entrada ->
            val productoId = entrada.arguments?.getInt("productoId")
            val producto = productosTienda.find {
                it.id == productoId
            }

            DetalleProductoScreen(
                navController = navController,
                producto = producto
            )
        }
    }
}