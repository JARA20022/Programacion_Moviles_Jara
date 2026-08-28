package com.jara.lab02carritokotlin

class Carrito(private val cliente: String) {

    private val productos: MutableList<Producto> = mutableListOf()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun cantidadProductos(): Int = productos.size

    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.calcularImporte()
        }
        return subtotal
    }

    fun calcularIGV(subtotal: Double): Double = subtotal * 0.18

    fun calcularTotal(subtotal: Double, igv: Double): Double = subtotal + igv

    fun calcularImpuestosAdicionales(): Double {
        var total = 0.0
        for (p in productos) {
            total += p.calcularImpuestoAdicional()
        }
        return total
    }

    fun calcularDescuento(total: Double): Double {
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }

    fun mostrarDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in productos) {
            println("$i. $p")
            i++
        }
        println("---------------------------------------")
    }

    fun productoMasCaro(): Producto? = productos.maxByOrNull { it.precio }

    fun buscarProducto(nombre: String): Producto? =
        productos.find { it.nombre.equals(nombre, ignoreCase = true) }

    fun eliminarProducto(nombre: String): Boolean =
        productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }

    fun nombreCliente(): String = cliente
}