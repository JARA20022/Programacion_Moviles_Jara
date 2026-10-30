package com.jara.lavacampus.ui.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jara.lavacampus.model.Validaciones
import com.jara.lavacampus.ui.components.*
import com.jara.lavacampus.viewmodel.NuevoPedidoViewModel
import java.util.Locale

@Composable
fun NuevoPedidoScreen(vm:NuevoPedidoViewModel,onVolver:()->Unit,onCreado:()->Unit) {
 val estado by vm.estado.collectAsStateWithLifecycle()
 LaunchedEffect(estado.pedidoCreado) { if(estado.pedidoCreado!=null)onCreado() }
 BackHandler(enabled=estado.guardando) { /* La transacción debe terminar antes de salir. */ }
 Scaffold(modifier=Modifier.fillMaxSize().imePadding(),containerColor=Color.Transparent,contentWindowInsets=WindowInsets(0,0,0,0),bottomBar={
  Surface(color=MaterialTheme.colorScheme.surface,shadowElevation=12.dp) {
   Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=12.dp),horizontalArrangement=Arrangement.spacedBy(16.dp),verticalAlignment=Alignment.CenterVertically) {
    Column(Modifier.weight(1f)){Text("Total a pagar",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.onSurfaceVariant);Text(importeSoles(estado.total),style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)}
    Button(onClick=vm::confirmar,enabled=estado.puedeConfirmar,shape=RoundedCornerShape(18.dp),contentPadding=PaddingValues(horizontal=22.dp,vertical=16.dp)){Text(if(estado.guardando)"Guardando…" else "Confirmar")}
   }
  }
 }) { padding ->
  LazyColumn(Modifier.fillMaxSize().padding(padding),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(18.dp)) {
   item {
    TextButton(onClick=onVolver,enabled=!estado.guardando){Text("← Volver")}
    CabeceraStudio("ARMA TU PEDIDO","¿Qué cuidamos hoy?","Elige servicios y cantidades. El total se calcula al instante.")
   }
   estado.aviso?.let { item { Surface(shape=RoundedCornerShape(20.dp),color=MaterialTheme.colorScheme.primaryContainer){Text(it,Modifier.padding(18.dp),color=MaterialTheme.colorScheme.onPrimaryContainer)} } }
   if(estado.cargando || estado.cargandoRepeticion)item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
   if(!estado.recibirPedidos)item { Text("La lavandería no está recibiendo pedidos.",color=MaterialTheme.colorScheme.error) }
   if(!estado.cargando && estado.servicios.isEmpty())item { Text("No hay servicios disponibles en este momento.") }
   items(estado.servicios,key={it.id}) { servicio ->
    val esKg=servicio.unidad=="KG"
    val cantidad=estado.cantidades[servicio.id]?:0.0
    val unidad=when(servicio.unidad){"KG"->"kg";"PRENDA"->"prenda";else->"unidad"}
    Surface(shape=RoundedCornerShape(26.dp),color=if(cantidad>0)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
      contentColor=if(cantidad>0)MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface) {
     Column(Modifier.fillMaxWidth().padding(20.dp).animateContentSize(),verticalArrangement=Arrangement.spacedBy(12.dp)) {
      Row(horizontalArrangement=Arrangement.spacedBy(14.dp),verticalAlignment=Alignment.CenterVertically) {
       IconoStudio("servicios",Modifier.size(32.dp))
       Column(Modifier.weight(1f)){Text(servicio.nombre,style=MaterialTheme.typography.titleLarge);Text("${importeSoles(servicio.precio)} / $unidad · ${servicio.horasEntrega} h",style=MaterialTheme.typography.bodyMedium)}
      }
      Text(servicio.descripcion,style=MaterialTheme.typography.bodyMedium)
      Text(if(esKg)"De 1 a 10 kg · pasos de 0.5" else "De 1 a 30 · pasos de 1",style=MaterialTheme.typography.bodySmall)
      SelectorCantidad(cantidad=cantidad,paso=if(esKg)0.5 else 1.0,minimo=1.0,maximo=if(esKg)10.0 else 30.0,
        onCambio={delta->vm.cambiarCantidad(servicio.id,delta)},habilitado=!estado.cargandoRepeticion && !estado.guardando && estado.pedidoCreado==null,servicio=servicio.nombre)
      if(cantidad>0)Text("Importe: ${importeSoles(Validaciones.subtotal(cantidad,servicio.precio))}",fontWeight=FontWeight.Bold)
      if (cantidad > 0 && servicio.nombre == "OTROS SERVICIOS") {
          val descripcionOtros = estado.descripcionesOtros[servicio.id] ?: ""
          OutlinedTextField(
              value = descripcionOtros,
              onValueChange = { vm.cambiarDescripcionOtros(servicio.id, it) },
              label = { Text("¿Qué servicio necesitas?") },
              placeholder = { Text("Ej. Lavado de zapatillas") },
              supportingText = { Text("Describe el servicio solicitado, entre 5 y 200 caracteres") },
              modifier = Modifier.fillMaxWidth(),
              enabled = !estado.cargandoRepeticion && !estado.guardando && estado.pedidoCreado == null,
              shape = RoundedCornerShape(12.dp)
          )
      }
     }
    }
   }
   item {
    OutlinedTextField(value=estado.nota,onValueChange=vm::cambiarNota,label={Text("Una indicación especial (opcional)")},supportingText={Text("${estado.nota.length}/300")},modifier=Modifier.fillMaxWidth(),enabled=!estado.cargandoRepeticion && !estado.guardando && estado.pedidoCreado==null,minLines=2,maxLines=4,shape=RoundedCornerShape(20.dp))
   }
   item {
    Surface(shape=RoundedCornerShape(28.dp),color=MaterialTheme.colorScheme.surface) {
     Column(Modifier.fillMaxWidth().padding(22.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
      Text("Todo claro antes de confirmar",style=MaterialTheme.typography.titleLarge)
      if(estado.cantidades.isEmpty())Text("Agrega un servicio para empezar.",color=MaterialTheme.colorScheme.onSurfaceVariant)
      estado.servicios.filter{(estado.cantidades[it.id]?:0.0)>0}.forEach { servicio ->
       val cantidad=estado.cantidades.getValue(servicio.id)
       val numero=if(servicio.unidad=="KG")String.format(Locale.US,"%.1f kg",cantidad) else "${cantidad.toInt()} ${if(servicio.unidad=="PRENDA")"prendas" else "unidades"}"
       Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)){Text("${servicio.nombre} · $numero",Modifier.weight(1f));Text(importeSoles(Validaciones.subtotal(cantidad,servicio.precio)))}
      }
      HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant)
      Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("Subtotal");Text(importeSoles(estado.subtotal))}
      if(estado.descuento>0)Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("Ahorro por volumen",color=MaterialTheme.colorScheme.tertiary);Text("−${importeSoles(estado.descuento)}",fontWeight=FontWeight.Bold,color=MaterialTheme.colorScheme.tertiary)}
      Text("Más de 5 kg: 10 % de descuento solo en lavado por kilo.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
      Text(estado.fechaEntrega?.let{"Entrega estimada: ${fechaPedido(it)}"}?:"Selecciona un servicio para calcular la entrega.",fontWeight=FontWeight.SemiBold)
      Text("Los domingos no hay atención.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
     }
    }
   }
   estado.error?.let { item { Text(it,color=MaterialTheme.colorScheme.error,fontWeight=FontWeight.SemiBold) } }
  }
 }
}
