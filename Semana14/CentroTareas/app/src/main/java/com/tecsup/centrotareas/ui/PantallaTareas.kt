package com.tecsup.centrotareas.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

private const val LIMITE_INICIAL = 500000

@Composable
fun PantallaTareas(vm: TareasViewModel) {
    var abierto by rememberSaveable { mutableStateOf(false) }
    val entrada = remember { Animatable(if (abierto) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!abierto) {
            abierto = true
            entrada.animateTo(1f, tween(720))
        }
    }
    var seccion by rememberSaveable { mutableIntStateOf(0) }
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var limite by rememberSaveable { mutableStateOf(LIMITE_INICIAL.toString()) }
    val progreso by vm.progreso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simular by vm.simularError.collectAsState()

    MaterialTheme(colorScheme = ColoresCentro) {
        Surface(color = Noche, modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(Modifier.fillMaxSize().safeDrawingPadding().imePadding(),
                contentAlignment = Alignment.TopCenter) {
                val margen = if (maxWidth < 360.dp) 12.dp else 20.dp
                Column(Modifier.widthIn(max = 700.dp).fillMaxWidth()
                    .verticalScroll(rememberScrollState()).padding(horizontal = margen, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.graphicsLayer {
                            rotationZ = (1f - entrada.value) * -32f
                            scaleX = .75f + entrada.value * .25f
                            scaleY = scaleX
                        }) { Marca(entrada.value) }
                        Text("CentroTareas", color = Hielo, fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold,
                            modifier = Modifier.graphicsLayer {
                                alpha = entrada.value
                                translationX = (1f - entrada.value) * 18.dp.toPx()
                            })
                    }
                    if (seccion == 1) {
                        PanelPrimos(primos, toques) { toques++ }
                    } else {
                        PanelProgreso(progreso, mensaje, entrada.value, toques) { toques++ }
                    }
                    Selector(seccion) { seccion = it }
                    AnimatedContent(targetState = seccion, transitionSpec = {
                        val direccion = if (targetState > initialState) 1 else -1
                        (fadeIn(tween(190)) + slideInHorizontally(tween(190)) { direccion * it / 9 })
                            .togetherWith(fadeOut(tween(100)))
                    }, label = "Sección") { destino ->
                        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            when (destino) {
                                0 -> {
                                    TituloSeccion("Tarea con progreso")
                                    Acciones {
                                        Button(onClick = vm::tareaLarga, enabled = !corriendo) { Text("Iniciar") }
                                        OutlinedButton(onClick = vm::cancelar, enabled = corriendo) { Text("Cancelar") }
                                    }
                                }
                                1 -> SeccionPrimos(primos, limite, { limite = it }, vm::contar, vm::cancelarPrimos)
                                2 -> SeccionCargas(carga, simular, vm::cambiarSimularError,
                                    vm::cargarUnaPorUna, vm::cargarALaVez)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun PanelProgreso(progreso: Float, mensaje: String, entrada: Float, toques: Int, tocar: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
        .background(Brush.linearGradient(listOf(Color(0xFF0C4046), Pizarra))).padding(20.dp)) {
        val apilado = maxWidth < 250.dp || LocalDensity.current.fontScale > 1.3f
        val diametro = if (maxWidth >= 340.dp) 146.dp else 112.dp
        val detalle: @Composable () -> Unit = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Progreso de tarea", color = Niebla, fontFamily = FontFamily.Monospace)
                Text(mensaje, color = Color.White, fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Button(onClick = tocar, colors = ButtonDefaults.buttonColors(containerColor = Menta,
                    contentColor = Noche)) { Text("Tócame · $toques") }
            }
        }
        if (apilado) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Dial(progreso, entrada, Modifier.size(132.dp))
                detalle()
            }
        } else {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f)) { detalle() }
                Dial(progreso, entrada, Modifier.size(diametro))
            }
        }
    }
}

@Composable
private fun Selector(actual: Int, elegir: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf("Tarea", "Primos", "Cargas").forEachIndexed { indice, nombre ->
            val activo = actual == indice
            Box(Modifier.clip(RoundedCornerShape(14.dp))
                .background(if (activo) Menta else Pizarra)
                .selectable(selected = activo, role = Role.Tab, onClick = { elegir(indice) })
                .heightIn(min = 48.dp).padding(horizontal = 18.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center) {
                Text(nombre, color = if (activo) Noche else Hielo, fontSize = 15.sp,
                    fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
private fun SeccionPrimos(estado: PrimosUi, texto: String, editar: (String) -> Unit,
                          contar: (Int) -> Unit, cancelar: () -> Unit) {
    val limite = texto.toIntOrNull()
    val calculando = estado is PrimosUi.Calculando
    TituloSeccion("Números primos")
    OutlinedTextField(value = texto, onValueChange = { if (it.all { c -> c in '0'..'9' }) editar(it) },
        modifier = Modifier.fillMaxWidth(), label = { Text("Hasta N") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true, enabled = !calculando)
    Acciones {
        Button(onClick = { limite?.let(contar) }, enabled = !calculando && limite != null && limite >= 2) {
            Text("Contar")
        }
        OutlinedButton(onClick = cancelar, enabled = calculando) { Text("Cancelar") }
    }
    when (estado) {
        PrimosUi.Inicial -> Unit
        is PrimosUi.Calculando, is PrimosUi.Cancelado -> Unit // El panel superior muestra estos estados.
        is PrimosUi.Listo -> Resultado("${estado.cantidad} primos\nTiempo: ${estado.milisegundos} ms")
    }
}

@Composable
private fun SeccionCargas(estado: CargaUi, simular: Boolean, cambiar: (Boolean) -> Unit,
                          secuencial: () -> Unit, paralelo: () -> Unit) {
    val disponible = estado != CargaUi.Cargando
    TituloSeccion("Dos cargas")
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("Simular error", Modifier.weight(1f))
        Switch(checked = simular, onCheckedChange = cambiar, enabled = disponible)
    }
    Acciones {
        OutlinedButton(onClick = secuencial, enabled = disponible) { Text("Una por una") }
        Button(onClick = paralelo, enabled = disponible) { Text("A la vez") }
    }
    when (estado) {
        CargaUi.Inicial -> Unit
        CargaUi.Cargando -> {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text("Cargando perfil y pedidos…")
        }
        is CargaUi.Exito -> Resultado("${estado.modo} · ${estado.milisegundos} ms\n${estado.perfil}\nPedidos: ${estado.pedidos}")
        is CargaUi.Error -> Resultado(estado.mensaje, error = true) {
            TextButton(onClick = paralelo) { Text("Reintentar") }
        }
    }
}
