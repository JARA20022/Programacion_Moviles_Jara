package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R

@Composable
fun SplashScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    SplashVisual(
        onComenzar = onRegistrarse,
        onIniciarSesion = onIniciarSesion
    )
}

private val AzulClinica = Color(0xFF2864E8)
private val AzulOscuro = Color(0xFF142C68)

@Composable
fun SplashVisual(
    onComenzar: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFF))
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.1f))

        Box(modifier = Modifier.size(84.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(width = 42.dp, height = 78.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(AzulClinica)
            )
            Box(
                Modifier
                    .size(width = 78.dp, height = 42.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(AzulClinica)
            )
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = Color.White
            )
        }

        Spacer(Modifier.height(13.dp))
        Text(
            text = "Clínica\nSaludPlus",
            color = AzulOscuro,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 31.sp,
            lineHeight = 34.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Tu salud, nuestra prioridad",
            color = Color(0xFF65728A),
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(0.15f))
        Image(
            painter = painterResource(R.drawable.doctor_splash),
            contentDescription = "Médico de Clínica SaludPlus",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(Modifier.weight(0.05f))

        Button(
            onClick = onComenzar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulClinica)
        ) {
            Text("Comenzar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        TextButton(onClick = onIniciarSesion) {
            Text("Ya tengo una cuenta", color = AzulClinica)
        }
    }
}
