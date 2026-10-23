package com.jara.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.jara.lavacampus.LavaApp
import com.jara.lavacampus.ui.estudiante.InicioScreen
import com.jara.lavacampus.viewmodel.InicioViewModel
import com.jara.lavacampus.viewmodel.LavaViewModelFactory
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jara.lavacampus.data.local.entity.Usuario
import com.jara.lavacampus.model.Rol
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.ui.estudiante.LoginScreen
import com.jara.lavacampus.ui.theme.colorDelRol
import com.jara.lavacampus.viewmodel.SesionViewModel

@Composable
fun NavLava(vm: SesionViewModel, modifier: Modifier = Modifier) {
    val app = LocalContext.current.applicationContext as LavaApp
    val nav = rememberNavController()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val restaurando by vm.restaurando.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    NavHost(navController = nav, startDestination = Rutas.SPLASH, modifier = modifier) {
        composable(Rutas.SPLASH) { SplashSesion() }
        composable(Rutas.LOGIN) { LoginScreen(vm) }

        composable(Rutas.INICIO) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val inicio: InicioViewModel = viewModel(key = "inicio-${actual.id}", factory = factory)
                InicioScreen(
                    vm = inicio,
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    onMisPedidos = { nav.navigate(Rutas.MIS_PEDIDOS) },
                    onTarifas = { nav.navigate(Rutas.TARIFAS) },
                    onDetalle = { id -> nav.navigate(Rutas.detallePedido(id)) },
                    onSalir = vm::cerrarSesion,
                    cerrandoSesion = cargando,
                    errorSesion = error
                )
            } else {
                SplashSesion()
            }
        }
        // Los destinos se registran ahora; sus pantallas se completan
        // en los pasos 8, 9 y 10, manteniendo un commit por paso.
        composable(Rutas.NUEVO_PEDIDO) {
            DestinoSiguiente("Nuevo pedido") { nav.popBackStack() }
        }
        composable(Rutas.MIS_PEDIDOS) {
            DestinoSiguiente("Mis pedidos") { nav.popBackStack() }
        }
        composable(Rutas.TARIFAS) {
            DestinoSiguiente("Tarifas") { nav.popBackStack() }
        }
        composable(
            route = Rutas.DETALLE_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = requireNotNull(entrada.arguments).getInt("id")
            DestinoSiguiente("Detalle del pedido #$id") { nav.popBackStack() }
        }
        composable(Rutas.PANEL) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                DestinoSesion("Panel", actual, cargando, error, vm::cerrarSesion)
            } else {
                SplashSesion()
            }
        }
    }

    LaunchedEffect(restaurando, usuario?.id, usuario?.rol) {
        if (!restaurando) {
            val destino = when (usuario?.rol) {
                Rol.CLIENTE.name -> Rutas.INICIO
                Rol.ADMIN.name -> Rutas.PANEL
                else -> Rutas.LOGIN
            }
            if (nav.currentDestination?.route != destino) {
                nav.navigate(destino) {
                    popUpTo(nav.graph.id) { inclusive = false }
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
private fun SplashSesion() {
    Column(
        Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LAVACAMPUS", style = MaterialTheme.typography.headlineMedium)
        CircularProgressIndicator()
        Text("Preparando tu espacio")
    }
}

@Composable
private fun DestinoSesion(
    titulo: String,
    usuario: Usuario,
    cargando: Boolean,
    error: String?,
    onSalir: () -> Unit
) {
    val rol = Rol.valueOf(usuario.rol)
    Column(
        Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically)
    ) {
        Text(
            titulo.uppercase(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            "Hola, ${usuario.nombre}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(rol.titulo, style = MaterialTheme.typography.titleMedium)
        Text(usuario.correo)
        if (usuario.codigo.isNotBlank()) Text("Código: ${usuario.codigo}")
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        LavaButton("Cerrar sesión", onSalir, colorDelRol(rol), cargando = cargando)
    }
}

// Estructura de navegación para las pantallas de los siguientes pasos.
@Composable
private fun DestinoSiguiente(titulo: String, onVolver: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        androidx.compose.material3.TextButton(onClick = onVolver) {
            Text("Volver al inicio")
        }
    }
}
