package com.tecsup.avisoscampus.notificacion

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.*
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.*

object Notificador {
 const val CANAL="avisos"
 const val URGENTES="urgentes"
 fun crearCanal(context: Context) {
  if(Build.VERSION.SDK_INT>=26) {
   val manager=context.getSystemService(NotificationManager::class.java)
   manager.createNotificationChannel(NotificationChannel(CANAL,"Avisos",NotificationManager.IMPORTANCE_DEFAULT))
   manager.createNotificationChannel(NotificationChannel(URGENTES,"Urgentes",NotificationManager.IMPORTANCE_HIGH).apply { description="Recordatorios de entregas" })
  }
 }
 fun tienePermiso(context: Context): Boolean =
  (Build.VERSION.SDK_INT<33 || ContextCompat.checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED) && NotificationManagerCompat.from(context).areNotificationsEnabled()
 @Suppress("MissingPermission")
 fun mostrar(context: Context,aviso: Aviso): Boolean {
  if(!tienePermiso(context)) return false
  val urgente=aviso.origen==Origen.RECORDATORIO
  val canal=if(urgente) URGENTES else CANAL
  if(Build.VERSION.SDK_INT>=26 && context.getSystemService(NotificationManager::class.java).getNotificationChannel(canal)?.importance==NotificationManager.IMPORTANCE_NONE) return false
  val pendiente=PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
  val notification=NotificationCompat.Builder(context,canal)
   .setSmallIcon(R.drawable.ic_notificacion).setContentTitle(aviso.titulo).setContentText(aviso.texto)
   .setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))
   .setColor(0xFF7568F5.toInt()).setContentIntent(pendiente).setAutoCancel(true)
   .setPriority(if(urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
   .build()
  return try { NotificationManagerCompat.from(context).notify(aviso.id,notification); true } catch(_: SecurityException) { false }
 }
}
