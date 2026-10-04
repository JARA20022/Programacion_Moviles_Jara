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
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val rutaActual = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    val destinosBarra = listOf(
        Rutas.HOME,
        Rutas.MIS_CITAS,
        Rutas.RESULTADOS,
        Rutas.PERFIL
    )

    Scaffold(
        bottomBar = {
            if (rutaActual in destinosBarra) {
                BarraPrincipal(
                    rutaActual = rutaActual,
                    onDestino = { ruta ->
                        navController.navigate(ruta) {
                            popUpTo(Rutas.HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
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
                EspecialidadesScreen(
                    onVolver = { navController.popBackStack() },
                    onSeleccionarEspecialidad = { especialidadId ->
                        navController.navigate(Rutas.medicos(especialidadId))
                    }
                )
            }

            composable(Rutas.MEDICOS) { entrada ->
                val especialidadId = entrada.arguments
                    ?.getString("especialidadId")
                    ?.toIntOrNull()
                    ?: -1

                MedicosScreen(
                    especialidadId = especialidadId,
                    onVolver = { navController.popBackStack() },
                    onSeleccionarMedico = { medicoId ->
                        navController.navigate(Rutas.fechaHora(medicoId))
                    }
                )
            }

            composable(Rutas.FECHA_HORA) { entrada ->
                val medicoId = entrada.arguments
                    ?.getString("medicoId")
                    ?.toIntOrNull()
                    ?: -1

                FechaHoraScreen(
                    medicoId = medicoId,
                    onVolver = { navController.popBackStack() },
                    onContinuar = { fecha, hora ->
                        navController.navigate(
                            Rutas.confirmarCita(medicoId, fecha, hora)
                        )
                    }
                )
            }

            composable(Rutas.CONFIRMAR_CITA) { entrada ->
                val medicoId = entrada.arguments
                    ?.getString("medicoId")
                    ?.toIntOrNull()
                    ?: -1
                val fecha = entrada.arguments
                    ?.getString("fecha")
                    .orEmpty()
                val hora = entrada.arguments
                    ?.getString("hora")
                    .orEmpty()

                ConfirmarCitaScreen(
                    medicoId = medicoId,
                    fecha = fecha,
                    hora = hora,
                    onVolver = { navController.popBackStack() },
                    onCitaAgendada = { citaId ->
                        navController.navigate(Rutas.citaExitosa(citaId)) {
                            popUpTo(Rutas.HOME)
                        }
                    }
                )
            }

            composable(Rutas.CITA_EXITOSA) { entrada ->
                val citaId = entrada.arguments
                    ?.getString("citaId")
                    ?.toIntOrNull()
                    ?: -1

                CitaExitosaScreen(
                    citaId = citaId,
                    onVerMisCitas = {
                        navController.navigate(Rutas.MIS_CITAS) {
                            popUpTo(Rutas.HOME)
                        }
                    },
                    onIrInicio = {
                        navController.popBackStack(Rutas.HOME, false)
                    }
                )
            }

            composable(Rutas.MIS_CITAS) {
                DestinoTemporal("Mis citas") {
                    navController.popBackStack()
                }
            }

            composable(Rutas.RESULTADOS) {
                DestinoTemporal("Resultados") {
                    navController.popBackStack()
                }
            }

            composable(Rutas.PERFIL) {
                DestinoTemporal("Perfil") {
                    navController.popBackStack()
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
private fun BarraPrincipal(
    rutaActual: String?,
    onDestino: (String) -> Unit
) {
    val destinos = listOf(
        Triple(Rutas.HOME, "Inicio", Icons.Default.Home),
        Triple(Rutas.MIS_CITAS, "Citas", Icons.Default.EventNote),
        Triple(Rutas.RESULTADOS, "Resultados", Icons.Default.Assessment),
        Triple(Rutas.PERFIL, "Perfil", Icons.Default.Person)
    )

    NavigationBar {
        destinos.forEach { (ruta, nombre, icono) ->
            NavigationBarItem(
                selected = rutaActual == ruta,
                onClick = { onDestino(ruta) },
                icon = {
                    Icon(icono, contentDescription = nombre)
                },
                label = { Text(nombre) }
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
            texto = "Volver",
            onClick = onVolver
        )
    }
}