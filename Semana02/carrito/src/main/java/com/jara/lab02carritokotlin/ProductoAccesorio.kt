package com.jara.lab02carritokotlin

class ProductoAccesorio(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImpuestoAdicional(): Double = 0.0
}