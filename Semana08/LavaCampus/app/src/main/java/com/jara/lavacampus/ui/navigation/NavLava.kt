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
import com.jara.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.jara.lavacampus.viewmodel.NuevoPedidoViewModel
import com.jara.lavacampus.viewmodel.PanelViewModel
import com.jara.lavacampus.ui.administrador.PanelScreen
import com.jara.lavacampus.viewmodel.MisPedidosViewModel
import com.jara.lavacampus.ui.estudiante.MisPedidosScreen
import com.jara.lavacampus.ui.estudiante.TarifasScreen
import com.jara.lavacampus.viewmodel.DetallePedidoViewModel
import com.jara.lavacampus.ui.estudiante.DetallePedidoScreen
import com.jara.lavacampus.viewmodel.InicioViewModel
import com.jara.lavacampus.viewmodel.LavaViewModelFactory
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jara.lavacampus.model.Rol
import com.jara.lavacampus.ui.estudiante.LoginScreen
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
        composable(Rutas.NUEVO_PEDIDO) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val nuevo: NuevoPedidoViewModel = viewModel(key = "nuevo-${actual.id}", factory = factory)
                NuevoPedidoScreen(
                    vm = nuevo,
                    onVolver = { nav.popBackStack() },
                    onCreado = {
                        nav.navigate(Rutas.MIS_PEDIDOS) {
                            popUpTo(Rutas.NUEVO_PEDIDO) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable(Rutas.MIS_PEDIDOS) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val pedidos: MisPedidosViewModel = viewModel(key = "pedidos-${actual.id}", factory = factory)
                MisPedidosScreen(
                    vm = pedidos,
                    onVolver = { nav.popBackStack() },
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    onDetalle = { id -> nav.navigate(Rutas.detallePedido(id)) },
                    onTarifas = { nav.navigate(Rutas.TARIFAS) }
                )
            } else {
                SplashSesion()
            }
        }
        composable(Rutas.TARIFAS) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val tarifas: MisPedidosViewModel = viewModel(key = "tarifas-${actual.id}", factory = factory)
                TarifasScreen(vm = tarifas, onVolver = { nav.popBackStack() })
            } else {
                SplashSesion()
            }
        }
        composable(
            route = Rutas.DETALLE_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = requireNotNull(entrada.arguments).getInt("id")
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id, id) { LavaViewModelFactory(app, actual, id) }
                val detalle: DetallePedidoViewModel = viewModel(key = "detalle-${actual.id}-$id", factory = factory)
                DetallePedidoScreen(vm = detalle, onVolver = { nav.popBackStack() })
            } else {
                SplashSesion()
            }
        }
        composable(Rutas.PANEL) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app) }
                val panel: PanelViewModel = viewModel(key = "panel-${actual.id}", factory = factory)
                PanelScreen(
                    vm = panel,
                    onSalir = vm::cerrarSesion,
                    cerrandoSesion = cargando,
                    errorSesion = error
                )
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
