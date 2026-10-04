package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    val usuario = Repositorio.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Mis datos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (usuario == null) {
                    Text("No hay una sesión iniciada.")
                } else {
                    Text(
                        text = usuario.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Correo: ${usuario.correo}")
                    Text("Teléfono: ${usuario.telefono}")
                    Text(
                        "Citas registradas: " +
                                Repositorio.citasDelUsuario(usuario.id).size
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        BotonPrincipal(
            texto = "Cerrar sesión",
            onClick = onCerrarSesion
        )
    }
}