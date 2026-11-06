package com.tecsup.jara

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onNavigateToCursos: () -> Unit, onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val usuario = auth.currentUser

    if (usuario == null) {
        LaunchedEffect(Unit) { onLogout() }
        return
    }

    PantallaAcademica(
        acento = TurquesaJara,
        tituloCabecera = "Bienvenido, Iván"
    ) {
        Text(
            usuario.email ?: "Usuario",
            style = MaterialTheme.typography.bodyMedium,
            color = TintaJara
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(FormaJara)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFBAF6FA), Color(0xFF44CADB))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LibrosJara(Modifier.width(170.dp).height(120.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(AzulJara)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Mis Cursos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        BotonAcademico("Ver cursos", onNavigateToCursos)

        BotonAcademico(
            texto = "Cerrar sesión",
            secundario = true,
            alPulsar = {
                auth.signOut()
                onLogout()
            }
        )
    }
}