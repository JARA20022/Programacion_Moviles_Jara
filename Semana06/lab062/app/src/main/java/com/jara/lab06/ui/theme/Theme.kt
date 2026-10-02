package com.jara.lab06.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = MoradoPrincipal,
    onPrimary = FondoGeneral,
    primaryContainer = FondoOpcionSeleccionada,
    onPrimaryContainer = MoradoPrincipal,
    secondary = TextoSecundarioPrecios,
    background = FondoGeneral,
    onBackground = TextoPrincipal,
    surface = FondoGeneral,
    onSurface = TextoPrincipal,
    surfaceVariant = FondoTarjetas,
    onSurfaceVariant = TextoSecundarioPrecios,
    outlineVariant = SeparadoresContornos
)

@Composable
fun Lab06Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
