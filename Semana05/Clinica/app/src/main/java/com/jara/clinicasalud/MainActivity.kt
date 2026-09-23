package com.jara.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: String,
    val experiencia: String? = null,
    val resenas: Int? = null,
    val descripcion: String? = null
)

@Composable
fun NavegacionClinica() {
    val navController = rememberNavController()

    var especialidadSeleccionada by remember {
        mutableStateOf("Cardiología")
    }

    val medicos = remember {
        listOf(
            Medico(
                id = 1,
                nombre = "Dra. Ana Torres",
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
                especialidad = "Pediatra",
                calificacion = "4.7"
            ),
            Medico(
                id = 3,
                nombre = "Dra. Rosa Díaz",
                especialidad = "Dermatóloga",
                calificacion = "4.8"
            )
        )
    }

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
                    containerColor = Color(0xFF5B2A86),
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
        "Cardiología",
        "Pediatría"
    )

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
                        containerColor = Color(0xFFF3F1F7),
                        labelColor = Color(0xFF6E6E6E),
                        selectedContainerColor = Color(0xFF5B2A86),
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
            color = Color(0xFF1E1E1E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = medicos,
                key = { it.id }
            ) { medico ->
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F1F7)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoMedico()

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medico.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = medico.especialidad,
                    fontSize = 12.sp,
                    color = Color(0xFF6E6E6E)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            CalificacionMedico(
                calificacion = medico.calificacion
            )
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
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = "←",
                            fontSize = 26.sp,
                            color = Color(0xFF1E1E1E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1E1E1E)
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
                    color = Color(0xFF1E1E1E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (medico.experiencia != null) {
                        "${medico.especialidad} · ${medico.experiencia}"
                    } else {
                        medico.especialidad
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF6E6E6E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CalificacionMedico(
                        calificacion = medico.calificacion
                    )

                    if (medico.resenas != null) {
                        Text(
                            text = "(${medico.resenas} reseñas)",
                            fontSize = 12.sp,
                            color = Color(0xFF6E6E6E)
                        )
                    }
                }

                if (medico.descripcion != null) {
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = medico.descripcion,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF1E1E1E)
                    )
                }
            }

            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5B2A86),
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

@Composable
fun CalificacionMedico(calificacion: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "★",
            fontSize = 18.sp,
            color = Color(0xFFBA8A00)
        )

        Text(
            text = calificacion,
            fontSize = 12.sp,
            color = Color(0xFF6E6E6E)
        )
    }
}

@Composable
fun IconoMedico(tamano: Dp = 44.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(
                color = Color(0xFFEEE6F7),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(tamano * 0.55f)
                .height(tamano * 0.12f)
                .background(Color(0xFF5B2A86))
        )

        Box(
            modifier = Modifier
                .width(tamano * 0.12f)
                .height(tamano * 0.55f)
                .background(Color(0xFF5B2A86))
        )
    }
}