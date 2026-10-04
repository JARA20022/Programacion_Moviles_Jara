package com.saludplus.citas.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val entradaActual = navController.currentBackStackEntryAsState().value
    val rutaActual = entradaActual?.destination?.route

    val destinosBarra = listOf(
        Rutas.HOME,
        Rutas.MIS_CITAS,
        Rutas.RESULTADOS,
        Rutas.PERFIL
    )

    Scaffold(
        bottomBar = {
            if (rutaActual in destinosBarra) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.HOME,
                        onClick = {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Home, contentDescription = null)
                        },
                        label = { Text("Inicio") }
                    )

                    NavigationBarItem(
                        selected = rutaActual == Rutas.MIS_CITAS,
                        onClick = {
                            navController.navigate(Rutas.MIS_CITAS) {
                                popUpTo(Rutas.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(Icons.Default.EventNote, contentDescription = null)
                        },
                        label = { Text("Citas") }
                    )

                    NavigationBarItem(
                        selected = rutaActual == Rutas.RESULTADOS,
                        onClick = {
                            navController.navigate(Rutas.RESULTADOS) {
                                popUpTo(Rutas.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Assessment, contentDescription = null)
                        },
                        label = { Text("Resultados") }
                    )

                    NavigationBarItem(
                        selected = rutaActual == Rutas.PERFIL,
                        onClick = {
                            navController.navigate(Rutas.PERFIL) {
                                popUpTo(Rutas.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        label = { Text("Perfil") }
                    )
                }
            }
        }
    ) { espacio ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(espacio)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(
                    onRegistrarse = {
                        navController.navigate(Rutas.REGISTRO)
                    },
                    onIniciarSesion = {
                        navController.navigate(Rutas.LOGIN)
                    }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onRegistroExitoso = {
                        navController.navigate(Rutas.HOME) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    },
                    onIniciarSesion = {
                        navController.navigate(Rutas.LOGIN)
                    },
                    onVerTerminos = {
                        navController.navigate(Rutas.TERMINOS)
                    }
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    onLoginExitoso = {
                        navController.navigate(Rutas.HOME) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    },
                    onRegistrarse = {
                        navController.navigate(Rutas.REGISTRO)
                    }
                )
            }

            composable(Rutas.TERMINOS) {
                TerminosScreen(
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.HOME) {
                HomeScreen(
                    onAgendarCita = {
                        navController.navigate(Rutas.ESPECIALIDADES)
                    },
                    onMisCitas = {
                        navController.navigate(Rutas.MIS_CITAS)
                    },
                    onPerfil = {
                        navController.navigate(Rutas.PERFIL)
                    },
                    onResultados = {
                        navController.navigate(Rutas.RESULTADOS)
                    },
                    onEspecialidad = { especialidadId ->
                        navController.navigate(Rutas.medicos(especialidadId))
                    },
                    onNotificaciones = {
                        navController.navigate(Rutas.NOTIFICACIONES)
                    }
                )
            }

            composable(Rutas.ESPECIALIDADES) {
                DestinoTemporal("Especialidades") {
                    navController.popBackStack()
                }
            }

            composable(Rutas.MEDICOS) {
                DestinoTemporal("Médicos") {
                    navController.popBackStack()
                }
            }

            composable(Rutas.MIS_CITAS) {
                DestinoTemporal("Mis citas") {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            }

            composable(Rutas.RESULTADOS) {
                DestinoTemporal("Resultados") {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            }

            composable(Rutas.PERFIL) {
                DestinoTemporal("Perfil") {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            }

            composable(Rutas.NOTIFICACIONES) {
                DestinoTemporal("Notificaciones") {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
private fun DestinoTemporal(
    titulo: String,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Volver a Inicio",
            onClick = onVolver
        )
    }
}