package com.saludplus.citas.navigation

import android.net.Uri

object Rutas {
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"

    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmar/{medicoId}/{fecha}/{hora}"

    const val MIS_CITAS = "mis_citas"
    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val NOTIFICACIONES = "notificaciones"

    fun medicos(especialidadId: Int): String =
        "medicos/$especialidadId"

    fun fechaHora(medicoId: Int): String =
        "fecha_hora/$medicoId"

    fun confirmarCita(
        medicoId: Int,
        fecha: String,
        hora: String
    ): String = "confirmar/$medicoId/$fecha/${Uri.encode(hora)}"
}