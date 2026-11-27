package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.*
import java.text.SimpleDateFormat
import java.util.*

@Composable fun HistorialAvisos(avisos: List<Aviso>) {
 if(avisos.isEmpty()) {
  Panel { Text("Todo tranquilo por aquí",style=MaterialTheme.typography.titleMedium); Text("Los avisos que generes aparecerán en esta sección.",color=CampusMuted) }
 } else {
  avisos.take(20).forEach { aviso ->
   val color=when(aviso.origen) { Origen.BOTON -> CampusPurple; Origen.RECORDATORIO -> CampusAmber; Origen.MODO_AVION -> CampusMint }
   Panel {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
     Text(aviso.origen.texto,Modifier.background(color.copy(alpha=0.12f),RoundedCornerShape(8.dp)).padding(horizontal=10.dp,vertical=4.dp),color=color,style=MaterialTheme.typography.labelMedium)
     Text(SimpleDateFormat("dd/MM · HH:mm",Locale.getDefault()).format(Date(aviso.hora)),color=CampusMuted,style=MaterialTheme.typography.labelSmall)
    }
    Text(aviso.titulo,style=MaterialTheme.typography.titleMedium)
    Text(aviso.texto,color=CampusMuted,style=MaterialTheme.typography.bodyMedium)
   }
  }
 }
}
