package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.CoralAction
import com.saludplus.citas.ui.theme.CreamBackground
import com.saludplus.citas.ui.theme.IndigoPrimary
import com.saludplus.citas.ui.theme.InkBlue
import com.saludplus.citas.ui.theme.LavenderLight
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary

@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    val usuario = Repositorio.usuarioActual
    val totalCitas = usuario?.let { Repositorio.citasDelUsuario(it.id).size } ?: 0

    val iniciales = usuario?.nombre
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.take(2)
        ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
        ?.joinToString("") ?: "P"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Mis Datos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlue
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(LavenderLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = usuario?.nombre ?: "Paciente",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(Modifier.height(10.dp))

                ItemDatoPerfil(
                    icono = Icons.Default.Email,
                    etiqueta = "Correo electrónico",
                    valor = usuario?.correo ?: "No registrado"
                )

                ItemDatoPerfil(
                    icono = Icons.Default.Phone,
                    etiqueta = "Teléfono celular",
                    valor = usuario?.telefono ?: "No registrado"
                )

                ItemDatoPerfil(
                    icono = Icons.Default.EventNote,
                    etiqueta = "Citas registradas",
                    valor = "$totalCitas cita(s) agendada(s)"
                )
            }
        }

        Spacer(Modifier.weight(1f))

        BotonPrincipal(
            texto = "Cerrar sesión",
            onClick = onCerrarSesion,
            containerColor = CoralAction
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ItemDatoPerfil(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LavenderLight, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = IndigoPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = etiqueta,
                fontSize = 11.sp,
                color = TextSecondary
            )

            Text(
                text = valor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
