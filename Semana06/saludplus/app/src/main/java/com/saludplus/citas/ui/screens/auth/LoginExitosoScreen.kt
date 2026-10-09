package com.saludplus.citas.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.SurfaceSecondary
import com.saludplus.citas.ui.theme.TextSecondary
import com.saludplus.citas.ui.theme.WarmAccent
import kotlinx.coroutines.delay

@Composable
fun LoginExitosoScreen(
    onNavegarHome: () -> Unit
) {
    val scale = remember { Animatable(0.5f) }
    val nombreUsuario = Repositorio.usuarioActual?.nombre?.trim()?.substringBefore(" ") ?: "a SaludPlus"

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
        delay(1500)
        onNavegarHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .scale(scale.value)
                .size(96.dp)
                .background(color = SurfaceSecondary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Bienvenido",
                tint = WarmAccent,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Bienvenido, $nombreUsuario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PetroleumPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Inicio de sesión correcto",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
