package com.jara.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.Rol
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.ui.components.LavaButton
import com.jara.lavacampus.ui.components.LavaTextField
import com.jara.lavacampus.ui.theme.TintaCampus
import com.jara.lavacampus.ui.theme.colorDelRol
import com.jara.lavacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    val error by vm.error.collectAsStateWithLifecycle()
    val cargando by vm.cargando.collectAsStateWithLifecycle()
    val foco = LocalFocusManager.current

    val ingresar: () -> Unit = {
        foco.clearFocus()
        vm.login(correo, clave, rol)
    }

    Column(
        modifier = Modifier.fillMaxSize().imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.fillMaxWidth()
                .background(TintaCampus, RoundedCornerShape(bottomEnd = 64.dp))
                .padding(horizontal = 28.dp, vertical = 30.dp)
        ) {
            Text(
                "LC / LAVACAMPUS",
                color = Color(0xFFFFC5AD),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "Tu tiempo,\npara ti.",
                color = Color.White,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "El cuidado de tu ropa empieza aquí.",
                color = Color(0xFFD4DAD9),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            Modifier.widthIn(max = 560.dp).fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Elige tu acceso",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Rol.entries.forEach { opcion ->
                    val seleccionado = rol == opcion
                    Surface(
                        onClick = { rol = opcion; vm.limpiarError() },
                        enabled = !cargando,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = if (seleccionado) colorDelRol(opcion)
                            else MaterialTheme.colorScheme.surface,
                        contentColor = if (seleccionado) Color.White
                            else MaterialTheme.colorScheme.onSurface,
                        border = if (seleccionado) null else BorderStroke(
                            1.dp, MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                if (opcion == Rol.CLIENTE) "01 / CAMPUS" else "02 / GESTIÓN",
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(opcion.titulo, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            LavaTextField(
                valor = correo,
                maxLongitud = Validaciones.MAX_CORREO,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo electrónico",
                habilitado = !cargando,
                onSiguiente = { foco.moveFocus(FocusDirection.Down) }
            )
            LavaTextField(
                valor = clave,
                maxLongitud = Validaciones.MAX_CLAVE,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                esClave = true,
                habilitado = !cargando,
                imeAction = ImeAction.Done,
                onListo = ingresar
            )

            error?.let { mensaje ->
                Text(
                    mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
            LavaButton(
                texto = "Ingresar",
                onClick = ingresar,
                color = colorDelRol(rol),
                cargando = cargando
            )
            Text(
                "Menos pendientes. Más campus.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
