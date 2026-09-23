package com.jara.tecsupfit.data

import com.jara.tecsupfit.model.*

// Datos de demostración tomados de las figuras del Word, sin servidor.
object DatosFit {
    val clases = listOf(
        ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"),
        ClaseFit(
            id = 2,
            nombre = "Cross Training",
            hora = "6:00 pm",
            sala = "Sala 1",
            duracion = "45 min",
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposIniciales = 8,
            capacidad = 12
        ),
        ClaseFit(3, "Spinning", "7:30 pm", "Sala 3"),
        // La figura de Reservas también muestra Yoga de Ayer a las 7:00 am.
        // Se incluye como sesión pasada en Esta semana; no se puede reservar.
        ClaseFit(4, "Yoga funcional", "7:00 am", "Sala 2", dia = "Ayer")
    )

    val usuario = UsuarioFit("Diego Ramos", "DR", "Plan Premium", 14, 3)

    // Solo se precarga la sesión completada de la referencia.
    // Las Confirmadas se generan al usar el botón Reservar cupo.
    fun reservasIniciales() = listOf(
        ReservaFit(id = 1, clase = clases.first { it.id == 4 }, estado = "Completada")
    )
}
