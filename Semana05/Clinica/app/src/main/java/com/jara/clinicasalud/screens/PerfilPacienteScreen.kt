package com.jara.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.clinicasalud.ui.components.BotonMenu
import com.jara.clinicasalud.ui.theme.*

// Perfil del paciente del drawer. No es el perfil del médico elegido en Inicio.
// El Word muestra estos datos en el encabezado del menú, pero no proporciona
// una maqueta interior para esta pantalla de la clínica. Se reutiliza su estilo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilPaciente(onAbrirMenu: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    // Abre el mismo drawer compartido por las otras secciones.
                    BotonMenu(onClick = onAbrirMenu)
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
                // Respeta la barra superior y los espacios del Scaffold.
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MoradoClinica
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Solo se muestran los datos de la referencia, sin inventar campos.
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Juan Pérez",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Paciente",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                    }
                }
            }
        }
    }
}
