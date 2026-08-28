package com.jara.lab02carritokotlin

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP   ")
    println("=========================================")
    println()

    val carrito = Carrito("Juan Leon")
    println("Cliente: ${carrito.nombreCliente()}")
    println()

    carrito.agregarProducto(ProductoElectronico("Laptop HP", 2500.0, 1, 12))
    carrito.agregarProducto(ProductoElectronico("Mouse Logitech", 45.5, 2, 6))
    carrito.agregarProducto(ProductoAccesorio("Audifonos Sony", 120.0, 1))
    carrito.agregarProducto(ProductoElectronico("USB Kingston 64GB", 25.0, 3, 24))

    println()
    carrito.mostrarDetalle()
    println()
    println("Cantidad de productos : ${carrito.cantidadProductos()}")

    val subtotal = carrito.calcularSubtotal()
    val igv = carrito.calcularIGV(subtotal)
    val total = carrito.calcularTotal(subtotal, igv)

    println(String.format("Subtotal              : S/ %.2f", subtotal))
    println(String.format("IGV (18%%)             : S/ %.2f", igv))
    println(String.format("TOTAL A PAGAR          : S/ %.2f", total))
    println("-----------------------------------------")

    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = carrito.calcularDescuento(total)
    if (descuento > 0) {
        val porcentaje = if (total > 5000) 10 else 5
        println("Descuento aplicado: $porcentaje% por compra mayor a S/ ${if (total > 5000) 5000 else 3000}")
        println(String.format("TOTAL CON DESCUENTO  : S/ %.2f", total - descuento))
    } else {
        println("No se aplico descuento (total no supera S/ 3000)")
    }

    println()
    println("Gracias por su compra, ${carrito.nombreCliente()}!")

    println()
    val encontrado = carrito.buscarProducto("Mouse Logitech")
    println("Busqueda -> " + (encontrado?.toString() ?: "No encontrado"))

    carrito.eliminarProducto("USB Kingston 64GB")
    println("\nDespues de eliminar 'USB Kingston 64GB':")
    carrito.mostrarDetalle()
}