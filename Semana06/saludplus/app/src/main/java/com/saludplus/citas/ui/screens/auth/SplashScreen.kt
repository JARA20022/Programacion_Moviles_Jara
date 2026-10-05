package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable

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