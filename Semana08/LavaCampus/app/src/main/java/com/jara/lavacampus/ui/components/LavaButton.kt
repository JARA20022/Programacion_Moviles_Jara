package com.jara.lavacampus.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LavaButton(
    texto: String,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    cargando: Boolean = false
) {
    val interaction=remember { MutableInteractionSource() }
    val pulsado by interaction.collectIsPressedAsState()
    val escala by animateFloatAsState(if(pulsado) .97f else 1f,spring(),label="presion")
    Button(
        interactionSource=interaction,
        onClick = onClick,
        enabled = !cargando,
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).graphicsLayer{scaleX=escala;scaleY=escala},
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        if (cargando) {
            CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = Color.White)
        } else {
            Text(texto, fontWeight = FontWeight.Bold)
        }
    }
}
