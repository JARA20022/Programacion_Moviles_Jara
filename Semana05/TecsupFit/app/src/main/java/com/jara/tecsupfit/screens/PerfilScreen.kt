package com.jara.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.tecsupfit.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import com.jara.tecsupfit.model.UsuarioFit
import com.jara.tecsupfit.ui.components.EstadisticaFit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFit(usuario: UsuarioFit, barraInferior: @Composable () -> Unit) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = TextoFit)
            )
        },
        bottomBar = barraInferior
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier.size(88.dp).background(FondoVerdeFit, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(usuario.iniciales, color = VerdeFit, fontWeight = FontWeight.Bold, fontSize = 24.sp) }
            Spacer(Modifier.height(12.dp))
            Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextoFit)
            Text(usuario.plan, fontSize = 13.sp, color = TextoSecundarioFit)
            Spacer(Modifier.height(32.dp))
            // Son los valores históricos del ejemplo: reservar no equivale a asistir.
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                EstadisticaFit(usuario.clasesTomadas, "Clases", Modifier.weight(1f))
                EstadisticaFit(usuario.rachas, "Rachas", Modifier.weight(1f))
            }
        }
    }
}
