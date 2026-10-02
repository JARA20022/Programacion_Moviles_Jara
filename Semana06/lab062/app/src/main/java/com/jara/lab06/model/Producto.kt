package com.jara.lab06.model

// Datos que tendrá cada producto.
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String,
    val descripcion: String
)