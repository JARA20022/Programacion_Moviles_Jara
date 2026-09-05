package com.jara.lab03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

data class Curso(val nombre: String, val peso: Float)

val cursos = listOf(
    Curso("Fundamentos de Programación", 0.20f),
    Curso("Programación Orientada a Objetos", 0.25f),
    Curso("Programación en Móviles", 0.30f),
    Curso("Base de Datos", 0.25f)
)

val MoradoOscuro = Color(0xFF5B3E96)
val MoradoClaro = Color(0xFFB39DDB)
val FondoInicio = Color(0xFFEDE7F6)
val FondoFin = Color(0xFFFFFFFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                RegistroNotasScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var calculado by remember { mutableStateOf(false) }

    val notas = listOf(nota1, nota2, nota3, nota4)
    val setters = listOf<(Float) -> Unit>(
        { nota1 = it }, { nota2 = it }, { nota3 = it }, { nota4 = it }
    )

    val promedioPonderado = cursos.indices.sumOf { i ->
        (notas[i] * cursos[i].peso).toDouble()
    }.toFloat()

    val promedioFinal = if (redondear) promedioPonderado.roundToInt().toFloat() else promedioPonderado

    val observacion = when {
        promedioFinal >= 17f -> "EXCELENTE"
        promedioFinal >= 13f -> "APROBADO"
        promedioFinal >= 10f -> "EN RECUPERACIÓN"
        else -> "DESAPROBADO"
    }

    val colorChip = when {
        promedioFinal >= 17f -> Color(0xFF2E7D32)
        promedioFinal >= 13f -> Color(0xFF66BB6A)
        promedioFinal >= 10f -> Color(0xFFFFA000)
        else -> Color(0xFFE53935)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro de Notas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoOscuro,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(listOf(FondoInicio, FondoFin))
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    "Notas del ciclo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B2A5A)
                )
                Text(
                    "Desliza para asignar cada nota (0 a 20)",
                    fontSize = 14.sp,
                    color = Color(0xFF7A6C99)
                )

                Spacer(modifier = Modifier.height(16.dp))

                cursos.forEachIndexed { i, curso ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(
                                curso.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF3B2A5A)
                            )
                            Text(
                                "  (${(curso.peso * 100).toInt()}%)",
                                fontSize = 14.sp,
                                color = Color(0xFF9B8FBF)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEDE7F6))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                notas[i].toInt().toString(),
                                fontWeight = FontWeight.Bold,
                                color = MoradoOscuro
                            )
                        }
                    }
                    Slider(
                        value = notas[i],
                        onValueChange = setters[i],
                        valueRange = 0f..20f,
                        steps = 19,
                        colors = SliderDefaults.colors(
                            thumbColor = MoradoOscuro,
                            activeTrackColor = MoradoOscuro,
                            inactiveTrackColor = Color(0xFFE0D6F5)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Redondear promedio final", fontSize = 15.sp, color = Color(0xFF3B2A5A))
                    Switch(
                        checked = redondear,
                        onCheckedChange = { redondear = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MoradoOscuro
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = { confirmado = it },
                        colors = CheckboxDefaults.colors(checkedColor = MoradoOscuro)
                    )
                    Text("Confirmo que las notas son correctas", fontSize = 15.sp, color = Color(0xFF3B2A5A))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { calculado = true },
                    enabled = confirmado,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoOscuro,
                        disabledContainerColor = MoradoClaro
                    )
                ) {
                    Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!calculado) {
                    Text(
                        "Asigna las notas y confirma para calcular",
                        color = Color(0xFF9B8FBF),
                        fontSize = 14.sp
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Promedio ponderado: ${"%.2f".format(promedioPonderado)}",
                                fontSize = 16.sp,
                                color = Color(0xFF3B2A5A)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Promedio final: ${"%.2f".format(promedioFinal)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MoradoOscuro
                            )
                            if (redondear) {
                                Text(
                                    "(redondeado)",
                                    fontSize = 13.sp,
                                    color = Color(0xFF9B8FBF)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colorChip.copy(alpha = 0.2f))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    observacion,
                                    fontWeight = FontWeight.Bold,
                                    color = colorChip
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "✓ Promedio calculado correctamente",
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Desarrollado por: (tu nombre completo)",
                    fontSize = 12.sp,
                    color = Color(0xFF9B8FBF),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}