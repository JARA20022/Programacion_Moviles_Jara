package com.saludplus.citas.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PetroleumPrimary,
    onPrimary = OnPrimary,
    primaryContainer = SurfaceSecondary,
    onPrimaryContainer = PetroleumPrimary,
    secondary = PetroleumVariant,
    onSecondary = OnPrimary,
    tertiary = WarmAccent,
    onTertiary = OnWarmAccent,
    background = BackgroundMain,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSecondary,
    onSurfaceVariant = TextPrimary,
    error = ErrorRed,
    onError = OnPrimary,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PetroleumPrimary,
    onPrimary = OnPrimary,
    primaryContainer = SurfaceSecondary,
    onPrimaryContainer = PetroleumPrimary,
    secondary = PetroleumVariant,
    onSecondary = OnPrimary,
    tertiary = WarmAccent,
    onTertiary = OnWarmAccent,
    background = BackgroundMain,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSecondary,
    onSurfaceVariant = TextPrimary,
    error = ErrorRed,
    onError = OnPrimary,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed
)

@Composable
fun SaludPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
