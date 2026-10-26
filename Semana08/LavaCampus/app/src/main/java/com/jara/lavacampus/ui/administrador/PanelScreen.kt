package com.jara.lavacampus.ui.administrador
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.ui.components.graficos.GraficoBarras
import com.jara.lavacampus.ui.components.graficos.GraficoDona
import com.jara.lavacampus.viewmodel.PanelViewModel
@Composable fun PanelScreen(vm:PanelViewModel,onSalir:()->Unit,onBandeja:()->Unit,onVerListos:()->Unit,onServicios:()->Unit,onAtrasados:()->Unit,onClientes:()->Unit,onAjustes:()->Unit,cerrandoSesion:Boolean=false,errorSesion:String?=null){
 val hoy by vm.pedidosHoy.collectAsStateWithLifecycle();val proceso by vm.enProceso.collectAsStateWithLifecycle();val recoger by vm.porRecoger.collectAsStateWithLifecycle()
 val ingresos by vm.ingresosSemana.collectAsStateWithLifecycle();val estados by vm.porEstado.collectAsStateWithLifecycle();val servicios by vm.porServicio.collectAsStateWithLifecycle()
 val listos by vm.listos.collectAsStateWithLifecycle();val atrasados by vm.atrasados.collectAsStateWithLifecycle();val cargando by vm.cargando.collectAsStateWithLifecycle();val error by vm.error.collectAsStateWithLifecycle()
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(22.dp),verticalArrangement=Arrangement.spacedBy(20.dp)){
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("LAVA / OPERACIONES",style=MaterialTheme.typography.labelLarge,modifier=Modifier.padding(top=12.dp));TextButton(onClick=onAjustes){Text("Ajustes ↗")}}}
  item{HeroStudio("BALANCE SEMANAL",importeSoles(ingresos),"Ingresos cobrados desde ${fechaPedido(vm.inicioSemana)}"){Text("CONTROL DEL DÍA / ${fechaPedido(vm.hoy)}",style=MaterialTheme.typography.labelMedium,color=com.jara.lavacampus.ui.theme.LimaStudio)}}
  if(cargando)item{LinearProgressIndicator(Modifier.fillMaxWidth())}
  error?.let{item{Text(it,color=MaterialTheme.colorScheme.error)}};errorSesion?.let{item{Text(it,color=MaterialTheme.colorScheme.error)}}
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){DatoStudio("Hoy",hoy.toString(),Modifier.weight(1f));DatoStudio("En proceso",proceso.toString(),Modifier.weight(1f));DatoStudio("Listos",recoger.toString(),Modifier.weight(1f))}}
  item{Surface(onClick=onAtrasados,modifier=Modifier.fillMaxWidth(),shape=MaterialTheme.shapes.small,color=if(atrasados>0)MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant){
   Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(if(atrasados>0)"ATENCIÓN / $atrasados ATRASADOS" else "SIN ENTREGAS ATRASADAS",style=MaterialTheme.typography.labelLarge);Text("Consultar entregas fuera de plazo →")}
  }}
  item{Column(verticalArrangement=Arrangement.spacedBy(10.dp)){
   AccesoStudio("01","Bandeja de pedidos","Gestionar lavado, casilleros y entregas",onBandeja)
   if(recoger>0 || listos.isNotEmpty())AccesoStudio("02","Listos para recoger","$recoger pedidos esperan en casilleros",onVerListos)
   AccesoStudio("03","Servicios y precios","Catálogo, disponibilidad y tarifas",onServicios)
   AccesoStudio("04","Clientes","Consulta estudiantes y sus pedidos",onClientes)
  }}
  item{CabeceraStudio("LECTURA DEL NEGOCIO","Cada pedido cuenta")}
  item{Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){Text("Estado de los pedidos",style=MaterialTheme.typography.titleLarge);GraficoDona(estados);Text("No incluye cancelados.",style=MaterialTheme.typography.bodySmall)}}}
  item{Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){Text("Ingresos por servicio",style=MaterialTheme.typography.titleLarge);Text("Importes netos cobrados esta semana",style=MaterialTheme.typography.bodySmall);GraficoBarras(servicios)}}}
  item{TextButton(onClick=onSalir,enabled=!cerrandoSesion){Text(if(cerrandoSesion)"Cerrando…" else "Cerrar sesión")}}
 }
}
