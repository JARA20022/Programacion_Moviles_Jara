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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jara.tecsupfit.model.ClaseFit
import com.jara.tecsupfit.ui.components.TarjetaClase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioFit(
    clases: List<ClaseFit>, filtro: String,
    onFiltro: (String) -> Unit, onClase: (Int) -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    // Todos los datos de esta demostración pertenecen a la semana del ejemplo.
    val visibles = if (filtro == "Hoy") clases.filter { it.dia == "Hoy" } else clases
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Hola, Diego", fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeFit, titleContentColor = Color.White
                )
            )
        },
        bottomBar = barraInferior
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            LazyRow(
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Hoy", "Esta semana")) { opcion ->
                    FilterChip(
                        selected = filtro == opcion, onClick = { onFiltro(opcion) },
                        label = { Text(opcion) },
                        shape = RoundedCornerShape(50), border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = FondoTarjetaFit,
                            labelColor = TextoSecundarioFit,
                            selectedContainerColor = VerdeFit,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Text("Clases disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoFit)
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(visibles, key = { it.id }) { clase ->
                    TarjetaClase(clase, mostrarDia = filtro != "Hoy", onClick = { onClase(clase.id) })
                }
            }
        }
    }
}
