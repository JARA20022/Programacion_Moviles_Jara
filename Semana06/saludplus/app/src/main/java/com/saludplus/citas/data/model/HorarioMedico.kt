package com.saludplus.citas.data.model

import java.time.DayOfWeek
import java.time.LocalTime

data class HorarioMedico(
    val dia: DayOfWeek,
    val horaInicio: LocalTime,
    val horaFin: LocalTime,
    val emergencia: Boolean = false
)
