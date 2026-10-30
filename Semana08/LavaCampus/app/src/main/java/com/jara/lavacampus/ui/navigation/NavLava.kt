package com.jara.lavacampus.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.jara.lavacampus.ui.estudiante.LoginScreen
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
    val rutaActual=entradaActual?.destination?.route
    val esAdmin=usuario?.rol==Rol.ADMIN.name
    val raiz=if(esAdmin)Rutas.PANEL else Rutas.INICIO
    val destinos=if(esAdmin)listOf(
        Triple(Rutas.PANEL,"Panel","panel"),Triple(Rutas.BANDEJA,"Pedidos","pedidos"),
        Triple(Rutas.SERVICIOS,"Servicios","servicios"),Triple(Rutas.CLIENTES,"Clientes","persona"),
        Triple(Rutas.AJUSTES,"Ajustes","ajustes")
    )else listOf(
        Triple(Rutas.INICIO,"Inicio","inicio"),Triple(Rutas.MIS_PEDIDOS,"Pedidos","pedidos"),
        Triple(Rutas.TARIFAS,"Tarifas","servicios"),Triple(Rutas.PERFIL,"Perfil","persona")
    )
    val mostrarBarra=usuario!=null && destinos.any{it.first==rutaActual}
    Scaffold(modifier=modifier,containerColor=Color.Transparent,contentColor=MaterialTheme.colorScheme.onBackground,
        contentWindowInsets=WindowInsets(0,0,0,0),bottomBar={
            if(mostrarBarra) NavigationBar(windowInsets=WindowInsets(0,0,0,0)) {
                destinos.forEach { (ruta,titulo,icono) ->
                    NavigationBarItem(selected=rutaActual==ruta,onClick={
                        nav.navigate(if(ruta==Rutas.BANDEJA)Rutas.bandeja() else ruta) {
                            popUpTo(raiz){saveState=true};launchSingleTop=true;restoreState=ruta!=Rutas.BANDEJA
                        }
                    },icon={IconoBarra(icono)},label={Text(titulo)})
                }
            }
        }) { margen ->
    NavHost(navController = nav, startDestination = Rutas.SPLASH, modifier = Modifier.padding(margen).consumeWindowInsets(margen)) {
        composable(Rutas.SPLASH) { SplashScreen() }
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
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
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
        composable(Rutas.AJUSTES) {
            if (usuario?.rol == Rol.ADMIN.name) AjustesScreen(vm, onVolver = { nav.popBackStack() })
            else SplashScreen()
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
private fun IconoBarra(tipo:String) {
    val color=LocalContentColor.current
    Canvas(Modifier.size(24.dp).clearAndSetSemantics{}) {
        val u=size.minDimension/24f
        fun linea(x:Float,y:Float,a:Float,c:Float)=drawLine(color,Offset(x*u,y*u),Offset(a*u,c*u),1.8f*u,StrokeCap.Round)
        when(tipo) {
            "inicio" -> {
                val casa=Path().apply{moveTo(3*u,11*u);lineTo(12*u,3*u);lineTo(21*u,11*u);moveTo(5*u,10*u);lineTo(5*u,21*u);lineTo(19*u,21*u);lineTo(19*u,10*u)}
                drawPath(casa,color,style=Stroke(1.8f*u,cap=StrokeCap.Round))
            }
            "persona" -> {drawCircle(color,3.5f*u,Offset(12*u,7*u),style=Stroke(1.8f*u));drawArc(color,180f,180f,false,Offset(4*u,13*u),androidx.compose.ui.geometry.Size(16*u,14*u),style=Stroke(1.8f*u))}
            "ajustes" -> {for(y in listOf(6f,12f,18f))linea(3f,y,21f,y);drawCircle(color,2.2f*u,Offset(8*u,6*u));drawCircle(color,2.2f*u,Offset(16*u,12*u));drawCircle(color,2.2f*u,Offset(9*u,18*u))}
            "servicios" -> {drawRect(color,Offset(4*u,2*u),androidx.compose.ui.geometry.Size(16*u,20*u),style=Stroke(1.8f*u));drawCircle(color,5*u,Offset(12*u,14*u),style=Stroke(1.8f*u));linea(7f,6f,10f,6f)}
            "panel" -> {for(x in listOf(4f,14f))for(y in listOf(4f,14f))drawRect(color,Offset(x*u,y*u),androidx.compose.ui.geometry.Size(6*u,6*u),style=Stroke(1.8f*u))}
            else -> {drawRect(color,Offset(5*u,3*u),androidx.compose.ui.geometry.Size(14*u,18*u),style=Stroke(1.8f*u));for(y in listOf(8f,12f,16f))linea(8f,y,16f,y)}
        }
    }
}
