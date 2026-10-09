package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val experiencia: Int,
    val calificacion: Double,
    val precioConsulta: Double,
    val codigoProfesional: String = "CMP-1001",
    val sedeId: Int = 1,
    val telefono: String = "999999999",
    val horarios: List<HorarioMedico> = emptyList()
)
