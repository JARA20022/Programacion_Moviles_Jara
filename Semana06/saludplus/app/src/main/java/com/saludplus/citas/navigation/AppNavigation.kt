package com.saludplus.citas.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
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
                        popUpTo(Rutas.SPLASH) {
                            inclusive = true
                        }
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
                        popUpTo(Rutas.SPLASH) {
                            inclusive = true
                        }
                    }
                },
                onRegistrarse = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.TERMINOS) {
            TerminosScreen(
                onVolver = {
                    navController.popBackStack()
                }
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
            DestinoTemporal(
                titulo = "Especialidades",
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.MEDICOS) {
            DestinoTemporal(
                titulo = "Médicos",
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.MIS_CITAS) {
            DestinoTemporal(
                titulo = "Mis citas",
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.RESULTADOS) {
            DestinoTemporal(
                titulo = "Resultados",
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.PERFIL) {
            DestinoTemporal(
                titulo = "Perfil",
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.NOTIFICACIONES) {
            DestinoTemporal(
                titulo = "Notificaciones",
                onVolver = { navController.popBackStack() }
            )
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