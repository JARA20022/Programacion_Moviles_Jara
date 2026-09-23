package com.jara.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.ui.theme.ClinicaSaludTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClinicaSaludTheme {
                InicioClinica()
            }
        }
    }
}

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioClinica() {
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoInicio(modifier: Modifier = Modifier) {
    val especialidades = listOf(
        "Cardiología",
        "Pediatría"
    )

    var especialidadSeleccionada by remember {
        mutableStateOf("Cardiología")
    }

    val medicos = remember {
        listOf(
            Medico(
                id = 1,
                nombre = "Dra. Ana Torres",
                especialidad = "Cardióloga",
                calificacion = "4.9"
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
                        especialidadSeleccionada = especialidad
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
                TarjetaMedico(medico = medico)
            }
        }
    }
}

@Composable
fun TarjetaMedico(medico: Medico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp),
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
                    text = medico.calificacion,
                    fontSize = 12.sp,
                    color = Color(0xFF6E6E6E)
                )
            }
        }
    }
}

@Composable
fun IconoMedico() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = Color(0xFFEEE6F7),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(5.dp)
                .background(Color(0xFF5B2A86))
        )

        Box(
            modifier = Modifier
                .width(5.dp)
                .height(24.dp)
                .background(Color(0xFF5B2A86))
        )
    }
}