package com.jara.lavacampus.ui.components
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.ui.theme.LimaStudio
import com.jara.lavacampus.ui.theme.TintaCampus
@Composable fun StudioFondo(content:@Composable () -> Unit) {
 val fondo=MaterialTheme.colorScheme.background;val trama=MaterialTheme.colorScheme.onBackground.copy(alpha=.055f)
 Box(Modifier.fillMaxSize().background(fondo).drawWithCache {
  val paso=20.dp.toPx()
  onDrawBehind {var x=0f;while(x<size.width){var y=0f;while(y<size.height){drawCircle(trama,.7.dp.toPx(),Offset(x,y));y+=paso};x+=paso}}
 }){content()}
}
@Composable fun CabeceraStudio(etiqueta:String,titulo:String,detalle:String="") {
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(8.dp)) {
  Text(etiqueta.uppercase(),style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
  Text(titulo,style=MaterialTheme.typography.headlineLarge)
  if(detalle.isNotBlank())Text(detalle,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
 }
}
@Composable fun HeroStudio(etiqueta:String,titulo:String,detalle:String,modifier:Modifier=Modifier,content:@Composable ColumnScope.()->Unit={}) {
 val progreso=remember{Animatable(0f)};LaunchedEffect(Unit){progreso.animateTo(1f,tween(700))}
 Surface(modifier.fillMaxWidth(),shape=CutCornerShape(topEnd=36.dp,bottomStart=18.dp),color=TintaCampus,contentColor=Color.White){
  Box {
   Canvas(Modifier.align(Alignment.TopEnd).size(150.dp).clearAndSetSemantics{}){
    val radio=size.minDimension*.44f;val centro=Offset(size.width*.72f,size.height*.4f)
    for(i in 0..4)drawArc(LimaStudio.copy(alpha=.08f+i*.015f),-80f,300f*progreso.value,false,
     Offset(centro.x-radio+i*7.dp.toPx(),centro.y-radio+i*7.dp.toPx()),Size((radio-i*7.dp.toPx())*2,(radio-i*7.dp.toPx())*2),style=Stroke(1.dp.toPx()))
   }
   Column(Modifier.padding(24.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
    Text(etiqueta,style=MaterialTheme.typography.labelMedium,color=LimaStudio)
    Text(titulo,style=MaterialTheme.typography.displaySmall)
    Text(detalle,color=Color(0xFFC5D9D0),style=MaterialTheme.typography.bodyMedium)
    Canvas(Modifier.fillMaxWidth().height(1.dp).clearAndSetSemantics{}){drawLine(LimaStudio.copy(alpha=.35f),Offset.Zero,Offset(size.width,0f),1.dp.toPx(),pathEffect=PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(),5.dp.toPx())))}
    content()
   }
  }
 }
}
@Composable fun AccesoStudio(numero:String,titulo:String,detalle:String,onClick:()->Unit,enabled:Boolean=true) {
 Surface(onClick=onClick,enabled=enabled,modifier=Modifier.fillMaxWidth(),shape=CutCornerShape(topEnd=16.dp),color=MaterialTheme.colorScheme.surface,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant)){
  Row(Modifier.padding(18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)){
   Text(numero,style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary)
   Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)){
    Text(titulo,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)
    Text(detalle,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
   };Text("↗",style=MaterialTheme.typography.headlineSmall,color=MaterialTheme.colorScheme.primary)
  }
 }
}
@Composable fun DatoStudio(etiqueta:String,valor:String,modifier:Modifier=Modifier){
 Surface(modifier,shape=RoundedCornerShape(18.dp),color=MaterialTheme.colorScheme.surface,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant)){
  Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text(valor,style=MaterialTheme.typography.headlineLarge,color=MaterialTheme.colorScheme.primary);Text(etiqueta,style=MaterialTheme.typography.bodySmall)}
 }
}
