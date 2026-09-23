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

// Muestra en LazyColumn las citas del estado compartido.
// Los colores de cada estado los dibuja el componente TarjetaCita.

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

