package com.jara.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jara.clinicasalud.ui.theme.ClinicaSaludTheme
import kotlinx.coroutines.launch

// Colores compartidos por las pantallas.
private val MoradoClinica = Color(0xFF5B2A86)
private val FondoTarjeta = Color(0xFFF3F1F7)
private val FondoIcono = Color(0xFFEEE6F7)
private val TextoPrincipal = Color(0xFF1E1E1E)
private val TextoSecundario = Color(0xFF6E6E6E)
private val DoradoEstrella = Color(0xFFBA8A00)
private val VerdeConfirmacion = Color(0xFF1D9E75)
private val FondoConfirmacion = Color(0xFFE1F5EE)
private val FondoCompletada = Color(0xFFE6E6E6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClinicaSaludTheme {
                NavegacionClinica()
            }
        }
    }
}

// Modelos: representan los datos, no dibujan la interfaz.
data class Medico(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val especialidad: String,
    val calificacion: String,
    val experiencia: String? = null,
    val resenas: Int? = null,
    val descripcion: String? = null
)

data class FechaCita(
    val dia: String,
    val numero: String,
    val diaCompleto: String
)

data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)

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
        "historial"
    )

    var especialidadSeleccionada by remember {
        mutableStateOf("Todas")
    }

    val medicos = remember {
        listOf(
            Medico(
                id = 1,
                nombre = "Dra. Ana Torres",
                categoria = "Cardiología",
                especialidad = "Cardióloga",
                calificacion = "4.9",
                experiencia = "12 años exp.",
                resenas = 128,
                descripcion = "Especialista en arritmias e hipertensión, " +
                        "formación en la Clínica Mayo."
            ),
            Medico(
                id = 2,
                nombre = "Dr. Luis Vega",
                categoria = "Pediatría",
                especialidad = "Pediatra",
                calificacion = "4.7"
            ),
            Medico(
                id = 3,
                nombre = "Dra. Rosa Díaz",
                categoria = "Dermatología",
                especialidad = "Dermatóloga",
                calificacion = "4.8"
            )
        )
    }

    // Estado compartido. Las citas nuevas se añaden a esta lista.
    // La cita completada inicial reproduce el ejemplo del Word.
    var citas by remember {
        mutableStateOf(
            listOf(
                Cita(
                    id = 1,
                    medico = medicos.first { it.id == 2 },
                    fecha = "Miércoles 15",
                    hora = "3:00 pm",
                    estado = "Completada"
                )
            )
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuLateral(
    rutaActual: String,
    onDestinoSeleccionado: (String) -> Unit
) {
    // Cada elemento relaciona una ruta con su texto visible.
    val destinos = listOf(
        "inicio" to "Inicio",
        "mis_citas" to "Mis citas",
        "historial" to "Historial médico"
    )

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Datos de presentación tomados del diseño de referencia.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(FondoIcono, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JP",
                        color = MoradoClinica,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Juan Pérez",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Paciente",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }
            }

            HorizontalDivider(color = FondoCompletada)

            Spacer(Modifier.height(16.dp))

            destinos.forEach { (ruta, titulo) ->
                val seleccionado = rutaActual == ruta

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = titulo,
                            fontWeight = if (seleccionado) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    icon = {
                        Text(
                            text = "○",
                            fontSize = 26.sp
                        )
                    },
                    selected = seleccionado,
                    onClick = {
                        onDestinoSeleccionado(ruta)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = FondoIcono,
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = MoradoClinica,
                        unselectedTextColor = TextoPrincipal,
                        selectedIconColor = MoradoClinica,
                        unselectedIconColor = TextoPrincipal
                    )
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioClinica(
    navController: NavController,
    medicos: List<Medico>,
    especialidadSeleccionada: String,
    onEspecialidadSeleccionada: (String) -> Unit,
    onAbrirMenu: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BotonMenu(
                        onClick = onAbrirMenu,
                        color = Color.White
                    )
                },
                title = {
                    Column {
                        Text(
                            text = "Clínica Salud+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hola, Juan",
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoClinica,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        ContenidoInicio(
            navController = navController,
            medicos = medicos,
            especialidadSeleccionada = especialidadSeleccionada,
            onEspecialidadSeleccionada = onEspecialidadSeleccionada,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoInicio(
    navController: NavController,
    medicos: List<Medico>,
    especialidadSeleccionada: String,
    onEspecialidadSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val especialidades = listOf(
        "Todas",
        "Cardiología",
        "Pediatría",
        "Dermatología"
    )

    // Filtra por categoría; Todas conserva la lista completa.
    val medicosFiltrados = if (especialidadSeleccionada == "Todas") {
        medicos
    } else {
        medicos.filter { it.categoria == especialidadSeleccionada }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(especialidades, key = { it }) { especialidad ->
                FilterChip(
                    selected = especialidadSeleccionada == especialidad,
                    onClick = {
                        onEspecialidadSeleccionada(especialidad)
                    },
                    label = {
                        Text(especialidad, fontSize = 12.sp)
                    },
                    shape = RoundedCornerShape(50),
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = FondoTarjeta,
                        labelColor = TextoSecundario,
                        selectedContainerColor = MoradoClinica,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Médicos disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(medicosFiltrados, key = { it.id }) { medico ->
                TarjetaMedico(
                    medico = medico,
                    onClick = {
                        navController.navigate("perfil/${medico.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaMedico(
    medico: Medico,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoMedico()

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = medico.especialidad,
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.width(8.dp))

            CalificacionMedico(medico.calificacion)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedico(
    navController: NavController,
    medico: Medico
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil del médico",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonRegresar {
                        navController.popBackStack()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconoMedico(tamano = 88.dp)

                Spacer(Modifier.height(12.dp))

                Text(
                    text = medico.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = if (medico.experiencia != null) {
                        "${medico.especialidad} · ${medico.experiencia}"
                    } else {
                        medico.especialidad
                    },
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CalificacionMedico(medico.calificacion)

                    if (medico.resenas != null) {
                        Text(
                            text = "(${medico.resenas} reseñas)",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                    }
                }

                if (medico.descripcion != null) {
                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = medico.descripcion,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoPrincipal
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("agenda/${medico.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoClinica,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Agendar cita",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaCita(
    navController: NavController,
    medico: Medico,
    onConfirmar: (String, String) -> Unit
) {
    val fechas = listOf(
        FechaCita("Jue", "26", "Jueves"),
        FechaCita("Vie", "27", "Viernes"),
        FechaCita("Sáb", "28", "Sábado")
    )

    val horas = listOf("9:00", "10:30", "3:00")

    // Una variable por grupo garantiza una selección única.
    var fechaSeleccionada by remember(medico.id) {
        mutableStateOf("27")
    }
    var horaSeleccionada by remember(medico.id) {
        mutableStateOf("10:30")
    }
    var confirmando by remember(medico.id) {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agendar cita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonRegresar {
                        navController.popBackStack()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Selecciona fecha",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fechas.forEach { fecha ->
                        OpcionAgenda(
                            texto = "${fecha.dia}\n${fecha.numero}",
                            seleccionada = fechaSeleccionada == fecha.numero,
                            onClick = {
                                fechaSeleccionada = fecha.numero
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Selecciona hora",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    horas.forEach { hora ->
                        OpcionAgenda(
                            texto = hora,
                            seleccionada = horaSeleccionada == hora,
                            onClick = {
                                horaSeleccionada = hora
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (!confirmando) {
                        confirmando = true

                        val fecha = fechas.first {
                            it.numero == fechaSeleccionada
                        }

                        val fechaCompleta =
                            "${fecha.diaCompleto} ${fecha.numero}"

                        val horaCompleta = if (horaSeleccionada == "3:00") {
                            "$horaSeleccionada pm"
                        } else {
                            "$horaSeleccionada am"
                        }

                        onConfirmar(fechaCompleta, horaCompleta)
                    }
                },
                enabled = !confirmando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoClinica,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar cita",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionCita(
    cita: Cita?,
    onVolverInicio: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirmación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cita != null) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(FondoConfirmacion, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 48.sp,
                        color = VerdeConfirmacion
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "¡Cita agendada!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = cita.medico.nombre,
                    fontSize = 15.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 14.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "La cita ya no está disponible en esta sesión.",
                    fontSize = 16.sp,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onVolverInicio,
                modifier = Modifier
                    .widthIn(min = 180.dp)
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FondoTarjeta,
                    contentColor = TextoPrincipal
                )
            ) {
                Text("Volver al inicio", fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitas(
    citas: List<Cita>,
    onAbrirMenu: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis citas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonMenu(onClick = onAbrirMenu)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (citas.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no tienes citas registradas.",
                        color = TextoSecundario
                    )
                }
            } else {
                // Las últimas citas registradas aparecen primero.
                items(citas.asReversed(), key = { it.id }) { cita ->
                    TarjetaCita(cita)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialMedico(
    citas: List<Cita>,
    onAbrirMenu: () -> Unit
) {
    // Consulta los mismos datos; no crea una lista independiente.
    val completadas = citas.filter { it.estado == "Completada" }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historial médico",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    BotonMenu(onClick = onAbrirMenu)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextoPrincipal
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Citas completadas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
            }

            if (completadas.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no tienes citas completadas.",
                        color = TextoSecundario
                    )
                }
            } else {
                items(completadas.asReversed(), key = { it.id }) { cita ->
                    TarjetaCita(cita)
                }
            }
        }
    }
}

@Composable
fun TarjetaCita(cita: Cita) {
    val confirmada = cita.estado == "Confirmada"
    val fondoEstado = if (confirmada) FondoConfirmacion else FondoCompletada
    val textoEstado = if (confirmada) VerdeConfirmacion else TextoSecundario

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // IntrinsicSize.Min ajusta la franja a la altura del contenido.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        if (confirmada) MoradoClinica else Color.Transparent
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = cita.medico.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(8.dp))

                // Etiqueta de estado: verde para Confirmada y gris para Completada.
                Text(
                    text = cita.estado,
                    modifier = Modifier
                        .background(fondoEstado, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = textoEstado
                )
            }
        }
    }
}

@Composable
fun BotonMenu(
    onClick: () -> Unit,
    color: Color = TextoPrincipal
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "Abrir menú"
        }
    ) {
        Text(
            text = "☰",
            fontSize = 26.sp,
            color = color
        )
    }
}

@Composable
fun BotonRegresar(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "Regresar"
        }
    ) {
        Text(
            text = "←",
            fontSize = 26.sp,
            color = TextoPrincipal
        )
    }
}

@Composable
fun OpcionAgenda(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = if (seleccionada) MoradoClinica else FondoTarjeta
    val colorTexto = if (seleccionada) Color.White else TextoPrincipal

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(fondo)
            .selectable(
                selected = seleccionada,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = if (seleccionada) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = colorTexto,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CalificacionMedico(calificacion: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "★",
            fontSize = 18.sp,
            color = DoradoEstrella
        )
        Text(
            text = calificacion,
            fontSize = 12.sp,
            color = TextoSecundario
        )
    }
}

@Composable
fun IconoMedico(tamano: Dp = 44.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(FondoIcono, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(tamano * 0.55f)
                .height(tamano * 0.12f)
                .background(MoradoClinica)
        )
        Box(
            modifier = Modifier
                .width(tamano * 0.12f)
                .height(tamano * 0.55f)
                .background(MoradoClinica)
        )
    }
}