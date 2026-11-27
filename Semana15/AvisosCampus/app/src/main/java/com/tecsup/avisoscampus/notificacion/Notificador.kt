package com.tecsup.avisoscampus.notificacion

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.*
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso
object Notificador {
 const val CANAL = "avisos"
 fun crearCanal(context: Context) {
  if (Build.VERSION.SDK_INT >= 26) context.getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel(CANAL,"Avisos",NotificationManager.IMPORTANCE_DEFAULT))
 }
 fun tienePermiso(context: Context): Boolean = Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
 @Suppress("MissingPermission")
 fun mostrar(context: Context, aviso: Aviso): Boolean {
  if (!tienePermiso(context)) return false
  val builder = NotificationCompat.Builder(context,CANAL).setSmallIcon(R.drawable.ic_notificacion).setContentTitle(aviso.titulo).setContentText(aviso.texto).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
  val pendiente=PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
  builder.setContentIntent(pendiente)
  return try { NotificationManagerCompat.from(context).notify(aviso.id,builder.build()); true } catch (_: SecurityException) { false }
 }
}
