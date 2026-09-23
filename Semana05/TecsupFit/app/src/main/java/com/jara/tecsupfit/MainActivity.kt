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
            ) { InicioInicialFit() }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioInicialFit() {
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
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Text("Clases disponibles", fontWeight = FontWeight.Bold, color = TextoFit)
        }
    }
}
