package com.jara.clinicasalud.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jara.clinicasalud.data.DatosClinica
import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.screens.*
import com.jara.clinicasalud.ui.components.MenuLateral
import kotlinx.coroutines.launch

// Administra las rutas, el drawer y el estado de la sesión.
// El estado sigue utilizando remember/mutableStateOf, sin ViewModel.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavegacionClinica() {
    val navController = rememberNavController()

    // Estado del menú y alcance para ejecutar su apertura y cierre.
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    // Observamos la ruta para resaltar el destino activo del menú.
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: "inicio"

    val seccionesPrincipales = listOf(
        "inicio",
        "mis_citas",
        "historial",
        "perfil_paciente"
    )

    var especialidadSeleccionada by remember {
        mutableStateOf("Todas")
    }

    // La lista de ejemplo se obtiene ahora de DatosClinica.
    val medicos = remember {
        DatosClinica.medicos
    }

    // El estado continúa aquí: no se trasladó al objeto de datos.
    // La lista inicial contiene la cita completada del ejemplo del Word.
    var citas by remember {
        mutableStateOf(DatosClinica.citasIniciales())
    }

    val abrirMenu: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    // El drawer envuelve el NavHost y, dentro de él, los Scaffold.
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = rutaActual in seccionesPrincipales,
        drawerContent = {
            MenuLateral(
                rutaActual = rutaActual,
                onDestinoSeleccionado = { ruta ->
                    scope.launch {
                        drawerState.close()

                        if (ruta != rutaActual) {
                            navController.navigate(ruta) {
                                // Mantiene Inicio como base de navegación.
                                popUpTo("inicio") {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") {
                InicioClinica(
                    navController = navController,
                    medicos = medicos,
                    especialidadSeleccionada = especialidadSeleccionada,
                    onEspecialidadSeleccionada = {
                        especialidadSeleccionada = it
                    },
                    onAbrirMenu = abrirMenu
                )
            }

            composable(
                route = "perfil/{medicoId}",
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId")
                val medico = medicos.first { it.id == medicoId }

                PerfilMedico(
                    navController = navController,
                    medico = medico
                )
            }

            composable(
                route = "agenda/{medicoId}",
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId")
                val medico = medicos.first { it.id == medicoId }

                AgendaCita(
                    navController = navController,
                    medico = medico,
                    onConfirmar = { fecha, hora ->
                        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1

                        val nuevaCita = Cita(
                            id = nuevoId,
                            medico = medico,
                            fecha = fecha,
                            hora = hora
                        )

                        // Una lista nueva notifica el cambio a Compose.
                        citas = citas + nuevaCita

                        navController.navigate("confirmacion/$nuevoId") {
                            popUpTo("agenda/${medico.id}") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = "confirmacion/{citaId}",
                arguments = listOf(
                    navArgument("citaId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val citaId = backStackEntry.arguments?.getInt("citaId")
                val cita = citas.firstOrNull { it.id == citaId }

                ConfirmacionCita(
                    cita = cita,
                    onVolverInicio = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Navegación secundaria: destinos accesibles desde el drawer.
            composable("mis_citas") {
                MisCitas(
                    citas = citas,
                    onAbrirMenu = abrirMenu
                )
            }

            // Perfil del paciente: destino del drawer, distinto del perfil médico.
            composable("perfil_paciente") {
                PerfilPaciente(onAbrirMenu = abrirMenu)
            }

            composable("historial") {
                HistorialMedico(
                    citas = citas,
                    onAbrirMenu = abrirMenu
                )
            }
        }
    }

    // Si el menú está abierto, Atrás lo cierra antes de cambiar de pantalla.
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
}

