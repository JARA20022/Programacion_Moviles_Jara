package com.jara.lab06.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    onSeleccionar: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            onClick = { onSeleccionar("inicio") },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = false,
            onClick = { onSeleccionar("pedidos") },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = false,
            onClick = { onSeleccionar("favoritos") },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = false,
            onClick = { onSeleccionar("perfil") },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}