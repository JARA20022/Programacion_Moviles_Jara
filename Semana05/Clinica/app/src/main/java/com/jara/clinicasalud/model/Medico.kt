package com.jara.clinicasalud.model

// Modelo de un médico. Contiene datos, no componentes de interfaz.
// categoria permite filtrar; especialidad se muestra en las tarjetas.
data class Medico(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val especialidad: String,
    val calificacion: String,
    val experiencia: String? = null,
    val resenas: Int? = null,
    val descripcion: String? = null
)
