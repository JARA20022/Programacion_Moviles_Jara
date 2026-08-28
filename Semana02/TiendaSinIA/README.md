# Lab 02 - Carrito de Compras en Kotlin

**Nombre completo:** Ivan Jara aYALA
**Curso:** Programación en Móviles - 4to Ciclo
**Docente:** Juan León Suiyon

## Descripción

Programa de consola desarrollado en Kotlin que simula el carrito de compras de una tienda. 
Modela los productos con una `data class`, permite agregar productos a una lista mutable, 
y calcula automáticamente el subtotal, el IGV (18%), el total a pagar, el producto más caro 
del carrito y el descuento aplicable según el monto total (5% si supera S/ 3000, 10% si 
supera S/ 5000).

## Funciones implementadas

- `calcularSubtotal(productos)`: suma el precio por cantidad de todos los productos.
- `calcularIGV(subtotal)`: calcula el 18% del subtotal.
- `calcularTotal(subtotal, igv)`: suma subtotal + IGV.
- `mostrarDetalle(productos)`: imprime el detalle del carrito con columnas alineadas y montos con 2 decimales.
- `calcularDescuento(total)`: aplica descuento usando `when` según el monto total.

## Captura de la consola (resultado final)


<img width="540" height="676" alt="image" src="https://github.com/user-attachments/assets/d0cbf17a-278f-4cce-9457-a5aaa343053c" />

## Respuesta: ¿por qué nombre y precio son val pero cantidad es var?

`nombre` y `precio` se declaran como `val` porque una vez creado el producto, su nombre y su 
precio no deberían cambiar durante la vida del objeto; son datos fijos que identifican al 
producto. En cambio, `cantidad` se declara como `var` porque sí puede variar: el cliente 
puede agregar o quitar unidades del mismo producto en el carrito.

Si se intenta cambiar `precio` después de crear el producto (por ejemplo `producto.precio = 100.0`), 
Kotlin marca un error de compilación, ya que las propiedades `val` son inmutables una vez asignadas 
y no pueden reasignarse.
