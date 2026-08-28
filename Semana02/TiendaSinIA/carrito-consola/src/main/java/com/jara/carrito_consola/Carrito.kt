package com.jara.carrito_consola

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f",
            i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Juan Leon"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("Teclado Mecanico", 180.0, 1))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    println()
    mostrarDetalle(carrito)
    println("Cantidad de productos: ${carrito.size}")
    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("Subtotal : S/ %8.2f", subtotal))
    println(String.format("IGV (18%%): S/ %8.2f", igv))
    println(String.format("TOTAL    : S/ %8.2f", total))

    println()
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento
    println(String.format("Descuento aplicado: S/ %.2f", descuento))
    println(String.format("TOTAL CON DESCUENTO: S/ %.2f", totalConDescuento))

    println()
    println("--- BUSQUEDA DE PRODUCTO ---")
    val encontrado = buscarProducto(carrito, "Mouse Logitech")
    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.nombre} - S/ ${encontrado.precio}")
    } else {
        println("Producto no encontrado")
    }

    println()
    println("--- ELIMINANDO PRODUCTO ---")
    carrito.removeIf { it.nombre == "Teclado Mecanico" }
    println("Se elimino 'Teclado Mecanico' del carrito")
    println()
    mostrarDetalle(carrito)
    val nuevoSubtotal = calcularSubtotal(carrito)
    val nuevoIgv = calcularIGV(nuevoSubtotal)
    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIgv)
    println(String.format("Nuevo Subtotal: S/ %8.2f", nuevoSubtotal))
    println(String.format("Nuevo TOTAL    : S/ %8.2f", nuevoTotal))
}