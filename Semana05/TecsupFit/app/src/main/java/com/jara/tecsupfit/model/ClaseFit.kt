package com.jara.tecsupfit.model

data class ClaseFit(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val dia: String = "Hoy",
    val duracion: String? = null,
    val descripcion: String? = null,
    val cuposIniciales: Int? = null,
    val capacidad: Int? = null
)
