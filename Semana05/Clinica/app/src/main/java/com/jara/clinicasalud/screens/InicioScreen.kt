package com.jara.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jara.clinicasalud.model.Medico
import com.jara.clinicasalud.ui.components.BotonMenu
import com.jara.clinicasalud.ui.components.TarjetaMedico
import com.jara.clinicasalud.ui.theme.*

// Inicio dibuja su Scaffold y delega el listado a ContenidoInicio.
// El filtro llega por parámetros; el estado sigue en NavegacionClinica.

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

