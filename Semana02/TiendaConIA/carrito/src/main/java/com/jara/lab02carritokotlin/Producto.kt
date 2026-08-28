package com.jara.lab02carritokotlin

abstract class Producto(
    val nombre: String,
    precioInicial: Double,
    var cantidad: Int
) {
    private var precioReal: Double = precioInicial

    val precio: Double
        get() = precioReal

    abstract fun calcularImpuestoAdicional(): Double

    fun calcularImporte(): Double = precio * cantidad

    override fun toString(): String {
        return String.format("%-20s x%d  S/ %8.2f", nombre, cantidad, calcularImporte())
    }
}