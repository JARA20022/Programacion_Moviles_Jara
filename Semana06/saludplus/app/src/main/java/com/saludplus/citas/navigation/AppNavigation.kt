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
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen

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
            InicioTemporal(
                onCerrarSesion = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun InicioTemporal(onCerrarSesion: () -> Unit) {
    val nombre = Repositorio.usuarioActual?.nombre ?: "Paciente"

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido, $nombre",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        Text("Clínica SaludPlus")

        Spacer(Modifier.height(32.dp))

        BotonPrincipal(
            texto = "Cerrar sesión",
            onClick = onCerrarSesion
        )
    }
}