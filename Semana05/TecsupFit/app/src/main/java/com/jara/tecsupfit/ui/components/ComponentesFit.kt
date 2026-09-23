package com.jara.tecsupfit.ui.components

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.jara.tecsupfit.model.ClaseFit
import com.jara.tecsupfit.model.ReservaFit

@Composable
fun IconoPesa(tamano: Dp = 40.dp) {
    // Pesa dibujada con Box: no requiere imágenes ni librerías de iconos.
    Box(
        modifier = Modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.fillMaxWidth(0.72f).height(tamano * 0.13f).background(VerdeFit))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(2) {
                Box(
                    Modifier.width(tamano * 0.14f).height(tamano * 0.38f)
                        .background(VerdeFit, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun TarjetaClase(clase: ClaseFit, mostrarDia: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjetaFit)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.background(FondoVerdeFit, RoundedCornerShape(10.dp))) {
                IconoPesa()
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(clase.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoFit)
                Spacer(Modifier.height(3.dp))
                val prefijo = if (mostrarDia) "${clase.dia}, " else ""
                Text(
                    "$prefijo${clase.hora} · ${clase.sala}",
                    fontSize = 12.sp, color = TextoSecundarioFit
                )
            }
        }
    }
}

@Composable
fun TarjetaReserva(reserva: ReservaFit) {
    val confirmada = reserva.estado == "Confirmada"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjetaFit)
    ) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                Modifier.width(4.dp).fillMaxHeight()
                    .background(if (confirmada) VerdeFit else Color.Transparent)
            )
            Column(Modifier.weight(1f).padding(16.dp)) {
                Text(reserva.clase.nombre, fontWeight = FontWeight.Bold, color = TextoFit)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${reserva.clase.dia}, ${reserva.clase.hora}",
                    fontSize = 13.sp, color = TextoSecundarioFit
                )
                if (reserva.cupos > 1) {
                    Text("${reserva.cupos} cupos", fontSize = 12.sp, color = TextoSecundarioFit)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = reserva.estado,
                    modifier = Modifier
                        .background(
                            if (confirmada) FondoVerdeFit else FondoCompletadaFit,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    color = if (confirmada) VerdeEstado else TextoSecundarioFit,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BarraInferiorFit(rutaActual: String, onDestino: (String) -> Unit) {
    val destinos = listOf(
        "inicio" to "Inicio", "reservas" to "Reservas",
        "rutinas" to "Rutinas", "perfil" to "Perfil"
    )
    Column {
        HorizontalDivider(color = Color(0xFFDDDDDD))
        NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
            destinos.forEach { (ruta, titulo) ->
                val seleccionado = ruta == rutaActual
                NavigationBarItem(
                    selected = seleccionado,
                    onClick = { onDestino(ruta) },
                    icon = {
                        // La referencia usa círculos; el color indica la pestaña activa.
                        Box(
                            Modifier.size(22.dp).border(
                                width = if (seleccionado) 2.dp else 1.dp,
                                color = if (seleccionado) VerdeFit else TextoSecundarioFit,
                                shape = CircleShape
                            )
                        )
                    },
                    label = {
                        Text(titulo, fontSize = 11.sp,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VerdeFit, selectedTextColor = VerdeFit,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = TextoSecundarioFit,
                        unselectedTextColor = TextoSecundarioFit
                    )
                )
            }
        }
    }
}

@Composable
fun BotonAtrasFit(onVolver: () -> Unit) {
    IconButton(
        onClick = onVolver,
        modifier = Modifier.semantics { contentDescription = "Regresar" }
    ) { Text("←", color = TextoFit, fontSize = 24.sp) }
}

@Composable
fun EstadisticaFit(valor: Int, etiqueta: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(FondoTarjetaFit, RoundedCornerShape(12.dp)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoFit)
        Text(etiqueta, fontSize = 12.sp, color = TextoSecundarioFit)
    }
}
