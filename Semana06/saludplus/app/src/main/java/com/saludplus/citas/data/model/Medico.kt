package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val experiencia: Int,
    val calificacion: Double,
    val precioConsulta: Double
)