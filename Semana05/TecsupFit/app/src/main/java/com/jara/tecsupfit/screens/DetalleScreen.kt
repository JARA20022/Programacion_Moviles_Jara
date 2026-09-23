package com.jara.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.tecsupfit.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import com.jara.tecsupfit.model.ClaseFit
import com.jara.tecsupfit.ui.components.BotonAtrasFit
import com.jara.tecsupfit.ui.components.IconoPesa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleFit(
    clase: ClaseFit, disponibles: Int?, onVolver: () -> Unit,
    onReservar: (Int) -> Unit,
    permitirReserva: Boolean = true,
    mostrarSelector: Boolean = true
) {
    // Una única variable representa la opción elegida, como en RadioButton.
    var cupos by remember(clase.id) { mutableStateOf(1) }
    val sesionVigente = clase.dia == "Hoy"
    val puedeReservar = permitirReserva && sesionVigente &&
            (disponibles == null || cupos <= disponibles)
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { BotonAtrasFit(onVolver) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = TextoFit)
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                        .background(FondoVerdeFit, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) { IconoPesa(96.dp) }
                Spacer(Modifier.height(20.dp))
                Text(clase.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoFit)
                val duracion = clase.duracion?.let { " · $it" } ?: ""
                Text("${clase.hora} · ${clase.sala}$duracion", fontSize = 14.sp, color = TextoSecundarioFit)
                clase.descripcion?.let {
                    Spacer(Modifier.height(20.dp))
                    Text(it, color = TextoFit, fontSize = 14.sp)
                }
                if (disponibles != null && clase.capacidad != null) {
                    Spacer(Modifier.height(20.dp))
                    Text("$disponibles de ${clase.capacidad} cupos disponibles", fontSize = 14.sp, color = TextoFit)
                }
                if (!sesionVigente) {
                    Spacer(Modifier.height(16.dp))
                    Text("Esta sesión ya finalizó.", color = TextoSecundarioFit)
                } else if (mostrarSelector) {
                    Spacer(Modifier.height(20.dp))
                    Text("Selecciona tus cupos", fontWeight = FontWeight.Bold, color = TextoFit)
                    // Adaptación a la rúbrica: la maqueta no dibuja este selector.
                    Row(
                        modifier = Modifier.fillMaxWidth().selectableGroup().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..3).forEach { cantidad ->
                            val seleccionada = cantidad == cupos
                            val disponible = disponibles == null || cantidad <= disponibles
                            Box(
                                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (seleccionada) VerdeFit else FondoTarjetaFit)
                                    .selectable(
                                        selected = seleccionada, enabled = disponible,
                                        role = Role.RadioButton, onClick = { cupos = cantidad }
                                    ).padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    if (cantidad == 1) "1 cupo" else "$cantidad cupos",
                                    fontSize = 13.sp,
                                    color = when {
                                        !disponible -> Color.Gray
                                        seleccionada -> Color.White
                                        else -> TextoFit
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
            Button(
                onClick = { onReservar(cupos) }, enabled = puedeReservar,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeFit, contentColor = Color.White)
            ) { Text("Reservar cupo", fontWeight = FontWeight.Bold) }
        }
    }
}
