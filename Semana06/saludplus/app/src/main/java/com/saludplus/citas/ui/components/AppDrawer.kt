package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.BluePrimary
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.ErrorRed
import com.saludplus.citas.ui.theme.NavyBlue
import com.saludplus.citas.ui.theme.PastelBlue
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    rutaActual: String?,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    content: @Composable () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val nombre = usuario?.nombre?.ifBlank { "Ivan Jara Ayala" } ?: "Ivan Jara Ayala"
    val correo = usuario?.correo?.ifBlank { "ivan.jara@saludplus.pe" } ?: "ivan.jara@saludplus.pe"

    val iniciales = nombre
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "IJ" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = usuario != null,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceWhite
            ) {
                // Header en Azul Marino #142C68
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyBlue)
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(PastelBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = correo,
                        fontSize = 12.sp,
                        color = PastelBlue
                    )
                }

                Spacer(Modifier.height(12.dp))

                val itemColors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = PastelBlue,
                    selectedIconColor = BluePrimary,
                    selectedTextColor = BluePrimary,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextPrimary
                )

                NavigationDrawerItem(
                    label = { Text("Inicio", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.HOME,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.HOME)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Sedes", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.SEDES,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.SEDES)
                    },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Sedes") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Doctores", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.MIS_DOCTORES,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.MIS_DOCTORES)
                    },
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Doctores") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Agenda", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.MIS_CITAS,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.MIS_CITAS)
                    },
                    icon = { Icon(Icons.Default.EventNote, contentDescription = "Agenda") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Resultados", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.RESULTADOS,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.RESULTADOS)
                    },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Resultados") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Mis datos", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == Rutas.PERFIL,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavegar(Rutas.PERFIL)
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Mis datos") },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )

                Spacer(Modifier.weight(1f))
                HorizontalDivider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", fontWeight = FontWeight.Bold, color = ErrorRed) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onCerrarSesion()
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión", tint = ErrorRed) },
                    colors = itemColors,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        content()
    }
}
