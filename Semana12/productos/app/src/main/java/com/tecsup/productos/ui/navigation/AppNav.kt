package com.tecsup.productos.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.productos.AppContainer
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.admin.AdminScreen
import com.tecsup.productos.ui.admin.AdminViewModel
import com.tecsup.productos.ui.login.LoginScreen
import com.tecsup.productos.ui.login.LoginViewModel
import com.tecsup.productos.ui.detalle.DetalleScreen
import com.tecsup.productos.ui.detalle.DetalleViewModel
import com.tecsup.productos.ui.favoritos.FavoritosScreen
import com.tecsup.productos.ui.favoritos.FavoritosViewModel
import com.tecsup.productos.ui.lista.ListaScreen
import com.tecsup.productos.ui.lista.ListaViewModel

private const val CATALOGO = "catalogo"
private const val DETALLE = "detalle"
private const val FAVORITOS = "favoritos"
private const val ADMIN = "admin"
private const val ID = "id"

@Composable
fun AppNav(contenedor: AppContainer, modifier: Modifier = Modifier) {
    val loginViewModel: LoginViewModel = viewModel(factory = contenedor.loginFactory)
    val estado by loginViewModel.estado.collectAsStateWithLifecycle()
    val sesion = when (val actual = estado) {
        is UiState.Exito -> actual.datos
        else -> null
    }

    if (sesion == null) {
        LoginScreen(viewModel = loginViewModel, modifier = modifier)
        return
    }

    val esAdmin = sesion.rol == Rol.ADMIN
    key(sesion.uid, sesion.rol) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = CATALOGO,
            modifier = modifier,
            enterTransition = {
                fadeIn(tween(240)) + slideInHorizontally(tween(280)) { it / 9 }
            },
            exitTransition = { fadeOut(tween(150)) },
            popEnterTransition = { fadeIn(tween(210)) },
            popExitTransition = {
                fadeOut(tween(160)) + slideOutHorizontally(tween(210)) { it / 9 }
            }
        ) {
            composable(CATALOGO) {
                val listaViewModel: ListaViewModel = viewModel(factory = contenedor.listaFactory)
                ListaScreen(
                    viewModel = listaViewModel,
                    onAbrirDetalle = { id -> navController.navigate("$DETALLE/$id") },
                    onAbrirFavoritos = { navController.navigate(FAVORITOS) { launchSingleTop = true } },
                    onCerrarSesion = loginViewModel::cerrarSesion,
                    onAbrirAdmin = if (esAdmin) {
                        { navController.navigate(ADMIN) { launchSingleTop = true } }
                    } else {
                        null
                    }
                )
            }
            if (esAdmin) {
                composable(ADMIN) {
                    val adminViewModel: AdminViewModel = viewModel(
                        factory = contenedor.adminFactory(sesion)
                    )
                    AdminScreen(
                        viewModel = adminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
            composable(FAVORITOS) {
                val favoritosViewModel: FavoritosViewModel = viewModel(
                    factory = contenedor.favoritosFactory
                )
                FavoritosScreen(
                    viewModel = favoritosViewModel,
                    onVolver = { navController.popBackStack() },
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
}
