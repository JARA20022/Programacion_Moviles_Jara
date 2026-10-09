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
import androidx.compose.material.icons.filled.Check
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
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.SuccessGreen
import com.saludplus.citas.ui.theme.SurfaceSecondary
import com.saludplus.citas.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun RegistroExitosoScreen(
    onNavegarLogin: () -> Unit
) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        delay(1500)
        onNavegarLogin()
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
                imageVector = Icons.Default.Check,
                contentDescription = "Registro Exitoso",
                tint = SuccessGreen,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Registro exitoso",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PetroleumPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Tu cuenta fue creada correctamente.\nAhora inicia sesión para continuar.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
