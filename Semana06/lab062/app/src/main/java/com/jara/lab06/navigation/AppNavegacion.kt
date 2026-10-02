package com.jara.lab06.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.jara.lab06.data.productosTienda
import com.jara.lab06.screens.DetalleProductoScreen
import com.jara.lab06.screens.InicioScreen
import com.jara.lab06.ui.components.AppDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    // El menú comienza cerrado.
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    // Permite realizar la animación de apertura y cierre.
    val scope = rememberCoroutineScope()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = rutaActual == "inicio",
        drawerContent = {
            AppDrawer(
                onSeleccionar = {
                    // En el siguiente avance conectaremos los destinos.
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // El detalle conserva su propia barra con Volver.
                if (rutaActual == "inicio") {
                    TopAppBar(
                        title = {
                            Text(
                                text = "IVAN JARA AYALA",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú principal"
                                )
                            }
                        }
                    )
                }
            }
        ) { espacio ->
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier.padding(espacio)
            ) {
                composable("inicio") {
                    InicioScreen(navController)
                }

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
    }
}