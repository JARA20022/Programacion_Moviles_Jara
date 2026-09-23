package com.jara.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.ui.components.BotonMenu
import com.jara.clinicasalud.ui.components.TarjetaCita
import com.jara.clinicasalud.ui.theme.*

// Filtra las citas completadas de la misma lista compartida.
// Separar este archivo no crea otro estado ni otro NavController.

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
