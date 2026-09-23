package com.jara.clinicasalud.model

// La agenda muestra el día abreviado y el número.
// La confirmación utiliza el nombre completo del día.
data class FechaCita(
    val dia: String,
    val numero: String,
    val diaCompleto: String
)
