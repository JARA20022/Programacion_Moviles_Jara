package com.tecsup.productos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.productos.AppContainer
import com.tecsup.productos.ui.detalle.DetalleScreen
import com.tecsup.productos.ui.detalle.DetalleViewModel
import com.tecsup.productos.ui.lista.ListaScreen
import com.tecsup.productos.ui.lista.ListaViewModel

private const val CATALOGO = "catalogo"
private const val DETALLE = "detalle"
private const val ID = "id"

@Composable
fun AppNav(contenedor: AppContainer, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CATALOGO,
        modifier = modifier
    ) {
        composable(CATALOGO) {
            val listaViewModel: ListaViewModel = viewModel(factory = contenedor.listaFactory)
            ListaScreen(
                viewModel = listaViewModel,
                onAbrirDetalle = { id -> navController.navigate("$DETALLE/$id") }
            )
        }
        composable(
            route = "$DETALLE/{$ID}",
            arguments = listOf(navArgument(ID) { type = NavType.IntType })
        ) { entrada ->
            val id = requireNotNull(entrada.arguments).getInt(ID)
            val detalleViewModel: DetalleViewModel = viewModel(
                factory = contenedor.detalleFactory(id)
            )
            DetalleScreen(
                viewModel = detalleViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
