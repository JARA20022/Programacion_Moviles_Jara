package com.jara.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.viewmodel.ServiciosViewModel
import com.jara.lavacampus.viewmodel.SesionViewModel
import com.jara.lavacampus.ui.components.IconoServicio
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteInicioScreen(
    vm: SesionViewModel,
    serviciosVm: ServiciosViewModel
) {
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()
    val servicios by serviciosVm.servicios.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio Soporte") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "¡Bienvenido(a), ${usuario?.nombre}!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Catálogo de Servicios:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            items(servicios.filter { it.activo }, key = { it.id }) { servicio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        IconoServicio(icono = servicio.icono, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(servicio.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(servicio.descripcion, style = MaterialTheme.typography.bodyMedium)
                            Text("S/ ${String.format(Locale.US, "%.2f", servicio.precio)} por ${servicio.unidad}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}
