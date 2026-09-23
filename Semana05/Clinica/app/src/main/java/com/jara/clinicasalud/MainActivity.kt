package com.jara.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jara.clinicasalud.ui.theme.ClinicaSaludTheme

// Colores compartidos por las pantallas.
// Cambiar uno aquí modifica todos los componentes que lo utilizan.
private val MoradoClinica = Color(0xFF5B2A86)
private val FondoTarjeta = Color(0xFFF3F1F7)
private val FondoIcono = Color(0xFFEEE6F7)
private val TextoPrincipal = Color(0xFF1E1E1E)
private val TextoSecundario = Color(0xFF6E6E6E)
private val DoradoEstrella = Color(0xFFBA8A00)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // setContent inicia la interfaz creada con Jetpack Compose.
        setContent {
            ClinicaSaludTheme {
                NavegacionClinica()
            }
        }
    }
}

// Representa los datos de un médico.
// categoria se utiliza para filtrar; especialidad se muestra en su tarjeta.
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

// Separa el nombre corto del día y su número para mostrarlos en dos líneas.
data class FechaCita(
    val dia: String,
    val numero: String
)

@Composable
fun NavegacionClinica() {
    // Un solo controlador administra los cambios de pantalla.
    val navController = rememberNavController()

    // El estado se conserva mientras este composable permanece en composición.
    // Al modificarlo, Compose actualiza la interfaz que lo utiliza.
    var especialidadSeleccionada by remember {
        mutableStateOf("Todas")
    }

    // Los datos son compartidos por Inicio y Perfil.
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

    // NavHost contiene las rutas. startDestination es la pantalla inicial.
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable(route = "inicio") {
            InicioClinica(
                navController = navController,
                medicos = medicos,
                especialidadSeleccionada = especialidadSeleccionada,
                onEspecialidadSeleccionada = {
                    especialidadSeleccionada = it
                }
            )
        }

        // medicoId es el parámetro que identifica al médico seleccionado.
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

        // La agenda recibe el mismo ID enviado desde Perfil.
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
                medico = medico
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioClinica(
    navController: NavController,
    medicos: List<Medico>,
    especialidadSeleccionada: String,
    onEspecialidadSeleccionada: (String) -> Unit
) {
    // Scaffold organiza la barra superior y el contenido de esta pantalla.
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
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
        // Este padding evita que el contenido quede debajo de la barra.
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

    // Este bloque realiza el filtrado real de la lista.
    // "Todas" devuelve la lista completa.
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
        // LazyRow organiza los chips horizontalmente.
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = especialidades,
                key = { it }
            ) { especialidad ->
                FilterChip(
                    selected = especialidadSeleccionada == especialidad,
                    onClick = {
                        onEspecialidadSeleccionada(especialidad)
                    },
                    label = {
                        Text(
                            text = especialidad,
                            fontSize = 12.sp
                        )
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Médicos disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal
        )

        Spacer(modifier = Modifier.height(12.dp))

        // LazyColumn muestra únicamente los médicos del filtro actual.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = medicosFiltrados,
                key = { it.id }
            ) { medico ->
                TarjetaMedico(
                    medico = medico,
                    onClick = {
                        // Ejemplo: al tocar a Luis se navega a perfil/2.
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
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjeta
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        // Row coloca icono, datos y calificación uno al lado del otro.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoMedico()

            Spacer(modifier = Modifier.width(12.dp))

            // weight ocupa el espacio disponible entre icono y calificación.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = medico.especialidad,
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

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
                    IconButton(
                        onClick = {
                            // Quita la pantalla actual y vuelve a la anterior.
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = "←",
                            fontSize = 26.sp,
                            color = TextoPrincipal
                        )
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = medico.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (medico.experiencia != null) {
                        "${medico.especialidad} · ${medico.experiencia}"
                    } else {
                        medico.especialidad
                    },
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(modifier = Modifier.height(4.dp))

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

                // Solo se muestra la descripción si está registrada.
                if (medico.descripcion != null) {
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = medico.descripcion,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoPrincipal
                    )
                }
            }

            // Ahora el botón está habilitado y abre la agenda del médico.
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
    medico: Medico
) {
    // Opciones tomadas de la imagen de referencia.
    val fechas = listOf(
        FechaCita("Jue", "26"),
        FechaCita("Vie", "27"),
        FechaCita("Sáb", "28")
    )

    val horas = listOf("9:00", "10:30", "3:00")

    // Una sola variable guarda la fecha elegida y otra la hora.
    // Los valores iniciales coinciden con la selección de la referencia.
    // medico.id reinicia estas selecciones si cambia el médico.
    var fechaSeleccionada by remember(medico.id) {
        mutableStateOf("27")
    }

    var horaSeleccionada by remember(medico.id) {
        mutableStateOf("10:30")
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
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = "←",
                            fontSize = 26.sp,
                            color = TextoPrincipal
                        )
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
            // El contenido puede desplazarse si la pantalla es pequeña.
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

                Spacer(modifier = Modifier.height(12.dp))

                // Agrupa las fechas como opciones de selección única.
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

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Selecciona hora",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(modifier = Modifier.height(12.dp))

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

            // La confirmación y el registro de la cita son el siguiente avance.
            // Permanece deshabilitado para no simular una reserva inexistente.
            Button(
                onClick = {},
                enabled = false,
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

@Composable
fun OpcionAgenda(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // La selección determina los colores de la opción.
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
    // Box superpone dos rectángulos para formar la cruz.
    Box(
        modifier = Modifier
            .size(tamano)
            .background(
                color = FondoIcono,
                shape = CircleShape
            ),
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