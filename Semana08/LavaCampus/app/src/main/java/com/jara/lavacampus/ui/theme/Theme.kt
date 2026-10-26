package com.jara.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jara.lavacampus.model.Rol

val TintaCampus = Color(0xFF20262A)
val TerracotaCampus = Color(0xFFAB4028)
val IndigoCampus = Color(0xFF334BA5)

fun colorDelRol(rol: Rol): Color =
    if (rol == Rol.CLIENTE) TerracotaCampus else IndigoCampus

private val PaletaClara = lightColorScheme(
    primary = TerracotaCampus,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD0),
    onPrimaryContainer = Color(0xFF3C0A00),
    secondary = IndigoCampus,
    onSecondary = Color.White,
    background = Color(0xFFF6F2EA),
    onBackground = TintaCampus,
    surface = Color(0xFFFFFBF5),
    onSurface = TintaCampus,
    surfaceVariant = Color(0xFFECE5DC),
    onSurfaceVariant = Color(0xFF514943),
    outline = Color(0xFF81756D)
)

private val PaletaOscura = darkColorScheme(
    primary = Color(0xFFFFB49E),
    onPrimary = Color(0xFF5C1807),
    secondary = Color(0xFFB8C4FF),
    background = Color(0xFF171C20),
    surface = Color(0xFF20262A),
    onBackground = Color(0xFFF1EDE5),
    onSurface = Color(0xFFF1EDE5)
)

@Composable
fun LavaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) PaletaOscura else PaletaClara,
        typography = Typography,
        content = content
    )
}
