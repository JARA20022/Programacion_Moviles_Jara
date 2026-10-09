package com.saludplus.citas.ui.screens.auth

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.OnPrimary
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.PetroleumVariant
import com.saludplus.citas.ui.theme.SurfaceSecondary
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import com.saludplus.citas.ui.theme.WarmAccent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    var mostrarIntroAnimada by rememberSaveable { mutableStateOf(true) }

    if (mostrarIntroAnimada) {
        IntroAnimada(
            onAnimacionCompletada = {
                mostrarIntroAnimada = false
            }
        )
    } else {
        SplashVisual(
            onComenzar = onRegistrarse,
            onIniciarSesion = onIniciarSesion
        )
    }
}

@Composable
private fun IntroAnimada(
    onAnimacionCompletada: () -> Unit
) {
    val scaleLogo = remember { Animatable(0.92f) }
    val alphaLogo = remember { Animatable(0f) }
    val alphaTexto = remember { Animatable(0f) }
    val anchoLinea = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 0-400 ms: Logo fade in y escala
        scaleLogo.animateTo(1.0f, tween(400))
        alphaLogo.animateTo(1.0f, tween(400))

        // Siguientes 350 ms: Texto y línea pulso
        alphaTexto.animateTo(1.0f, tween(350))
        anchoLinea.animateTo(120f, tween(350))

        delay(450)
        onAnimacionCompletada()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PetroleumPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .scale(scaleLogo.value)
                    .alpha(alphaLogo.value)
                    .size(88.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(width = 44.dp, height = 82.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(PetroleumVariant)
                )
                Box(
                    Modifier
                        .size(width = 82.dp, height = 44.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(PetroleumVariant)
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = WarmAccent
                )
            }

            Spacer(Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(alphaTexto.value)
            ) {
                Text(
                    text = "Clínica SaludPlus",
                    color = OnPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Tu salud, nuestra prioridad",
                    color = SurfaceSecondary,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .width(anchoLinea.value.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(WarmAccent)
                )
            }
        }
    }
}

@Composable
fun SplashVisual(
    onComenzar: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(width = 36.dp, height = 66.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PetroleumPrimary)
            )
            Box(
                Modifier
                    .size(width = 66.dp, height = 36.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PetroleumPrimary)
            )
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = WarmAccent
            )
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Clínica SaludPlus",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Tu salud, nuestra prioridad",
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(0.1f))

        Image(
            painter = painterResource(R.drawable.doctor_splash),
            contentDescription = "Médico de Clínica SaludPlus",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(Modifier.height(20.dp))

        BotonPrincipal(
            texto = "Comenzar",
            containerColor = PetroleumPrimary,
            onClick = onComenzar
        )

        Spacer(Modifier.height(10.dp))

        TextButton(onClick = onIniciarSesion) {
            Text(
                text = "Ya tengo una cuenta",
                color = PetroleumPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(12.dp))
    }
}
