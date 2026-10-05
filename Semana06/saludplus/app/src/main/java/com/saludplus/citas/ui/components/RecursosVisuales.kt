package com.saludplus.citas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.saludplus.citas.R
import java.util.Locale

@Composable
fun FotoMedico(
    medicoId: Int,
    nombre: String,
    modifier: Modifier = Modifier,
    tamaño: Dp = 56.dp
) {
    val foto = when (medicoId) {
        1 -> R.drawable.medico_1
        2 -> R.drawable.medico_3
        3 -> R.drawable.medico_2
        4 -> R.drawable.medico_5
        5 -> R.drawable.medico_4
        6 -> R.drawable.medico_3
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
    tamaño: Dp = 42.dp
) {
    val nombreNormalizado = nombre.lowercase(Locale.ROOT)

    val (icono, color) = when {
        "pediatr" in nombreNormalizado ->
            Icons.Filled.ChildCare to Color(0xFFF5A33A)
        "gineco" in nombreNormalizado ->
            Icons.Filled.Female to Color(0xFFE85093)
        "cardio" in nombreNormalizado ->
            Icons.Filled.Favorite to Color(0xFFEB4C59)
        "dermato" in nombreNormalizado ->
            Icons.Filled.Spa to Color(0xFFE69A3A)
        "traumato" in nombreNormalizado ->
            Icons.Filled.Healing to Color(0xFF318BD2)
        "oftalmo" in nombreNormalizado ->
            Icons.Filled.Visibility to Color(0xFF316BDF)
        else ->
            Icons.Filled.MedicalServices to Color(0xFF3D7BDD)
    }

    Box(
        modifier = modifier
            .size(tamaño)
            .background(color.copy(alpha = 0.13f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = nombre,
            tint = color,
            modifier = Modifier.size(tamaño * 0.58f)
        )
    }
}

@Composable
fun IconoInicio(
    destino: String,
    modifier: Modifier = Modifier,
    tamaño: Dp = 38.dp
) {
    val (icono, color) = when (destino.lowercase(Locale.ROOT)) {
        "agendar" ->
            Icons.Filled.CalendarMonth to Color(0xFF2864E8)
        "citas" ->
            Icons.Filled.CalendarMonth to Color(0xFF2BA777)
        "resultados" ->
            Icons.Filled.Description to Color(0xFFE89932)
        else ->
            Icons.Filled.Person to Color(0xFF8A5BE2)
    }

    Icon(
        imageVector = icono,
        contentDescription = destino,
        tint = color,
        modifier = modifier.size(tamaño)
    )
}