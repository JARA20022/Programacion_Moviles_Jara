package com.tecsup.productos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CatalogoColors = darkColorScheme(
    primary = Color(0xFFC9FC66),
    onPrimary = Color(0xFF1B2900),
    primaryContainer = Color(0xFF293D1B),
    onPrimaryContainer = Color(0xFFDDFCBA),
    secondary = Color(0xFFB3C6FF),
    onSecondary = Color(0xFF10295C),
    secondaryContainer = Color(0xFF202C54),
    onSecondaryContainer = Color(0xFFDFE5FF),
    background = Color(0xFF0C111D),
    onBackground = Color(0xFFF5F6FF),
    surface = Color(0xFF131B2A),
    onSurface = Color(0xFFF5F6FF),
    surfaceVariant = Color(0xFF242E42),
    onSurfaceVariant = Color(0xFFB8C4DB),
    outline = Color(0xFF77839E),
    outlineVariant = Color(0xFF343F54),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF4B2024),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun ProductosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CatalogoColors,
        typography = Typography,
        content = content
    )
}
