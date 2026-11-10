package com.tecsup.productos.domain.model

data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val descripcion: String? = null,
    val precio: Double? = null,
)
