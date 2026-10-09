package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.TextPrimary

@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Términos y condiciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PetroleumPrimary
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Clínica SaludPlus permite consultar especialidades, elegir médicos y solicitar citas desde esta aplicación.",
            fontSize = 15.sp,
            color = TextPrimary,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Los datos ingresados se utilizan en esta demostración para identificar al paciente y mostrar sus citas.",
            fontSize = 15.sp,
            color = TextPrimary,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Las reservas se guardan temporalmente en el dispositivo mientras la aplicación permanece abierta.",
            fontSize = 15.sp,
            color = TextPrimary,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(32.dp))

        BotonPrincipal(
            texto = "Volver al registro",
            onClick = onVolver,
            containerColor = PetroleumPrimary
        )
    }
}
