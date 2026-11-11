package com.tecsup.productos.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.productos.MainActivity

class NotificadorAndroid(contexto: Context) : Notificador {
    private val contexto = contexto.applicationContext

    override fun mostrar(titulo: String, texto: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
        ) return

        val gestor = NotificationManagerCompat.from(contexto)
        if (!gestor.areNotificationsEnabled()) return

        crearCanal()
        val abrirApp = Intent(contexto, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val alTocar = PendingIntent.getActivity(
            contexto, 0, abrirApp, PendingIntent.FLAG_IMMUTABLE
        )
        val aviso = NotificationCompat.Builder(contexto, CANAL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(alTocar)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            gestor.notify(NOTIFICACION_ID, aviso)
        } catch (_: SecurityException) {
            // El permiso puede revocarse entre la comprobación y el envío.
        }
    }

    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID, "Sincronización del catálogo", NotificationManager.IMPORTANCE_DEFAULT
            )
            contexto.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(canal)
        }
    }

    private companion object {
        const val CANAL_ID = "sincronizacion_catalogo"
        const val NOTIFICACION_ID = 1
    }
}
