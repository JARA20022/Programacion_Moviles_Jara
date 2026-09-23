package com.jara.clinicasalud.model

// Reúne los datos elegidos al agendar. Una cita nueva empieza Confirmada.
data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)
