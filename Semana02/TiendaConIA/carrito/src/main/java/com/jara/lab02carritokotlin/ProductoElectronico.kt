package com.jara.lab02carritokotlin

class ProductoElectronico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val garantiaMeses: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImpuestoAdicional(): Double = precio * 0.02
}