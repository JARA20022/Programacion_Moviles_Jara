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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.jara.tecsupfit.model.ReservaFit
import com.jara.tecsupfit.ui.components.BotonAtrasFit
import com.jara.tecsupfit.ui.components.TarjetaReserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasFit(
    reservas: List<ReservaFit>,
    barraInferior: @Composable () -> Unit = {},
    onVolver: (() -> Unit)? = null
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Mis reservas", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { onVolver?.let { BotonAtrasFit(it) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = TextoFit)
            )
        },
        bottomBar = barraInferior
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (reservas.isEmpty()) {
                item { Text("Todavía no tienes reservas.", color = TextoSecundarioFit) }
            }
            items(reservas.asReversed(), key = { it.id }) { reserva -> TarjetaReserva(reserva) }
        }
    }
}
