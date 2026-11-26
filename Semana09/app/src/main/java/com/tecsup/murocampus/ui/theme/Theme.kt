package com.tecsup.murocampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Noche = darkColorScheme(
    primary = Color(0xFF62F7DA), onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF104E54), onPrimaryContainer = Color(0xFF99FFEB),
    secondary = Color(0xFFC5A6FF), onSecondary = Color(0xFF30204F),
    secondaryContainer = Color(0xFF443064), onSecondaryContainer = Color(0xFFE8D7FF),
    tertiary = Color(0xFFFFAD79), onTertiary = Color(0xFF412512),
    background = Color(0xFF0B1025), onBackground = Color(0xFFE3E6FF),
    surface = Color(0xFF121933), onSurface = Color(0xFFE3E6FF),
    surfaceVariant = Color(0xFF202B48), onSurfaceVariant = Color(0xFFBBC5E1),
    surfaceContainer = Color(0xFF192340), surfaceContainerHigh = Color(0xFF222C4B),
    outline = Color(0xFF7785A8), outlineVariant = Color(0xFF394462),
    error = Color(0xFFFFA5AE), onError = Color(0xFF38131D),
    errorContainer = Color(0xFF532439), onErrorContainer = Color(0xFFFFD9DE)
)
@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Noche,
        shapes = Shapes(small = RoundedCornerShape(14.dp), medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(26.dp)),
        content = content
    )
}
