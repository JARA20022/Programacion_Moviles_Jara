package com.jara.lavacampus.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jara.lavacampus.ui.components.IconoStudio
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
import com.jara.lavacampus.viewmodel.ServiciosViewModel
import com.jara.lavacampus.viewmodel.ClientesViewModel
import com.jara.lavacampus.ui.administrador.ServiciosScreen
import com.jara.lavacampus.ui.administrador.FormularioServicioScreen
import com.jara.lavacampus.ui.administrador.ClientesScreen
import com.jara.lavacampus.ui.administrador.AjustesScreen
import com.jara.lavacampus.ui.administrador.SoportesScreen
import com.jara.lavacampus.viewmodel.SoportesViewModel
import com.jara.lavacampus.ui.estudiante.PerfilScreen
import com.jara.lavacampus.viewmodel.BandejaViewModel
import com.jara.lavacampus.viewmodel.GestionPedidoViewModel
import com.jara.lavacampus.ui.administrador.BandejaScreen
import com.jara.lavacampus.ui.administrador.GestionPedidoScreen
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
import com.jara.lavacampus.ui.estudiante.SplashScreen
import com.jara.lavacampus.ui.estudiante.SoporteInicioScreen
import com.jara.lavacampus.ui.administrador.SolicitudesSoporteScreen
import com.jara.lavacampus.ui.administrador.RespuestaSolicitudScreen
import com.jara.lavacampus.viewmodel.SolicitudesSoporteViewModel
import com.jara.lavacampus.ui.estudiante.LoginScreen
import com.jara.lavacampus.ui.administrador.SoportePerfilScreen
import com.jara.lavacampus.viewmodel.SesionViewModel

@Composable
fun NavLava(vm: SesionViewModel, modifier: Modifier = Modifier) {
    val app = LocalContext.current.applicationContext as LavaApp
    val nav = rememberNavController()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val restaurando by vm.restaurando.collectAsStateWithLifecycle()
    val recibirPedidos by vm.recibirPedidos.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    val entradaActual by nav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val esAdmin = usuario?.rol == Rol.ADMIN.name
    val esSoporte = usuario?.rol == Rol.SOPORTE.name
    
    val raiz = when {
        esAdmin -> Rutas.PANEL
        esSoporte -> Rutas.INICIO_SOPORTE
        else -> Rutas.INICIO
    }
    
    val destinos = when {
        esAdmin -> listOf(
            Triple(Rutas.PANEL, "Panel", "panel"), 
            Triple(Rutas.BANDEJA, "Pedidos", "pedidos"),
            Triple(Rutas.SERVICIOS, "Servicios", "servicios"), 
            Triple(Rutas.CLIENTES, "Clientes", "persona"), 
            Triple(Rutas.AJUSTES, "Ajustes", "ajustes")
        )
        esSoporte -> listOf(
            Triple(Rutas.INICIO_SOPORTE, "Inicio", "inicio"),
            Triple(Rutas.SOLICITUDES, "Solicitudes", "pedidos"),
            Triple(Rutas.SOPORTE_PERFIL, "Perfil", "persona")
        )
        else -> listOf(
            Triple(Rutas.INICIO, "Inicio", "inicio"), 
            Triple(Rutas.MIS_PEDIDOS, "Pedidos", "pedidos"),
            Triple(Rutas.TARIFAS, "Tarifas", "servicios"), 
            Triple(Rutas.PERFIL, "Perfil", "persona")
        )
    }
    val mostrarBarra = usuario != null && destinos.any { it.first == rutaActual }
    Scaffold(modifier=modifier,containerColor=Color.Transparent,contentColor=MaterialTheme.colorScheme.onBackground,contentWindowInsets=WindowInsets(0,0,0,0),bottomBar={
        if(mostrarBarra) NavigationBar(
            modifier=Modifier.padding(horizontal=12.dp,vertical=8.dp).clip(RoundedCornerShape(26.dp)),
            containerColor=MaterialTheme.colorScheme.surface,tonalElevation=0.dp,windowInsets=WindowInsets(0,0,0,0)
        ) {
            destinos.forEach { (ruta,titulo,icono) ->
                NavigationBarItem(selected=rutaActual==ruta,onClick={
                    val destino=if(ruta==Rutas.BANDEJA)Rutas.bandeja() else ruta
                    nav.navigate(destino) {
                        popUpTo(raiz) { saveState = true }
                        launchSingleTop = true
                        restoreState = ruta != Rutas.BANDEJA
                    }
                },icon={IconoStudio(icono)},label={Text(titulo)},
                colors=NavigationBarItemDefaults.colors(indicatorColor=MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor=MaterialTheme.colorScheme.onPrimaryContainer,selectedTextColor=MaterialTheme.colorScheme.primary))
            }
        }
    }) { margen ->
    NavHost(navController = nav, startDestination = Rutas.SPLASH, modifier = Modifier.padding(margen).consumeWindowInsets(margen),
        enterTransition={fadeIn(tween(260))+slideInHorizontally(tween(280)){it/12}},
        exitTransition={fadeOut(tween(160))},popEnterTransition={fadeIn(tween(220))},
        popExitTransition={fadeOut(tween(160))+slideOutHorizontally(tween(220)){it/12}}) {
        composable(Rutas.SPLASH) { SplashScreen() }
        composable(Rutas.INICIO_SOPORTE) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.SOPORTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app) }
                val servicios: ServiciosViewModel = viewModel(key = "servicios-soporte-${actual.id}", factory = factory)
                SoporteInicioScreen(vm = vm, serviciosVm = servicios)
            } else {
                SplashScreen()
            }
        }
        composable(Rutas.SOLICITUDES) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.SOPORTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val solicitudes: SolicitudesSoporteViewModel = viewModel(key = "solicitudes-${actual.id}", factory = factory)
                SolicitudesSoporteScreen(vm = solicitudes, onVolver = { nav.popBackStack() }, onAtender = { id -> nav.navigate(Rutas.respuestaSolicitud(id)) })
            } else { SplashScreen() }
        }
        composable(Rutas.RESPUESTA_SOLICITUD, arguments = listOf(navArgument("detalleId") { type = NavType.IntType })) { entrada ->
            val actual = usuario
            if (actual != null && actual.rol == Rol.SOPORTE.name) {
                val detalleId = requireNotNull(entrada.arguments).getInt("detalleId")
                val factory = remember(actual.id, detalleId) { LavaViewModelFactory(app, actual) }
                val solicitudes: SolicitudesSoporteViewModel = viewModel(key = "respuesta-${actual.id}", factory = factory)
                RespuestaSolicitudScreen(vm = solicitudes, detalleId = detalleId, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.LOGIN) { LoginScreen(vm) }

        composable(Rutas.INICIO) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val inicio: InicioViewModel = viewModel(key = "inicio-${actual.id}", factory = factory)
                InicioScreen(
                    vm = inicio,
                    onPerfil = { nav.navigate(Rutas.PERFIL) },
                    recibirPedidos = recibirPedidos,
                    onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                    onMisPedidos = { nav.navigate(Rutas.MIS_PEDIDOS) },
                    onTarifas = { nav.navigate(Rutas.TARIFAS) },
                    onDetalle = { id -> nav.navigate(Rutas.detallePedido(id)) },
                    onSalir = vm::cerrarSesion,
                    cerrandoSesion = cargando,
                    errorSesion = error
                )
            } else {
                SplashScreen()
            }
        }
        composable(Rutas.NUEVO_PEDIDO,arguments=listOf(navArgument("origen"){type=NavType.IntType;defaultValue=0})) { entrada ->
            val origen=entrada.arguments?.getInt("origen")?.takeIf{it>0}
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id,origen) { LavaViewModelFactory(app, actual, pedidoOrigenId=origen) }
                val nuevo: NuevoPedidoViewModel = viewModel(key = "nuevo-${actual.id}-$origen", factory = factory)
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
                    onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                    onDetalle = { id -> nav.navigate(Rutas.detallePedido(id)) },
                    onTarifas = { nav.navigate(Rutas.TARIFAS) },
                    onRepetir = { id -> nav.navigate(Rutas.nuevoPedido(id)) }
                )
            } else {
                SplashScreen()
            }
        }
        composable(Rutas.TARIFAS) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val tarifas: MisPedidosViewModel = viewModel(key = "tarifas-${actual.id}", factory = factory)
                TarifasScreen(vm = tarifas, onVolver = { nav.popBackStack() })
            } else {
                SplashScreen()
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
                SplashScreen()
            }
        }
        composable(
            Rutas.BANDEJA,
            arguments = listOf(navArgument("estado") { type = NavType.StringType; defaultValue = "TODOS" })
        ) { entrada ->
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val filtro = entrada.arguments?.getString("estado") ?: "TODOS"
                val factory = remember(actual.id, filtro) { LavaViewModelFactory(app, filtroInicial = filtro) }
                val bandeja: BandejaViewModel = viewModel(key = "bandeja-${actual.id}-$filtro", factory = factory)
                BandejaScreen(vm = bandeja, onVolver = { nav.popBackStack() }, onGestion = { id -> nav.navigate(Rutas.gestionPedido(id)) })
            } else { SplashScreen() }
        }
        composable(
            Rutas.GESTION_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val id = requireNotNull(entrada.arguments).getInt("id")
                val factory = remember(actual.id, id) { LavaViewModelFactory(app, pedidoId = id) }
                val gestion: GestionPedidoViewModel = viewModel(key = "gestion-${actual.id}-$id", factory = factory)
                GestionPedidoScreen(vm = gestion, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.PERFIL) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.CLIENTE.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val inicio: InicioViewModel = viewModel(key = "perfil-${actual.id}", factory = factory)
                PerfilScreen(sesion = vm, inicio = inicio, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.SOPORTE_PERFIL) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.SOPORTE.name) {
                SoportePerfilScreen(vm = vm, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.AJUSTES) {
            if (usuario?.rol == Rol.ADMIN.name) AjustesScreen(vm, onVolver = { nav.popBackStack() }, onSoportes = { nav.navigate(Rutas.SOPORTES) })
            else SplashScreen()
        }
        composable(Rutas.SOPORTES) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app, actual) }
                val soportes: SoportesViewModel = viewModel(key = "soportes-${actual.id}", factory = factory)
                SoportesScreen(vm = soportes, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.SERVICIOS) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app) }
                val servicios: ServiciosViewModel = viewModel(key = "servicios-${actual.id}", factory = factory)
                ServiciosScreen(
                    vm = servicios, onVolver = { nav.popBackStack() },
                    onNuevo = { nav.navigate(Rutas.formularioServicio()) },
                    onEditar = { id -> nav.navigate(Rutas.formularioServicio(id)) }
                )
            } else { SplashScreen() }
        }
        composable(Rutas.FORMULARIO_SERVICIO, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val id = requireNotNull(entrada.arguments).getInt("id")
                val factory = remember(actual.id, id) { LavaViewModelFactory(app, servicioId = id) }
                val servicio: ServiciosViewModel = viewModel(key = "formulario-${actual.id}-$id", factory = factory)
                FormularioServicioScreen(vm = servicio, esEdicion = id != 0, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.CLIENTES) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app) }
                val clientes: ClientesViewModel = viewModel(key = "clientes-${actual.id}", factory = factory)
                ClientesScreen(vm = clientes, onVolver = { nav.popBackStack() })
            } else { SplashScreen() }
        }
        composable(Rutas.PANEL) {
            val actual = usuario
            if (actual != null && actual.rol == Rol.ADMIN.name) {
                val factory = remember(actual.id) { LavaViewModelFactory(app) }
                val panel: PanelViewModel = viewModel(key = "panel-${actual.id}", factory = factory)
                PanelScreen(
                    vm = panel,
                    onSalir = vm::cerrarSesion,
                    onBandeja = { nav.navigate(Rutas.bandeja()) },
                    onVerListos = { nav.navigate(Rutas.bandeja("LISTO")) },
                    onServicios = { nav.navigate(Rutas.SERVICIOS) },
                    onAtrasados = { nav.navigate(Rutas.bandeja("ATRASADOS")) },
                    onClientes = { nav.navigate(Rutas.CLIENTES) },
                    onAjustes = { nav.navigate(Rutas.AJUSTES) },
                    cerrandoSesion = cargando,
                    errorSesion = error
                )
            } else {
                SplashScreen()
            }
        }
    }

    }

    LaunchedEffect(restaurando, usuario?.id, usuario?.rol) {
        if (!restaurando) {
            val destino = when (usuario?.rol) {
                Rol.CLIENTE.name -> Rutas.INICIO
                Rol.ADMIN.name -> Rutas.PANEL
                Rol.SOPORTE.name -> Rutas.INICIO_SOPORTE
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
