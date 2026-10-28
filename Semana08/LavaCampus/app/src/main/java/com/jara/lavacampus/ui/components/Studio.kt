package com.jara.lavacampus.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jara.lavacampus.ui.theme.*

@Composable fun StudioFondo(content: @Composable () -> Unit) {
 val fondo=MaterialTheme.colorScheme.background
 val acento=MaterialTheme.colorScheme.primary.copy(alpha=.065f)
 val grano=MaterialTheme.colorScheme.onBackground.copy(alpha=.035f)
 Box(Modifier.fillMaxSize().background(fondo).drawWithCache {
  val glow=Brush.radialGradient(listOf(acento,Color.Transparent),Offset(size.width*.95f,0f),size.width*.9f)
  val azar=kotlin.random.Random(42)
  val puntos=List(900) { Offset(azar.nextFloat()*size.width,azar.nextFloat()*size.height) }
  onDrawBehind { drawRect(glow);puntos.forEach { drawCircle(grano,.6.dp.toPx(),it) } }
 }) { content() }
}

// La animación ocurre al entrar; no hay temporizadores ni bucles de animación.
@Composable fun EntradaStudio(indice: Int = 0, content: @Composable () -> Unit) {
 val avance=remember { Animatable(0f) }
 LaunchedEffect(Unit) { avance.animateTo(1f,tween(durationMillis=420,delayMillis=(indice*65).coerceAtMost(260))) }
 Box(Modifier.graphicsLayer { alpha=avance.value;translationY=(1f-avance.value)*24.dp.toPx() }) { content() }
}

@Composable fun IconoStudio(tipo: String, modifier: Modifier=Modifier, color: Color=LocalContentColor.current) {
 Canvas(modifier.size(24.dp).clearAndSetSemantics {}) {
  val u=size.minDimension/24f;val stroke=Stroke(1.7f*u,cap=StrokeCap.Round)
  fun line(x:Float,y:Float,x2:Float,y2:Float)=drawLine(color,Offset(x*u,y*u),Offset(x2*u,y2*u),1.7f*u,StrokeCap.Round)
  when(tipo) {
   "inicio" -> {val p=Path().apply{moveTo(3*u,11*u);lineTo(12*u,3*u);lineTo(21*u,11*u);moveTo(5*u,10*u);lineTo(5*u,21*u);lineTo(19*u,21*u);lineTo(19*u,10*u)};drawPath(p,color,style=stroke);line(10f,21f,10f,15f);line(10f,15f,14f,15f);line(14f,15f,14f,21f)}
   "pedidos" -> {drawRoundRect(color,Offset(5*u,3*u),Size(14*u,18*u),androidx.compose.ui.geometry.CornerRadius(2*u),style=stroke);line(8f,8f,16f,8f);line(8f,12f,16f,12f);line(8f,16f,13f,16f)}
   "persona" -> {drawCircle(color,3.5f*u,Offset(12*u,7*u),style=stroke);drawArc(color,180f,180f,false,Offset(4*u,13*u),Size(16*u,14*u),style=stroke)}
   "servicios" -> {drawRoundRect(color,Offset(3*u,2*u),Size(18*u,20*u),androidx.compose.ui.geometry.CornerRadius(3*u),style=stroke);drawCircle(color,5*u,Offset(12*u,14*u),style=stroke);line(6f,6f,9f,6f);drawCircle(color,.8f*u,Offset(18*u,6*u))}
   "panel" -> {drawRoundRect(color,Offset(3*u,3*u),Size(7*u,7*u),androidx.compose.ui.geometry.CornerRadius(2*u),style=stroke);drawRoundRect(color,Offset(14*u,3*u),Size(7*u,11*u),androidx.compose.ui.geometry.CornerRadius(2*u),style=stroke);drawRoundRect(color,Offset(3*u,14*u),Size(7*u,7*u),androidx.compose.ui.geometry.CornerRadius(2*u),style=stroke);line(14f,19f,21f,19f)}
   "reloj" -> {drawCircle(color,9*u,Offset(12*u,12*u),style=stroke);line(12f,6f,12f,12f);line(12f,12f,16f,14f)}
   "listo" -> {drawCircle(color,9*u,Offset(12*u,12*u),style=stroke);line(7f,12f,10f,15f);line(10f,15f,17f,8f)}
   "ajustes" -> {for(y in listOf(6f,12f,18f))line(3f,y,21f,y);drawCircle(color,2.5f*u,Offset(8*u,6*u),style=stroke);drawCircle(color,2.5f*u,Offset(16*u,12*u),style=stroke);drawCircle(color,2.5f*u,Offset(9*u,18*u),style=stroke)}
   "mas" -> {line(12f,4f,12f,20f);line(4f,12f,20f,12f)}
   "flecha" -> {line(5f,12f,19f,12f);line(13f,6f,19f,12f);line(19f,12f,13f,18f)}
   else -> {drawCircle(color,9*u,Offset(12*u,12*u),style=stroke);line(12f,7f,12f,13f);drawCircle(color,1*u,Offset(12*u,17*u))}
  }
 }
}

@Composable fun CabeceraStudio(etiqueta: String, titulo: String, detalle: String="") {
 Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(6.dp)) {
  Text(etiqueta,style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary)
  Text(titulo,style=MaterialTheme.typography.headlineMedium)
  if(detalle.isNotBlank()) Text(detalle,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
 }
}

@Composable fun OrbitaStudio(modifier: Modifier=Modifier) {
 val giro=remember{Animatable(-30f)}
 LaunchedEffect(Unit){giro.animateTo(0f,tween(900))}
 Canvas(modifier.clearAndSetSemantics{}) {
  val c=center;val r=size.minDimension*.34f
  drawCircle(Brush.radialGradient(listOf(Color(0xFFB7A3FF).copy(.5f),Color.Transparent),c,r*1.6f),r*1.6f,c)
  rotate(giro.value) {
   for(i in 0..4) drawOval(Color(0xFFE3D9FF).copy(alpha=.25f+i*.1f),Offset(c.x-r,c.y-r*.6f+i*r*.06f),Size(r*2,r*1.2f),style=Stroke(1.2.dp.toPx()))
   drawArc(Color(0xFFFFC2B0),-30f,215f,false,Offset(c.x-r*.85f,c.y-r*.85f),Size(r*1.7f,r*1.7f),style=Stroke(r*.15f,cap=StrokeCap.Round))
  }
  drawCircle(Color.White,3.dp.toPx(),Offset(c.x+r*.78f,c.y-r*.5f))
 }
}

@Composable fun HeroStudio(etiqueta: String,titulo: String,detalle: String,modifier: Modifier=Modifier,content: @Composable ColumnScope.() -> Unit={}) {
 Surface(modifier.fillMaxWidth(),shape=RoundedCornerShape(30.dp),color=TintaCampus,contentColor=Color.White) {
  Box(Modifier.background(Brush.linearGradient(listOf(Color(0xFF583F8E),TintaCampus)))) {
   OrbitaStudio(Modifier.align(Alignment.TopEnd).size(140.dp))
   Column(Modifier.fillMaxWidth().padding(24.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
    Text(etiqueta,style=MaterialTheme.typography.labelMedium,color=LimaStudio)
    Text(titulo,style=MaterialTheme.typography.displaySmall,modifier=Modifier.fillMaxWidth(.85f))
    Text(detalle,color=Color(0xFFE1D8F0),style=MaterialTheme.typography.bodyMedium)
    content()
   }
  }
 }
}

@Composable fun AccesoStudio(numero:String,titulo:String,detalle:String,onClick:()->Unit,enabled:Boolean=true) {
 Surface(onClick=onClick,enabled=enabled,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(24.dp),color=MaterialTheme.colorScheme.surface) {
  Row(Modifier.padding(20.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)) {
   Surface(shape=CircleShape,color=MaterialTheme.colorScheme.primaryContainer){Text(numero,Modifier.padding(14.dp),color=MaterialTheme.colorScheme.onPrimaryContainer,style=MaterialTheme.typography.labelLarge)}
   Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(titulo,style=MaterialTheme.typography.titleMedium);Text(detalle,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}
   IconoStudio("flecha",color=MaterialTheme.colorScheme.primary)
  }
 }
}

@Composable fun DatoStudio(etiqueta:String,valor:String,modifier:Modifier=Modifier) {
 Surface(modifier,shape=RoundedCornerShape(24.dp),color=MaterialTheme.colorScheme.surface) {
  Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
   Text(valor,style=MaterialTheme.typography.headlineLarge,color=MaterialTheme.colorScheme.primary)
   Text(etiqueta,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
  }
 }
}

@Composable fun ModuloStudio(titulo:String,detalle:String,icono:String,onClick:()->Unit,modifier:Modifier=Modifier,coral:Boolean=false) {
 val fondo=if(coral)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
 val tinta=if(coral)MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
 Surface(onClick=onClick,modifier=modifier,shape=RoundedCornerShape(26.dp),color=fondo,contentColor=tinta) {
  Column(Modifier.padding(18.dp).heightIn(min=122.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){IconoStudio(icono);IconoStudio("flecha",Modifier.size(18.dp))}
   Text(titulo,style=MaterialTheme.typography.titleLarge)
   Text(detalle,style=MaterialTheme.typography.bodySmall)
  }
 }
}
