package com.jara.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.ui.components.graficos.GraficoBarras
import com.jara.lavacampus.ui.components.graficos.GraficoDona
import com.jara.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm:PanelViewModel,onSalir:()->Unit,onBandeja:()->Unit,onVerListos:()->Unit,onServicios:()->Unit,onAtrasados:()->Unit,onClientes:()->Unit,onAjustes:()->Unit,cerrandoSesion:Boolean=false,errorSesion:String?=null) {
 val hoy by vm.pedidosHoy.collectAsStateWithLifecycle()
 val proceso by vm.enProceso.collectAsStateWithLifecycle()
 val recoger by vm.porRecoger.collectAsStateWithLifecycle()
 val ingresos by vm.ingresosSemana.collectAsStateWithLifecycle()
 val estados by vm.porEstado.collectAsStateWithLifecycle()
 val servicios by vm.porServicio.collectAsStateWithLifecycle()
 val atrasados by vm.atrasados.collectAsStateWithLifecycle()
 val cargando by vm.cargando.collectAsStateWithLifecycle()
 val error by vm.error.collectAsStateWithLifecycle()
 var grafico by rememberSaveable { mutableStateOf(0) }
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(20.dp)) {
  item {
   EntradaStudio {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
     Column(Modifier.weight(1f)) {
      Text("LavaCampus",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary)
      Text("Tu operación.",style=MaterialTheme.typography.headlineLarge)
      Text(fechaPedido(vm.hoy),color=MaterialTheme.colorScheme.onSurfaceVariant,style=MaterialTheme.typography.bodyMedium)
     }
     FilledTonalIconButton(onClick=onAjustes,modifier=Modifier.size(48.dp)) {
      androidx.compose.foundation.layout.Box(Modifier.semantics { contentDescription="Abrir ajustes" }){IconoStudio("ajustes")}
     }
    }
   }
  }
  item {
   EntradaStudio(1) {
    Surface(shape=RoundedCornerShape(30.dp),color=Color(0xFFD7C6FF),contentColor=Color(0xFF281843)) {
     Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFFE3D9FF),Color(0xFFC1A4FC))))) {
      OrbitaStudio(Modifier.align(Alignment.CenterEnd).size(150.dp))
      Column(Modifier.fillMaxWidth().padding(24.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
       Text("INGRESOS DE LA SEMANA",style=MaterialTheme.typography.labelMedium)
       Text(importeSoles(ingresos),style=MaterialTheme.typography.displaySmall)
       Text("Cobrado desde ${fechaPedido(vm.inicioSemana)}",style=MaterialTheme.typography.bodySmall)
       HorizontalDivider(color=Color(0xFF281843).copy(alpha=.16f))
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(16.dp)) {
        Column(Modifier.weight(1f)){Text(hoy.toString(),style=MaterialTheme.typography.headlineMedium);Text("pedidos hoy",style=MaterialTheme.typography.bodySmall)}
        Column(Modifier.weight(1f)){Text(proceso.toString(),style=MaterialTheme.typography.headlineMedium);Text("en proceso",style=MaterialTheme.typography.bodySmall)}
        Column(Modifier.weight(1f)){Text(recoger.toString(),style=MaterialTheme.typography.headlineMedium);Text("por recoger",style=MaterialTheme.typography.bodySmall)}
       }
      }
     }
    }
   }
  }
  if(cargando)item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
  error?.let { item { Text(it,color=MaterialTheme.colorScheme.error) } }
  errorSesion?.let { item { Text(it,color=MaterialTheme.colorScheme.error) } }
  item {
   EntradaStudio(2) {
    Surface(onClick=onAtrasados,shape=RoundedCornerShape(22.dp),color=if(atrasados>0)MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,contentColor=if(atrasados>0)MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface) {
     Row(Modifier.fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)) {
      IconoStudio("reloj")
      Column(Modifier.weight(1f)){Text(if(atrasados>0)"$atrasados pedidos fuera de plazo" else "Entregas al día",fontWeight=FontWeight.Bold);Text("Revisar la bandeja",style=MaterialTheme.typography.bodySmall)}
      IconoStudio("flecha")
     }
    }
   }
  }
  item { Text("Todo bajo control",style=MaterialTheme.typography.titleLarge) }
  item {
   EntradaStudio(3) {
    Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
     Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
      ModuloStudio("Pedidos","Gestionar entregas","pedidos",onBandeja,Modifier.weight(1f))
      ModuloStudio("Recoger","$recoger pedidos listos","listo",onVerListos,Modifier.weight(1f),coral=true)
     }
     Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
      ModuloStudio("Servicios","Catálogo y precios","servicios",onServicios,Modifier.weight(1f),coral=true)
      ModuloStudio("Clientes","Consultar estudiantes","persona",onClientes,Modifier.weight(1f))
     }
    }
   }
  }
  item {
   Surface(shape=RoundedCornerShape(28.dp),color=MaterialTheme.colorScheme.surface) {
    Column(Modifier.fillMaxWidth().padding(20.dp),verticalArrangement=Arrangement.spacedBy(18.dp)) {
     Text("El campus en cifras",style=MaterialTheme.typography.titleLarge)
     Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
      FilterChip(selected=grafico==0,onClick={grafico=0},label={Text("Pedidos")})
      FilterChip(selected=grafico==1,onClick={grafico=1},label={Text("Ingresos")})
     }
     if(grafico==0) { GraficoDona(estados);Text("Distribución sin pedidos cancelados.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
     else { GraficoBarras(servicios);Text("Importes netos cobrados esta semana.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
    }
   }
  }
  item { TextButton(onClick=onSalir,enabled=!cerrandoSesion){Text(if(cerrandoSesion)"Cerrando…" else "Cerrar sesión")} }
 }
}
