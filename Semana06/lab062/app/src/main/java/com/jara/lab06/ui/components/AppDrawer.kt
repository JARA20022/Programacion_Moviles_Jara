package com.jara.lab06.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    rutaActual: String?,
    onSeleccionar: (String) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            // Datos del usuario que aparecen en el encabezado.
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "IJ",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "IVAN JARA AYALA",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ivan.jara.a@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall
                )

                Text("Estudiante")
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // selected resalta la pantalla que está abierta.
            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = rutaActual == "inicio" ||
                        rutaActual == "detalle/{productoId}",
                icon = { Icon(Icons.Default.Home, null) },
                onClick = { onSeleccionar("inicio") }
            )

            NavigationDrawerItem(
                label = { Text("Mis pedidos") },
                selected = rutaActual == "pedidos",
                icon = { Icon(Icons.Default.ShoppingCart, null) },
                onClick = { onSeleccionar("pedidos") }
            )

            NavigationDrawerItem(
                label = { Text("Favoritos") },
                selected = rutaActual == "favoritos",
                icon = { Icon(Icons.Default.Favorite, null) },
                onClick = { onSeleccionar("favoritos") }
            )

            NavigationDrawerItem(
                label = { Text("Perfil") },
                selected = rutaActual == "perfil",
                icon = { Icon(Icons.Default.Person, null) },
                onClick = { onSeleccionar("perfil") }
            )
        }
    }
}