package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.ui.components.LavaTextField
import com.jara.lavacampus.viewmodel.SoportesViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.FocusDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoportesScreen(vm: SoportesViewModel, onVolver: () -> Unit) {
    val soportes by vm.soportes.collectAsStateWithLifecycle()
    var mostrarFormulario by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios  de Soporte") },
                navigationIcon = {
                    TextButton(onClick = onVolver) { Text("← Volver") }
                }
            )
        }
    ) { padding ->
        if (mostrarFormulario) {
            FormularioSoporte(
                vm = vm,
                onCancelar = { mostrarFormulario = false },
                onExito = { mostrarFormulario = false },
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    LavaButton(
                        texto = "Registrar Soporte",
                        onClick = { mostrarFormulario = true },
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (soportes.isEmpty()) {
                    item {
                        Text(
                            "No hay usuarios de soporte registrados.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(soportes, key = { it.id }) { soporte ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "${soporte.nombre} ${soporte.apellido}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("Código: ${soporte.codigo}")
                            Text("Correo: ${soporte.correo}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormularioSoporte(
    vm: SoportesViewModel,
    onCancelar: () -> Unit,
    onExito: () -> Unit,
    modifier: Modifier = Modifier
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveConfirmacion by remember { mutableStateOf("") }
    var mostrarClave by remember { mutableStateOf(false) }

    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Registrar Soporte",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            LavaTextField(
                valor = codigo,
                onCambio = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        codigo = newValue
                        vm.limpiarError()
                    }
                },
                etiqueta = "Código",
                habilitado = !cargando,
                maxLongitud = 12,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            Text(
                "Ingresa de 4 a 12 números, sin espacios ni letras.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }

        item {
            LavaTextField(
                valor = nombre,
                onCambio = { nombre = it; vm.limpiarError() },
                etiqueta = "Nombre",
                habilitado = !cargando,
                maxLongitud = 60,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            Text(
                "De 2 a 60 caracteres. Solo letras, espacios, guiones o apóstrofes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }

        item {
            LavaTextField(
                valor = apellido,
                onCambio = { apellido = it; vm.limpiarError() },
                etiqueta = "Apellido",
                habilitado = !cargando,
                maxLongitud = 60,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            Text(
                "De 2 a 60 caracteres. Solo letras, espacios, guiones o apóstrofes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }

        item {
            LavaTextField(
                valor = correo,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo",
                habilitado = !cargando,
                maxLongitud = 254,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            Text(
                "Ingresa un correo válido, por ejemplo nombre@correo.com.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }

        item {
            LavaTextField(
                valor = clave,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                habilitado = !cargando,
                maxLongitud = 64,
                esClave = !mostrarClave,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            Text(
                "Usa entre 8 y 64 caracteres, con al menos una letra y un número. Puedes incluir símbolos como @, #, $ o !. No se permiten espacios ni saltos de línea.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = mostrarClave, onCheckedChange = { mostrarClave = it })
                Text("Mostrar contraseñas", style = MaterialTheme.typography.bodySmall)
            }
            
            // Password criteria indicators
            Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                CriteriaText(text = "De 8 a 64 caracteres.", met = clave.length in 8..64)
                CriteriaText(text = "Al menos una letra.", met = clave.any { it.isLetter() })
                CriteriaText(text = "Al menos un número.", met = clave.any { it.isDigit() })
                CriteriaText(text = "Sin espacios ni saltos de línea.", met = clave.isNotEmpty() && !clave.contains(Regex("\\s")))
            }
        }

        item {
            LavaTextField(
                valor = claveConfirmacion,
                onCambio = { claveConfirmacion = it; vm.limpiarError() },
                etiqueta = "Confirmar Contraseña",
                habilitado = !cargando,
                maxLongitud = 64,
                esClave = !mostrarClave,
                onListo = { foco.clearFocus() }
            )
            Text(
                "Repite exactamente la clave anterior.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
            if (clave.isNotEmpty() && claveConfirmacion.isNotEmpty() && clave != claveConfirmacion) {
                Text(
                    "Las claves no coinciden.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                )
            }
        }

        if (error != null) {
            item {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onCancelar,
                    enabled = !cargando,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                LavaButton(
                    texto = "Guardar",
                    onClick = {
                        foco.clearFocus()
                        vm.registrarSoporte(codigo, nombre, apellido, correo, clave, claveConfirmacion, onExito)
                    },
                    color = MaterialTheme.colorScheme.primary,
                    cargando = cargando,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CriteriaText(text: String, met: Boolean) {
    Text(
        text = if (met) "✓ $text" else "✗ $text",
        color = if (met) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}
