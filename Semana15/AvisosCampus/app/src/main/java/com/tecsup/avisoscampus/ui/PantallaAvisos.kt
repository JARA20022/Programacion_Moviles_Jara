package com.tecsup.avisoscampus.ui

import android.content.*
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tecsup.avisoscampus.model.*
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@Composable fun Panel(modifier: Modifier=Modifier,content: @Composable ColumnScope.() -> Unit) {
 Surface(modifier=modifier.fillMaxWidth().animateContentSize(),shape=RoundedCornerShape(24.dp),color=CampusSurface,border=BorderStroke(1.dp,Color.White.copy(alpha=0.06f))) {
  Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),content=content)
 }
}
// Conserva el componente del proyecto base para compatibilidad con otros archivos.
@Composable fun Tarjeta(titulo: String,contenido: @Composable ColumnScope.() -> Unit) {
 Panel { Text(titulo,style=MaterialTheme.typography.titleMedium); contenido() }
}
@Composable private fun SectionLabel(number: String,title: String,color: Color) {
 Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
  Text(number,Modifier.background(color.copy(alpha=0.12f),RoundedCornerShape(10.dp)).padding(8.dp),color=color,style=MaterialTheme.typography.labelLarge)
  Text(title,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)
 }
}
@Composable private fun Header(count: Int,allowed: Boolean) {
 val infinite=rememberInfiniteTransition(label="ambient")
 val glow by infinite.animateFloat(initialValue=0.14f,targetValue=0.28f,animationSpec=infiniteRepeatable(tween(2800),RepeatMode.Reverse),label="glow")
 Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF45307C),Color(0xFF24345D),Color(0xFF153C43))),RoundedCornerShape(28.dp)).padding(24.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) {
   Text("CAMPUS / PERSONAL",color=CampusMint,style=MaterialTheme.typography.labelSmall,letterSpacing=2.sp)
   Box(Modifier.size(10.dp).background(if(allowed) CampusMint else CampusAmber,RoundedCornerShape(50)))
  }
  Text("AvisosCampus",style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.ExtraBold)
  Text("Tu día, bajo control.",color=Color(0xFFE0DAF5),style=MaterialTheme.typography.titleMedium)
  Text("Avisos, recordatorios y actividad del teléfono en un solo lugar.",color=CampusMuted)
  Row(Modifier.background(Color.White.copy(alpha=glow),RoundedCornerShape(12.dp)).padding(12.dp),horizontalArrangement=Arrangement.spacedBy(16.dp)) {
   Text("$count guardados",modifier=Modifier.weight(1f),fontWeight=FontWeight.Bold)
   Text(if(allowed) "Avisos habilitados" else "Revisa el permiso",modifier=Modifier.weight(1f),color=if(allowed) CampusMint else CampusAmber)
  }
 }
}
@Composable fun PantallaAvisos(vm: AvisosViewModel) {
 val context=LocalContext.current
 val owner=LocalLifecycleOwner.current
 val permiso by vm.permiso.collectAsState()
 val faltan by vm.faltan.collectAsState()
 val escuchando by vm.escuchandoAvion.collectAsState()
 val avisos by vm.avisos.collectAsState()
 val mensaje by vm.mensaje.collectAsState()
 val pedir=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { vm.permisoRespondido(it) }
 var filtro by rememberSaveable { mutableStateOf("Todos") }
 var confirmar by rememberSaveable { mutableStateOf(false) }
 val progress by animateFloatAsState(targetValue=if(faltan==null) 0f else (10-(faltan ?: 10))/10f,animationSpec=tween(450),label="countdown")
 LaunchedEffect(vm) { vm.eventos.collect { if(!Notificador.mostrar(context,it)) vm.noSePudoMostrar() } }
 DisposableEffect(owner,context) {
  val observer=LifecycleEventObserver { _,event ->
   if(event==Lifecycle.Event.ON_RESUME) {
    val granted=Notificador.tienePermiso(context)
    if(granted || vm.permiso.value!=Permiso.SinPedir) vm.permisoRespondido(granted)
   }
  }
  owner.lifecycle.addObserver(observer)
  if(Notificador.tienePermiso(context)) vm.permisoRespondido(true)
  onDispose { owner.lifecycle.removeObserver(observer) }
 }
 DisposableEffect(escuchando,context) {
  if(escuchando) {
   val receiver=ModoAvionReceiver { vm.alCambiarModoAvion(it) }
   ContextCompat.registerReceiver(context,receiver,IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),ContextCompat.RECEIVER_NOT_EXPORTED)
   onDispose { context.unregisterReceiver(receiver) }
  } else { onDispose {} }
 }
 val ajustes={context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE,context.packageName)); Unit}
 val controls: @Composable () -> Unit = {
  Panel {
   SectionLabel("01","Permiso de notificaciones",CampusPurple)
   val estado=when(permiso) { Permiso.SinPedir -> "Sin pedir"; Permiso.Concedido -> "Permitido"; Permiso.Denegado -> "Denegado" }
   Text(estado,color=when(permiso) { Permiso.SinPedir -> CampusMuted; Permiso.Concedido -> CampusMint; Permiso.Denegado -> Color(0xFFFFA5AC) },style=MaterialTheme.typography.titleLarge)
   Text("Decide si quieres recibir avisos en el panel del teléfono.",color=CampusMuted)
   if(permiso!=Permiso.Concedido) Button(onClick={if(Build.VERSION.SDK_INT>=33) pedir.launch(android.Manifest.permission.POST_NOTIFICATIONS) else vm.permisoRespondido(true)},modifier=Modifier.fillMaxWidth()) { Text("Permitir notificaciones") }
   TextButton(onClick=ajustes) { Text("Abrir ajustes") }
  }
  Panel {
   SectionLabel("02","Avisos",CampusMint)
   Text("Una entrega, un aviso.",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
   Text("Registra una tarea y consulta su notificación expandida.",color=CampusMuted)
   Button(onClick=vm::avisarAhora,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=CampusMint,contentColor=CampusBackground)) { Text("Mostrar aviso",Modifier.padding(vertical=5.dp)) }
  }
  Panel {
   SectionLabel("03","Recordatorio",CampusAmber)
   Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)) {
    Box(Modifier.size(86.dp),contentAlignment=Alignment.Center) {
     CircularProgressIndicator(progress={progress},modifier=Modifier.fillMaxSize(),color=CampusAmber,trackColor=CampusAmber.copy(alpha=0.12f),strokeWidth=5.dp)
     Text(if(faltan==null) "10 s" else "${faltan} s",fontSize=24.sp,fontWeight=FontWeight.Bold,color=CampusAmber)
    }
    Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)) {
     Text(if(faltan==null) "Tu próxima pausa" else "Faltan $faltan s",fontWeight=FontWeight.Bold)
     Text("El recordatorio usa el canal Urgentes.",color=CampusMuted,style=MaterialTheme.typography.bodySmall)
    }
   }
   Button(onClick={vm.recordarEn()},enabled=faltan==null,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=CampusAmber,contentColor=CampusBackground)) { Text("Recordar en 10 s") }
   AnimatedVisibility(visible=faltan!=null) { OutlinedButton(onClick=vm::cancelarRecordatorio,enabled=faltan!=null,modifier=Modifier.fillMaxWidth()) { Text("Cancelar") } }
  }
  Panel {
   SectionLabel("04","Receptor de anuncios",CampusMint)
   Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
    Column(Modifier.weight(1f)) { Text("Escuchar el modo avión",fontWeight=FontWeight.Bold); Text(if(escuchando) "Escucha activada" else "Escucha pausada",color=if(escuchando) CampusMint else CampusMuted) }
    Switch(checked=escuchando,onCheckedChange=vm::cambiarEscucha)
   }
   Text("Activa o desactiva el modo avión desde los ajustes rápidos para generar un aviso.",color=CampusMuted,style=MaterialTheme.typography.bodySmall)
  }
 }
 val history: @Composable () -> Unit = {
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) {
   Column { Text("Actividad reciente",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold); Text("Historial · guardado en el dispositivo",color=CampusMuted,style=MaterialTheme.typography.bodySmall) }
  }
  Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   listOf("Todos","Aviso","Recordatorio","Modo avión").forEach { tag -> FilterChip(selected=filtro==tag,onClick={filtro=tag},label={Text(tag)}) }
  }
  HistorialAvisos(avisos.filter { filtro=="Todos" || it.origen.texto==filtro })
  if(avisos.isNotEmpty()) TextButton(onClick={confirmar=true}) { Text("Borrar historial",color=Color(0xFFFFA5AC)) }
  Text("Se muestran los 20 más recientes del filtro seleccionado.",color=CampusMuted,style=MaterialTheme.typography.bodySmall)
 }
 Surface(Modifier.fillMaxSize(),color=CampusBackground) {
  BoxWithConstraints(Modifier.fillMaxSize().safeDrawingPadding()) {
   val wide=maxWidth>=700.dp
   Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(if(wide) 28.dp else 16.dp),verticalArrangement=Arrangement.spacedBy(20.dp)) {
    Header(avisos.size,permiso==Permiso.Concedido)
    AnimatedVisibility(visible=mensaje!=null) {
     Surface(color=MaterialTheme.colorScheme.errorContainer,shape=RoundedCornerShape(18.dp)) {
      Column(Modifier.padding(16.dp)) { Text(mensaje.orEmpty(),color=MaterialTheme.colorScheme.onErrorContainer); TextButton(onClick=vm::cerrarMensaje) { Text("Entendido") } }
     }
    }
    if(wide) {
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(24.dp),verticalAlignment=Alignment.Top) {
      Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(16.dp)) { controls() }
      Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(16.dp)) { history() }
     }
    } else { controls(); history() }
    Text("AVISOSCAMPUS  /  TU ESPACIO DE AVISOS",color=CampusMuted,style=MaterialTheme.typography.labelSmall,modifier=Modifier.padding(vertical=12.dp))
   }
  }
 }
 if(confirmar) AlertDialog(onDismissRequest={confirmar=false},title={Text("¿Borrar el historial?")},text={Text("Se eliminarán los avisos guardados en este dispositivo.")},confirmButton={TextButton(onClick={vm.limpiarHistorial(); confirmar=false}) { Text("Borrar") }},dismissButton={TextButton(onClick={confirmar=false}) { Text("Conservar") }})
}
