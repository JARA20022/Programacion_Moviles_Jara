package com.jara.lab05.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppLightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = TextOnPurple,
    background = BackgroundGeneral,
    onBackground = TextPrimary,
    surface = BackgroundGeneral,
    onSurface = TextPrimary,
    surfaceTint = Color.Transparent
)

@Composable
fun Lab05Theme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppLightColorScheme,
        typography = Typography,
        content = content
    )
}