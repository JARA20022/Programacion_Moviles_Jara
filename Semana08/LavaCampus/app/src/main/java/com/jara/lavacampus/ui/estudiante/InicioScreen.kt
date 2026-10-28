package com.jara.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(vm:InicioViewModel,onNuevoPedido:()->Unit,onMisPedidos:()->Unit,onTarifas:()->Unit,onDetalle:(Int)->Unit,onSalir:()->Unit,onPerfil:()->Unit,recibirPedidos:Boolean,cerrandoSesion:Boolean=false,errorSesion:String?=null) {
 val activo by vm.pedidoActivo.collectAsStateWithLifecycle()
 val recientes by vm.recientes.collectAsStateWithLifecycle()
 val cargando by vm.cargando.collectAsStateWithLifecycle()
 val error by vm.error.collectAsStateWithLifecycle()
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(22.dp)) {
  item {
   EntradaStudio {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)) {
     Column(Modifier.weight(1f)) { Text("LavaCampus",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary);Text("Hola, ${vm.estudiante.nombre.substringBefore(' ')}.",style=MaterialTheme.typography.headlineLarge) }
     Surface(onClick=onPerfil,shape=CircleShape,color=MaterialTheme.colorScheme.primaryContainer,contentColor=MaterialTheme.colorScheme.onPrimaryContainer) { Text(vm.estudiante.nombre.take(1).uppercase(),Modifier.padding(18.dp),style=MaterialTheme.typography.titleLarge) }
    }
   }
  }
  item {
   EntradaStudio(1) {
    Surface(shape=RoundedCornerShape(32.dp),color=Color(0xFF31204D),contentColor=Color.White) {
     Box(Modifier.background(Brush.linearGradient(listOf(Color(0xFF654292),Color(0xFF251F3D))))) {
      OrbitaStudio(Modifier.align(Alignment.TopEnd).size(180.dp))
      Column(Modifier.padding(24.dp),verticalArrangement=Arrangement.spacedBy(18.dp)) {
       Text("MÁS TIEMPO PARA TI",style=MaterialTheme.typography.labelMedium,color=Color(0xFFE0D1FF))
       Text("Tu ropa,\nlista para\nlo que viene.",style=MaterialTheme.typography.displaySmall)
       Text(if(recibirPedidos)"Lavado, planchado y cuidado en tu campus." else "Recepción pausada. Puedes consultar tus pedidos.",style=MaterialTheme.typography.bodyMedium,color=Color(0xFFE6DDED))
       Button(onClick=onNuevoPedido,enabled=recibirPedidos,modifier=Modifier.fillMaxWidth().heightIn(min=56.dp),shape=RoundedCornerShape(18.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFFFB49F),contentColor=Color(0xFF402119))) {
        Text("Nuevo pedido",Modifier.weight(1f),fontWeight=FontWeight.Bold);IconoStudio("mas")
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
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically) { Text("En seguimiento",style=MaterialTheme.typography.titleLarge);TextButton(onClick=onMisPedidos){Text("Ver todos")} }
  }
  val datos=activo
  if(datos!=null)item {
   Surface(onClick={onDetalle(datos.pedido.id)},shape=RoundedCornerShape(26.dp),color=MaterialTheme.colorScheme.surface) {
    Column(Modifier.fillMaxWidth().padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
     Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) { Text("Pedido #${datos.pedido.id}",style=MaterialTheme.typography.titleMedium);Text(importeSoles(datos.pedido.total),fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.primary) }
     Text("Entrega: ${fechaPedido(datos.pedido.fechaEntrega)}",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
     StepperEstado(EstadoPedido.valueOf(datos.pedido.estado))
     if(datos.pedido.estado=="LISTO")Text("Listo en el casillero ${datos.pedido.casillero.orEmpty()}",fontWeight=FontWeight.Bold)
     Text("Ver detalle →",color=MaterialTheme.colorScheme.primary)
    }
   }
  } else if(!cargando && error==null)item { Surface(shape=RoundedCornerShape(24.dp),color=MaterialTheme.colorScheme.surface) { Row(Modifier.fillMaxWidth().padding(22.dp),horizontalArrangement=Arrangement.spacedBy(16.dp)){IconoStudio("servicios",color=MaterialTheme.colorScheme.primary);Text("Aún no tienes pedidos activos.\nEmpieza con tu primer lavado.",style=MaterialTheme.typography.bodyMedium)} } }
  item {
   Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
    ModuloStudio("Mis pedidos","Historial y repetición","pedidos",onMisPedidos,Modifier.weight(1f))
    ModuloStudio("Servicios","Explora las tarifas","servicios",onTarifas,Modifier.weight(1f),coral=true)
   }
  }
  item { Text("Tu actividad reciente",style=MaterialTheme.typography.titleLarge) }
  items(recientes,key={it.pedido.id}) { PedidoCard(it,{onDetalle(it.pedido.id)}) }
  if(!cargando && recientes.isEmpty())item { Text("Aquí aparecerán tus próximos pedidos.",color=MaterialTheme.colorScheme.onSurfaceVariant) }
  item { TextButton(onClick=onSalir,enabled=!cerrandoSesion){Text(if(cerrandoSesion)"Cerrando…" else "Cerrar sesión")} }
 }
}
