package com.jara.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.model.Rol

val TintaCampus = Color(0xFF171324)
val TerracotaCampus = Color(0xFF7046D2)
val IndigoCampus = Color(0xFF6452C6)
val LimaStudio = Color(0xFFE0D5FF)
val CoralCampus = Color(0xFFFFB49F)
fun colorDelRol(rol: Rol): Color = if (rol == Rol.CLIENTE) TerracotaCampus else IndigoCampus
private val Claro = lightColorScheme(
    primary=Color(0xFF6841C6), onPrimary=Color.White,
    primaryContainer=Color(0xFFE7DEFF), onPrimaryContainer=Color(0xFF2B1757),
    secondary=Color(0xFF9C462E), onSecondary=Color.White,
    secondaryContainer=Color(0xFFFFDAD0), onSecondaryContainer=Color(0xFF451B13),
    tertiary=Color(0xFF006A60), onTertiary=Color.White,
    background=Color(0xFFF4F1FA), onBackground=Color(0xFF211B30),
    surface=Color(0xFFFFFBFF), onSurface=Color(0xFF211B30),
    surfaceVariant=Color(0xFFEAE4F3), onSurfaceVariant=Color(0xFF625A70),
    outline=Color(0xFF81768E), outlineVariant=Color(0xFFDBD2E9),
    error=Color(0xFFAA2C3D), onError=Color.White,
    errorContainer=Color(0xFFFFDADF), onErrorContainer=Color(0xFF5F1424))
private val Oscuro = darkColorScheme(
    primary=Color(0xFFC4AEFF), onPrimary=Color(0xFF27124F),
    primaryContainer=Color(0xFF392B59), onPrimaryContainer=Color(0xFFEBE0FF),
    secondary=CoralCampus, onSecondary=Color(0xFF4B2319),
    secondaryContainer=Color(0xFF4F302A), onSecondaryContainer=Color(0xFFFFDFD5),
    tertiary=Color(0xFF8FDCC8), onTertiary=Color(0xFF00382F),
    background=Color(0xFF101018), onBackground=Color(0xFFF5F0FF),
    surface=Color(0xFF20202E), onSurface=Color(0xFFF5F0FF),
    surfaceVariant=Color(0xFF2A293C), onSurfaceVariant=Color(0xFFC0B9CE),
    outline=Color(0xFF91869F), outlineVariant=Color(0xFF40394F),
    error=Color(0xFFFFA8B8), onError=Color(0xFF5C1427),
    errorContainer=Color(0xFF482631), onErrorContainer=Color(0xFFFFDDE5))
@Composable
fun LavaCampusTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme=if(darkTheme) Oscuro else Claro, typography=Typography,
        shapes=Shapes(extraSmall=RoundedCornerShape(10.dp),small=RoundedCornerShape(16.dp),
            medium=RoundedCornerShape(24.dp),large=RoundedCornerShape(30.dp),extraLarge=RoundedCornerShape(36.dp)),content=content)
}
