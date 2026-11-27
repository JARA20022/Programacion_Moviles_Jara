package com.tecsup.avisoscampus.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import com.tecsup.avisoscampus.model.Aviso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía")
        }
        avisos.take(20).forEach { aviso ->
            val hora = SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault()
            ).format(Date(aviso.hora))

            Text(aviso.origen.texto + " · " + aviso.titulo)
            Text(aviso.texto)
            Text(hora)
        }
    }
}
