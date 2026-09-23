package com.jara.tecsupfit.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jara.tecsupfit.data.DatosFit
import com.jara.tecsupfit.model.ReservaFit
import com.jara.tecsupfit.screens.*
import com.jara.tecsupfit.ui.components.BarraInferiorFit

@Composable
fun NavegacionFit() {
    // Un único NavController gobierna el flujo y las cuatro pestañas.
    val navController = rememberNavController()
    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route ?: "inicio"
    var filtro by remember { mutableStateOf("Hoy") }
    var reservas by remember { mutableStateOf(DatosFit.reservasIniciales()) }

    val abrirPestana: (String) -> Unit = { destino ->
        if (destino != rutaActual) {
            navController.navigate(destino) {
                popUpTo("inicio") { inclusive = false }
                launchSingleTop = true
            }
        }
    }
    val barra: @Composable () -> Unit = {
        BarraInferiorFit(rutaActual = rutaActual, onDestino = abrirPestana)
    }

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            InicioFit(
                clases = DatosFit.clases, filtro = filtro, onFiltro = { filtro = it },
                onClase = { navController.navigate("detalle/$it") },
                barraInferior = barra
            )
        }
        composable(
            route = "detalle/{claseId}",
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { entradaDetalle ->
            val id = entradaDetalle.arguments?.getInt("claseId")
            val clase = DatosFit.clases.first { it.id == id }
            val ocupados = reservas.filter {
                it.clase.id == clase.id && it.estado == "Confirmada"
            }.sumOf { it.cupos }
            val disponibles = clase.cuposIniciales?.let { (it - ocupados).coerceAtLeast(0) }
            DetalleFit(
                clase = clase, disponibles = disponibles,
                onVolver = { navController.popBackStack() },
                onReservar = { cantidad ->
                    // Se comprueba también aquí, además del botón de la pantalla.
                    if (clase.dia == "Hoy" && cantidad in 1..3 &&
                        (disponibles == null || cantidad <= disponibles)) {
                        val nueva = ReservaFit(
                            id = (reservas.maxOfOrNull { it.id } ?: 0) + 1,
                            clase = clase, cupos = cantidad
                        )
                        // Asignar una lista nueva notifica el cambio a Compose.
                        reservas = reservas + nueva
                        navController.navigate("confirmacion/${nueva.id}") {
                            popUpTo("detalle/${clase.id}") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(
            route = "confirmacion/{reservaId}",
            arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
        ) { entradaConfirmacion ->
            val id = entradaConfirmacion.arguments?.getInt("reservaId")
            ConfirmacionFit(
                reserva = reservas.firstOrNull { it.id == id },
                onVerReservas = { abrirPestana("reservas") }
            )
        }
        composable("reservas") { ReservasFit(reservas, barraInferior = barra) }
        composable("rutinas") { RutinasFit(barraInferior = barra) }
        composable("perfil") { PerfilFit(DatosFit.usuario, barraInferior = barra) }
    }
}
