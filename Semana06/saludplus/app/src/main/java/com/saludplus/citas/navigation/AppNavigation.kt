package com.saludplus.citas.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.AppDrawer
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.agendamiento.SedesScreen
import com.saludplus.citas.ui.screens.auth.LoginExitosoScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroExitosoScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.home.MisDoctoresScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen
import com.saludplus.citas.ui.theme.CreamBackground
import com.saludplus.citas.ui.theme.IndigoPrimary
import com.saludplus.citas.ui.theme.InkBlue
import com.saludplus.citas.ui.theme.LavenderLight
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val rutaActual = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    val usuarioActual = Repositorio.usuarioActual

    val rutasPublicas = setOf(
        Rutas.SPLASH,
        Rutas.REGISTRO,
        Rutas.REGISTRO_EXITOSO,
        Rutas.LOGIN,
        Rutas.LOGIN_EXITOSO,
        Rutas.TERMINOS
    )

    LaunchedEffect(rutaActual, usuarioActual) {
        if (
            rutaActual != null &&
            rutaActual !in rutasPublicas &&
            usuarioActual == null
        ) {
            navController.navigate(Rutas.SPLASH) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val destinosBarra = listOf(
        Rutas.HOME,
        Rutas.MIS_CITAS,
        Rutas.RESULTADOS,
        Rutas.PERFIL
    )

    val volverInicio: () -> Unit = {
        val regreso = navController.popBackStack(
            Rutas.HOME,
            false
        )

        if (!regreso) {
            navController.navigate(Rutas.HOME) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val volverDesdeLogin: () -> Unit = {
        val regreso = if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            false
        }

        if (!regreso) {
            navController.navigate(Rutas.SPLASH) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val cerrarSesionNavegar = {
        Repositorio.cerrarSesion()
        navController.navigate(Rutas.SPLASH) {
            popUpTo(navController.graph.id) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    val tituloBarraSuperior = when (rutaActual) {
        Rutas.LOGIN -> "Volver"
        Rutas.CITA_EXITOSA -> "Cita agendada"
        Rutas.MIS_CITAS -> "Agenda"
        Rutas.PERFIL -> "Mis datos"
        Rutas.RESULTADOS -> "Resultados médicos"
        else -> null
    }

    AppDrawer(
        drawerState = drawerState,
        scope = scope,
        rutaActual = rutaActual,
        onNavegar = { ruta ->
            navController.navigate(ruta) {
                popUpTo(Rutas.HOME)
                launchSingleTop = true
            }
        },
        onCerrarSesion = cerrarSesionNavegar
    ) {
        Scaffold(
            containerColor = CreamBackground,
            topBar = {
                tituloBarraSuperior?.let { titulo ->
                    TopAppBar(
                        title = {
                            Text(
                                text = titulo,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (rutaActual == Rutas.LOGIN) {
                                        volverDesdeLogin()
                                    } else {
                                        scope.launch { drawerState.open() }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (rutaActual == Rutas.LOGIN) {
                                        Icons.AutoMirrored.Filled.ArrowBack
                                    } else {
                                        Icons.Default.Menu
                                    },
                                    contentDescription = if (rutaActual == Rutas.LOGIN) {
                                        "Volver"
                                    } else {
                                        "Abrir menú"
                                    }
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = CreamBackground,
                            titleContentColor = InkBlue,
                            navigationIconContentColor = InkBlue
                        )
                    )
                }
            },
            bottomBar = {
                if (rutaActual in destinosBarra) {
                    BarraPrincipal(
                        rutaActual = rutaActual,
                        onDestino = { ruta ->
                            navController.navigate(ruta) {
                                popUpTo(Rutas.HOME)
                                launchSingleTop = true
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
                            navController.navigate(Rutas.REGISTRO_EXITOSO) {
                                popUpTo(Rutas.REGISTRO) {
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

                composable(Rutas.REGISTRO_EXITOSO) {
                    RegistroExitosoScreen(
                        onNavegarLogin = {
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.REGISTRO_EXITOSO) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable(Rutas.LOGIN) {
                    LoginScreen(
                        onLoginExitoso = {
                            navController.navigate(Rutas.LOGIN_EXITOSO) {
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

                composable(Rutas.LOGIN_EXITOSO) {
                    LoginExitosoScreen(
                        onNavegarHome = {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.LOGIN_EXITOSO) {
                                    inclusive = true
                                }
                            }
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
                        onAbrirMenu = {
                            scope.launch { drawerState.open() }
                        },
                        onSedes = {
                            navController.navigate(Rutas.SEDES)
                        },
                        onMisDoctores = {
                            navController.navigate(Rutas.MIS_DOCTORES)
                        },
                        onResultados = {
                            navController.navigate(Rutas.RESULTADOS)
                        },
                        onPerfil = {
                            navController.navigate(Rutas.PERFIL)
                        },
                        onEspecialidad = { especialidadId ->
                            navController.navigate(
                                Rutas.medicos(especialidadId)
                            )
                        },
                        onNotificaciones = {
                            navController.navigate(Rutas.NOTIFICACIONES)
                        },
                        onAgendarCita = {
                            if (Repositorio.sedeSeleccionada == null) {
                                navController.navigate(Rutas.SEDES)
                            } else {
                                navController.navigate(Rutas.ESPECIALIDADES)
                            }
                        }
                    )
                }

                composable(Rutas.SEDES) {
                    SedesScreen(
                        onVolver = {
                            navController.popBackStack()
                        },
                        onSeleccionarSede = {
                            navController.navigate(Rutas.ESPECIALIDADES)
                        }
                    )
                }

                composable(Rutas.MIS_DOCTORES) {
                    MisDoctoresScreen(
                        onVolver = {
                            navController.popBackStack()
                        },
                        onSeleccionarMedico = { medicoId ->
                            navController.navigate(
                                Rutas.fechaHora(medicoId)
                            )
                        }
                    )
                }

                composable(Rutas.ESPECIALIDADES) {
                    EspecialidadesScreen(
                        onVolver = {
                            navController.popBackStack()
                        },
                        onSeleccionarEspecialidad = { especialidadId ->
                            navController.navigate(
                                Rutas.medicos(especialidadId)
                            )
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
                        onVolver = {
                            navController.popBackStack()
                        },
                        onSeleccionarMedico = { medicoId ->
                            navController.navigate(
                                Rutas.fechaHora(medicoId)
                            )
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
                        onVolver = {
                            navController.popBackStack()
                        },
                        onContinuar = { fecha, hora ->
                            navController.navigate(
                                Rutas.confirmarCita(
                                    medicoId,
                                    fecha,
                                    hora
                                )
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
                        onVolver = {
                            navController.popBackStack()
                        },
                        onCitaAgendada = { citaId ->
                            navController.navigate(
                                Rutas.citaExitosa(citaId)
                            ) {
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
                        onIrInicio = volverInicio
                    )
                }

                composable(Rutas.MIS_CITAS) {
                    MisCitasScreen(
                        onAgendarCita = {
                            if (Repositorio.sedeSeleccionada == null) {
                                navController.navigate(Rutas.SEDES)
                            } else {
                                navController.navigate(Rutas.ESPECIALIDADES)
                            }
                        }
                    )
                }

                composable(Rutas.RESULTADOS) {
                    ResultadosScreen()
                }

                composable(Rutas.PERFIL) {
                    PerfilScreen(
                        onCerrarSesion = cerrarSesionNavegar
                    )
                }

                composable(Rutas.NOTIFICACIONES) {
                    NotificacionesScreen(
                        onVolver = {
                            navController.popBackStack()
                        }
                    )
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
        Triple(Rutas.MIS_CITAS, "Agenda", Icons.Default.EventNote),
        Triple(Rutas.RESULTADOS, "Resultados", Icons.Default.Assessment),
        Triple(Rutas.PERFIL, "Mis datos", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = SurfaceWhite
    ) {
        destinos.forEach { (ruta, nombre, icono) ->
            NavigationBarItem(
                selected = rutaActual == ruta,
                onClick = {
                    onDestino(ruta)
                },
                icon = {
                    Icon(
                        imageVector = icono,
                        contentDescription = nombre
                    )
                },
                label = {
                    Text(
                        text = nombre,
                        fontWeight = if (rutaActual == ruta) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = IndigoPrimary,
                    selectedTextColor = IndigoPrimary,
                    indicatorColor = LavenderLight,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
