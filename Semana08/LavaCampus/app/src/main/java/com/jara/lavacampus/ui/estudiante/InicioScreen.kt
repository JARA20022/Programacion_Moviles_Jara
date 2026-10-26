package com.jara.lavacampus.ui.estudiante
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.EstadoPedido
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.ui.theme.LimaStudio
import com.jara.lavacampus.viewmodel.InicioViewModel
@Composable fun InicioScreen(vm:InicioViewModel,onNuevoPedido:()->Unit,onMisPedidos:()->Unit,onTarifas:()->Unit,onDetalle:(Int)->Unit,onSalir:()->Unit,onPerfil:()->Unit,recibirPedidos:Boolean,cerrandoSesion:Boolean=false,errorSesion:String?=null){
 val activo by vm.pedidoActivo.collectAsStateWithLifecycle();val recientes by vm.recientes.collectAsStateWithLifecycle()
 val cargando by vm.cargando.collectAsStateWithLifecycle();val error by vm.error.collectAsStateWithLifecycle()
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(22.dp),verticalArrangement=Arrangement.spacedBy(20.dp)){
  item{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("LAVA / CAMPUS",style=MaterialTheme.typography.labelLarge,modifier=Modifier.padding(top=12.dp));TextButton(onClick=onPerfil){Text("Mi cuenta ↗")}}}
  item{HeroStudio("TU ESPACIO / ${vm.estudiante.codigo}","Hola, ${vm.estudiante.nombre.substringBefore(' ')}.",if(recibirPedidos)"Hoy puedes ocuparte de lo que importa. Nosotros cuidamos tu ropa." else "La recepción está pausada. Puedes consultar tus pedidos."){
   Button(onClick=onNuevoPedido,enabled=recibirPedidos,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=LimaStudio,contentColor=com.jara.lavacampus.ui.theme.TintaCampus),contentPadding=PaddingValues(16.dp)){Text("Crear un pedido  +",fontWeight=FontWeight.Bold)}
  }}
  if(cargando)item{LinearProgressIndicator(Modifier.fillMaxWidth())}
  error?.let{item{Text(it,color=MaterialTheme.colorScheme.error)}};errorSesion?.let{item{Text(it,color=MaterialTheme.colorScheme.error)}}
  item{CabeceraStudio("EN MOVIMIENTO","Tu ropa, paso a paso")}
  val datos=activo
  if(datos!=null)item{Surface(onClick={onDetalle(datos.pedido.id)},shape=MaterialTheme.shapes.large,color=MaterialTheme.colorScheme.primaryContainer){
   Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("PEDIDO #${datos.pedido.id}",style=MaterialTheme.typography.labelLarge);Text(importeSoles(datos.pedido.total),fontWeight=FontWeight.Bold)}
    Text("Entrega: ${fechaPedido(datos.pedido.fechaEntrega)}");StepperEstado(EstadoPedido.valueOf(datos.pedido.estado))
    if(datos.pedido.estado=="LISTO")Text("Tu ropa está lista · casillero ${datos.pedido.casillero.orEmpty()}",fontWeight=FontWeight.Bold)
    Text("Abrir seguimiento →",color=MaterialTheme.colorScheme.primary)
   }
  }}else if(!cargando && error==null)item{Text("Sin pedidos activos. Tu próximo cuidado empieza arriba.",color=MaterialTheme.colorScheme.onSurfaceVariant)}
  item{Column(verticalArrangement=Arrangement.spacedBy(10.dp)){AccesoStudio("01","Mis pedidos","Seguimiento, historial y repetir pedido",onMisPedidos);AccesoStudio("02","Explorar servicios","Tarifas y tiempos de entrega",onTarifas)}}
  item{CabeceraStudio("ARCHIVO PERSONAL","Últimos cuidados")}
  items(recientes,key={it.pedido.id}){PedidoCard(it,{onDetalle(it.pedido.id)})}
  if(!cargando && recientes.isEmpty())item{Text("Tus pedidos aparecerán aquí.")}
  item{TextButton(onClick=onSalir,enabled=!cerrandoSesion){Text(if(cerrandoSesion)"Cerrando…" else "Cerrar sesión")}}
 }
}
