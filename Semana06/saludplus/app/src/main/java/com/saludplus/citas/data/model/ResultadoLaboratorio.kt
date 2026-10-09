package com.saludplus.citas.data.model

data class ResultadoLaboratorio(
    val id: Int,
    val usuarioId: Int,
    val examen: String,
    val fecha: String,
    val estado: String,
    val resultado: String,
    val unidad: String,
    val referencia: String
)
