package com.tecsup.avisoscampus.ui

import android.content.*
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import com.tecsup.avisoscampus.model.*
import com.tecsup.avisoscampus.notificacion.Notificador

@Composable fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
 Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) { Text(titulo,color=MaterialTheme.colorScheme.primary,style=MaterialTheme.typography.titleMedium); contenido() } }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun PantallaAvisos(vm: AvisosViewModel) {
 val contexto=LocalContext.current
 val permiso by vm.permiso.collectAsState()
 val pedir=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { vm.permisoRespondido(it) }
 LaunchedEffect(Unit) { if(Notificador.tienePermiso(contexto)) vm.permisoRespondido(true) }
 Scaffold(topBar={ TopAppBar(title={Text("AvisosCampus")},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=Color.White)) }) { padding ->
 Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
 Tarjeta("Permiso de notificaciones") {
  Text(when(permiso) { Permiso.SinPedir -> "Sin pedir"; Permiso.Concedido -> "Permitido"; Permiso.Denegado -> "Denegado" },color=when(permiso) { Permiso.SinPedir -> Color.Gray; Permiso.Concedido -> Color(0xFF238636); Permiso.Denegado -> Color.Red })
  Button(onClick={ if(Build.VERSION.SDK_INT>=33) pedir.launch(android.Manifest.permission.POST_NOTIFICATIONS) else vm.permisoRespondido(true) }) { Text("Permitir notificaciones") }
 }
 }
 }
}
