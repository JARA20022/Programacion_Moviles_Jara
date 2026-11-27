package com.tecsup.avisoscampus.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CampusBackground=Color(0xFF0C1020)
val CampusSurface=Color(0xFF181E33)
val CampusMuted=Color(0xFFA9B3CF)
val CampusPurple=Color(0xFFB5A5FF)
val CampusMint=Color(0xFF69E2C4)
val CampusAmber=Color(0xFFFFCA80)
@Composable fun CampusTheme(content: @Composable () -> Unit) {
 MaterialTheme(colorScheme=darkColorScheme(primary=CampusPurple,secondary=CampusMint,tertiary=CampusAmber,background=CampusBackground,surface=CampusSurface,onSurface=Color(0xFFF3F4FF),onBackground=Color(0xFFF3F4FF)),content=content)
}
