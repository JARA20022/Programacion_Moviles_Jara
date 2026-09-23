package com.jara.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// La primera fase reúne el código en MainActivity.kt; el avance 8 lo separa.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Se fija el tema claro para conservar los colores de la referencia.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = VerdeFit, background = Color.White,
                    surface = Color.White, onSurface = TextoFit
                )
            ) { NavegacionInicialFit() }
        }
    }
}
// Paleta aproximada a partir de las figuras 3 y 4 del Word.
val VerdeFit = Color(0xFF10735E)
val VerdeEstado = Color(0xFF169E7C)
val FondoVerdeFit = Color(0xFFE0F5EE)
val FondoTarjetaFit = Color(0xFFF0F0F0)
val TextoFit = Color(0xFF202020)
val TextoSecundarioFit = Color(0xFF686868)
val FondoCompletadaFit = Color(0xFFE5E5E5)
// Una clase incluye el día de la sesión para aplicar el filtro de Inicio.
data class ClaseFit(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val dia: String = "Hoy",
    val duracion: String? = null,
    val descripcion: String? = null,
    val cuposIniciales: Int? = null,
    val capacidad: Int? = null
)

data class ReservaFit(
    val id: Int,
    val clase: ClaseFit,
    val cupos: Int = 1,
    val estado: String = "Confirmada"
)

data class UsuarioFit(
    val nombre: String,
    val iniciales: String,
    val plan: String,
    val clasesTomadas: Int,
    val rachas: Int
)
// Datos de demostración tomados de las figuras del Word, sin servidor.
object DatosFit {
    val clases = listOf(
        ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"),
        ClaseFit(
            id = 2,
            nombre = "Cross Training",
            hora = "6:00 pm",
            sala = "Sala 1",
            duracion = "45 min",
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposIniciales = 8,
            capacidad = 12
        ),
        ClaseFit(3, "Spinning", "7:30 pm", "Sala 3"),
        // La figura de Reservas también muestra Yoga de Ayer a las 7:00 am.
        // Se incluye como sesión pasada en Esta semana; no se puede reservar.
        ClaseFit(4, "Yoga funcional", "7:00 am", "Sala 2", dia = "Ayer")
    )

    val usuario = UsuarioFit("Diego Ramos", "DR", "Plan Premium", 14, 3)

    // Solo se precarga la sesión completada de la referencia.
    // Las Confirmadas se generan al usar el botón Reservar cupo.
    fun reservasIniciales() = listOf(
        ReservaFit(id = 1, clase = clases.first { it.id == 4 }, estado = "Completada")
    )
}
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
fun BotonAtrasFit(onVolver: () -> Unit) {
    IconButton(
        onClick = onVolver,
        modifier = Modifier.semantics { contentDescription = "Regresar" }
    ) { Text("←", color = TextoFit, fontSize = 24.sp) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioFit(
    clases: List<ClaseFit>, filtro: String,
    onFiltro: (String) -> Unit, onClase: (Int) -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    // Todos los datos de esta demostración pertenecen a la semana del ejemplo.
    val visibles = if (filtro == "Hoy") clases.filter { it.dia == "Hoy" } else clases
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Hola, Diego", fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeFit, titleContentColor = Color.White
                )
            )
        },
        bottomBar = barraInferior
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            LazyRow(
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Hoy", "Esta semana")) { opcion ->
                    FilterChip(
                        selected = filtro == opcion, onClick = { onFiltro(opcion) },
                        label = { Text(opcion) },
                        shape = RoundedCornerShape(50), border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = FondoTarjetaFit,
                            labelColor = TextoSecundarioFit,
                            selectedContainerColor = VerdeFit,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Text("Clases disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoFit)
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(visibles, key = { it.id }) { clase ->
                    TarjetaClase(clase, mostrarDia = filtro != "Hoy", onClick = { onClase(clase.id) })
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleFit(
    clase: ClaseFit, disponibles: Int?, onVolver: () -> Unit,
    onReservar: (Int) -> Unit,
    permitirReserva: Boolean = true
) {
    // En el siguiente avance se añade la selección de cupos.
    val cupos = 1
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
@Composable
fun NavegacionInicialFit() {
    val navController = rememberNavController()
    var filtro by remember { mutableStateOf("Hoy") }
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            InicioFit(DatosFit.clases, filtro, onFiltro = { filtro = it },
                onClase = { navController.navigate("detalle/$it") })
        }
        composable("detalle/{claseId}",
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("claseId")
            val clase = DatosFit.clases.first { it.id == id }
            DetalleFit(clase, clase.cuposIniciales,
                onVolver = { navController.popBackStack() }, onReservar = {},
                permitirReserva = false)
        }
    }
}
