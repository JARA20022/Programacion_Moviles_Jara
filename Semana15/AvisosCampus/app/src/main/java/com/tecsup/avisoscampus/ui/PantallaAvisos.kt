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

@Composable fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
 Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) { Text(titulo,color=MaterialTheme.colorScheme.primary,style=MaterialTheme.typography.titleMedium); contenido() } }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun PantallaAvisos(vm: AvisosViewModel) {
 val contexto=LocalContext.current
 Scaffold(topBar={ TopAppBar(title={Text("AvisosCampus")},colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=Color.White)) }) { padding ->
 Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
 Text("Proyecto base · MVVM + Repository")
 }
 }
}
