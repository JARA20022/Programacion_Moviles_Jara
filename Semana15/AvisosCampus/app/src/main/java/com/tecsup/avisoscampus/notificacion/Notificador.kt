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
}
