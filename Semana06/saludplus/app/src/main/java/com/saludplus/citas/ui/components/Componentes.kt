package com.saludplus.citas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.ui.theme.OnPrimary
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.PetroleumVariant
import com.saludplus.citas.ui.theme.SurfaceSecondary
import java.util.Locale

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = PetroleumPrimary,
    contentColor: Color = OnPrimary
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        )
    ) {
        Text(
            text = texto,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FotoMedico(
    medicoId: Int,
    nombre: String,
    modifier: Modifier = Modifier,
    tamaño: Dp = 72.dp
) {
    val foto = when (medicoId) {
        1 -> R.drawable.medico_1
        2 -> R.drawable.medico_3
        3 -> R.drawable.medico_2
        4 -> R.drawable.medico_5
        5 -> R.drawable.medico_4
        6 -> R.drawable.medico_6
        else -> R.drawable.medico_1
    }

    Image(
        painter = painterResource(foto),
        contentDescription = "Foto ilustrativa de $nombre",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(tamaño)
            .clip(CircleShape)
    )
}

@Composable
fun IconoEspecialidad(
    nombre: String,
    modifier: Modifier = Modifier,
    tamaño: Dp = 48.dp
) {
    val nombreNormalizado = nombre.lowercase(Locale.ROOT)

    val (icono, color) = when {
        "pediatr" in nombreNormalizado ->
            Icons.Filled.ChildCare to PetroleumPrimary

        "gineco" in nombreNormalizado ->
            Icons.Filled.Female to PetroleumVariant

        "cardio" in nombreNormalizado ->
            Icons.Filled.Favorite to PetroleumPrimary

        "dermato" in nombreNormalizado ->
            Icons.Filled.Spa to PetroleumVariant

        "traumato" in nombreNormalizado ->
            Icons.Filled.Healing to PetroleumPrimary

        "oftalmo" in nombreNormalizado ->
            Icons.Filled.Visibility to PetroleumVariant

        else ->
            Icons.Filled.MedicalServices to PetroleumPrimary
    }

    Box(
        modifier = modifier
            .size(tamaño)
            .background(
                color = SurfaceSecondary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = nombre,
            tint = color,
            modifier = Modifier.size(tamaño * 0.55f)
        )
    }
}

@Composable
fun IconoInicio(
    destino: String,
    modifier: Modifier = Modifier,
    tamaño: Dp = 48.dp
) {
    val (icono, color) = when (destino.lowercase(Locale.ROOT)) {
        "agendar", "sedes" ->
            Icons.Filled.CalendarMonth to PetroleumPrimary

        "citas", "doctores" ->
            Icons.Filled.MedicalServices to PetroleumVariant

        "resultados" ->
            Icons.Filled.Description to PetroleumPrimary

        else ->
            Icons.Filled.Person to PetroleumVariant
    }

    Box(
        modifier = modifier
            .size(tamaño)
            .background(
                color = SurfaceSecondary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = destino,
            tint = color,
            modifier = Modifier.size(tamaño * 0.55f)
        )
    }
}
