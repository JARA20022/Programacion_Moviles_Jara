package com.jara.lab06.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.jara.lab06.data.favoritosTienda
import com.jara.lab06.data.productosTienda
import com.jara.lab06.screens.DetalleProductoScreen
import com.jara.lab06.screens.FavoritosScreen
import com.jara.lab06.screens.InicioScreen
import com.jara.lab06.screens.PedidosScreen
import com.jara.lab06.screens.PerfilScreen
import com.jara.lab06.ui.components.AppDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val esDetalle = rutaActual == "detalle/{productoId}"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !esDetalle,
        drawerContent = {
            AppDrawer(
                cantidadFavoritos = favoritosTienda.size,
                rutaActual = rutaActual,
                onSeleccionar = { destino ->
                    // Cambiamos de pantalla sin repetir el mismo destino.
                    navController.navigate(destino) {
                        popUpTo("inicio") {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // El detalle mantiene su propia barra con Volver.
                if (!esDetalle) {
                    TopAppBar(
                        title = {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "IVAN JARA AYALA",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Más vendidos",
                                    fontSize = 14.sp,
                                    color = Color(0xFFDCCCE8)
                                )
                            }
                        },
                        navigationIcon = {
                            Box(
                                modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Abrir menú principal",
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF5B2C83),
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        ),
                        modifier = Modifier.heightIn(min = 80.dp)
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

                composable("pedidos") {
                    PedidosScreen()
                }

                composable("favoritos") {
                    FavoritosScreen(navController)
                }

                composable("perfil") {
                    PerfilScreen()
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
