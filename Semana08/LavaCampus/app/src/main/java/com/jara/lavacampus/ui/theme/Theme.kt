package com.jara.lavacampus.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.model.Rol
val TintaCampus=Color(0xFF102A2B)
val TerracotaCampus=Color(0xFF005D50)
val IndigoCampus=Color(0xFF244BB4)
val LimaStudio=Color(0xFFD6F58A)
fun colorDelRol(rol:Rol):Color=if(rol==Rol.CLIENTE)TerracotaCampus else IndigoCampus
private val Claro=lightColorScheme(
 primary=TerracotaCampus,onPrimary=Color.White,primaryContainer=Color(0xFFD7F1E9),onPrimaryContainer=TintaCampus,
 secondary=IndigoCampus,onSecondary=Color.White,secondaryContainer=Color(0xFFE1E9FF),onSecondaryContainer=Color(0xFF16274E),
 tertiary=Color(0xFF4D6618),onTertiary=Color.White,
 background=Color(0xFFF0F3ED),onBackground=TintaCampus,surface=Color(0xFFFCFDF8),onSurface=TintaCampus,
 surfaceVariant=Color(0xFFE2E9DF),onSurfaceVariant=Color(0xFF465751),outline=Color(0xFF7A8B82),outlineVariant=Color(0xFFCDD8CE),
 error=Color(0xFFA22B32),onError=Color.White,errorContainer=Color(0xFFFFE3E2),onErrorContainer=Color(0xFF61171C))
private val Oscuro=darkColorScheme(
 primary=Color(0xFF9DE4CB),onPrimary=Color(0xFF00382F),primaryContainer=Color(0xFF214C43),onPrimaryContainer=Color(0xFFC2F5DF),
 secondary=Color(0xFFB5C8FF),onSecondary=Color(0xFF152B5B),secondaryContainer=Color(0xFF293D63),onSecondaryContainer=Color(0xFFDCE5FF),
 tertiary=LimaStudio,onTertiary=Color(0xFF2A3C08),background=Color(0xFF0D1817),onBackground=Color(0xFFEDF2E8),
 surface=Color(0xFF172724),onSurface=Color(0xFFEDF2E8),surfaceVariant=Color(0xFF2C3D37),onSurfaceVariant=Color(0xFFB6C8BD),
 outline=Color(0xFF80978A),outlineVariant=Color(0xFF3A4E44),error=Color(0xFFFFB4B4),onError=Color(0xFF650A15),errorContainer=Color(0xFF51272C),onErrorContainer=Color(0xFFFFDADB))
@Composable fun LavaCampusTheme(darkTheme:Boolean=isSystemInDarkTheme(),content:@Composable () -> Unit) {
 MaterialTheme(colorScheme=if(darkTheme)Oscuro else Claro,typography=Typography,
  shapes=Shapes(extraSmall=RoundedCornerShape(6.dp),small=CutCornerShape(topEnd=12.dp),medium=RoundedCornerShape(20.dp),large=RoundedCornerShape(topStart=28.dp,topEnd=6.dp,bottomEnd=28.dp,bottomStart=6.dp),extraLarge=RoundedCornerShape(28.dp)),content=content)
}
