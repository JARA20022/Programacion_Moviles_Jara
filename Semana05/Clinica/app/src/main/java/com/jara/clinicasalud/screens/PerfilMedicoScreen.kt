package com.jara.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jara.clinicasalud.model.Medico
import com.jara.clinicasalud.ui.components.BotonRegresar
import com.jara.clinicasalud.ui.components.CalificacionMedico
import com.jara.clinicasalud.ui.components.IconoMedico
import com.jara.clinicasalud.ui.theme.*

// Recibe el médico resuelto por la ruta perfil/{medicoId}.
// Agendar cita envía el mismo ID a la pantalla de agenda.

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
                    BotonRegresar {
                        navController.popBackStack()
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

                Spacer(Modifier.height(12.dp))

                Text(
                    text = medico.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = if (medico.experiencia != null) {
                        "${medico.especialidad} · ${medico.experiencia}"
                    } else {
                        medico.especialidad
                    },
                    fontSize = 13.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(4.dp))

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

                if (medico.descripcion != null) {
                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = medico.descripcion,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = TextoPrincipal
                    )
                }
            }

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

