package com.jara.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.util.Locale

// Guardamos los datos que necesita cada producto.
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String,
    val descripcion: String
)

// Los productos permanecen en memoria, como en los laboratorios anteriores.
val productosTienda = listOf(
    Producto(
        1,
        "Laptop para estudiar",
        2499.00,
        "Tecnología",
        "Ideal para realizar trabajos, estudiar y practicar programación."
    ),
    Producto(
        2,
        "Audífonos inalámbricos",
        129.00,
        "Tecnología",
        "Escucha tus clases y música con comodidad."
    ),
    Producto(
        3,
        "Mouse inalámbrico",
        59.00,
        "Tecnología",
        "Un accesorio práctico para trabajar con tu computadora."
    ),
    Producto(
        4,
        "Cuaderno universitario",
        18.00,
        "Útiles",
        "Organiza tus apuntes y actividades de clase."
    ),
    Producto(
        5,
        "Juego de lapiceros",
        12.00,
        "Útiles",
        "Lapiceros de diferentes colores para tus apuntes."
    ),
    Producto(
        6,
        "Mochila para clases",
        89.00,
        "Accesorios",
        "Lleva tus cuadernos y materiales de forma cómoda."
    ),
    Producto(
        7,
        "Tomatodo",
        35.00,
        "Accesorios",
        "Lleva agua durante tus clases y actividades."
    )
)

val categoriasTienda = listOf(
    "Todas",
    "Tecnología",
    "Útiles",
    "Accesorios"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF175CD3),
                    background = Color(0xFFF5F7FB),
                    surface = Color.White
                )
            ) {
                AppNavegacion()
            }
        }
    }
}

// Conectamos Inicio con Detalle, como en el Lab05.
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            InicioScreen(navController)
        }

        composable(
            route = "detalle/{productoId}",
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { entrada ->
            val productoId = entrada.arguments?.getInt("productoId")
            val producto = productosTienda.find {
                it.id == productoId
            }

            DetalleProductoScreen(
                navController = navController,
                producto = producto
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavHostController) {
    // Al cambiar la categoría, cambia la lista que se muestra.
    var categoriaSeleccionada by remember {
        mutableStateOf("Todas")
    }

    val productosVisibles = productosTienda.filter {
        categoriaSeleccionada == "Todas" ||
                it.categoria == categoriaSeleccionada
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "IVAN JARA AYALA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { espacio ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Bienvenido a mi tienda",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Encuentra productos para tus clases.",
                        color = Color.Gray
                    )
                }
            }

            // Las categorías se desplazan de forma horizontal.
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoriasTienda) { categoria ->
                        FilterChip(
                            selected = categoriaSeleccionada == categoria,
                            onClick = {
                                categoriaSeleccionada = categoria
                            },
                            label = {
                                Text(categoria)
                            }
                        )
                    }
                }
            }

            // La lista vertical muestra las secciones y sus productos.
            categoriasTienda.filter { it != "Todas" }.forEach { seccion ->
                val productosSeccion = productosVisibles.filter {
                    it.categoria == seccion
                }

                if (productosSeccion.isNotEmpty()) {
                    item {
                        Text(
                            text = seccion,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(
                        items = productosSeccion,
                        key = { it.id }
                    ) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onVerDetalle = {
                                navController.navigate(
                                    "detalle/${producto.id}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// Usamos una misma tarjeta para todos los productos.
@Composable
fun TarjetaProducto(
    producto: Producto,
    onVerDetalle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = producto.categoria,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "S/ %.2f",
                        producto.precio
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onVerDetalle) {
                    Text("Ver producto")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavHostController,
    producto: Producto?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalle del producto")
                },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { espacio ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (producto == null) {
                item {
                    Text("No se encontró el producto.")
                }
            } else {
                item {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Text("Categoría: ${producto.categoria}")
                }

                item {
                    Text(
                        text = String.format(
                            Locale.US,
                            "S/ %.2f",
                            producto.precio
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Text(producto.descripcion)
                }

                item {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver a la tienda")
                    }
                }
            }
        }
    }
}