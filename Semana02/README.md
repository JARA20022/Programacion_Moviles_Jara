# Lab 02 — Carrito de compras en Kotlin (versión orientada a objetos)

**Curso:** Programación en Móviles — 4to ciclo, Tecsup
**Docente:** Juan León Suiyon
**Alumno:** Iván Jara Ayala
**Repositorio:** [Programacion_Moviles_Jara/Semana02](https://github.com/JARA20022/Programacion_Moviles_Jara/tree/main/Semana02)

## Descripción

Programa de consola en Kotlin que simula el carrito de compras de una tienda: agrega productos, calcula subtotal, IGV (18%), total, descuento por monto de compra, identifica el producto más caro, y permite buscar y eliminar productos.

A diferencia de la versión base del laboratorio (que usaba una única `data class Producto`), esta versión está **rediseñada con Programación Orientada a Objetos**, aplicando los 4 pilares:

| Pilar | Dónde se aplica |
|---|---|
| **Abstracción** | `Producto` es una `abstract class`: define el contrato (`nombre`, `precio`, `cantidad`, `calcularImpuestoAdicional()`) sin implementar el cálculo de impuesto adicional — cada subclase decide cómo. |
| **Herencia** | `ProductoElectronico` y `ProductoAccesorio` heredan de `Producto`: reutilizan `nombre`, `precio`, `cantidad`, `calcularImporte()` y `toString()` sin reescribirlos. |
| **Polimorfismo** | `Carrito` recorre una `List<Producto>` sin saber si cada elemento es electrónico o accesorio; cada objeto responde a `calcularImpuestoAdicional()` y `toString()` a su propia manera. |
| **Encapsulamiento** | `precioReal` en `Producto` es `private` (solo se expone una versión de solo lectura vía `val precio: Double get() = ...`); la lista `productos` en `Carrito` también es `private`, solo accesible mediante métodos controlados (`agregarProducto`, `buscarProducto`, `eliminarProducto`, etc.). |

## Estructura del proyecto

Todo el código vive en el módulo **`carrito`** (módulo Kotlin/JVM independiente del módulo `app` de Android, necesario para poder ejecutar `main()` por consola):

```
carrito/src/main/java/com/jara/lab02carritokotlin/
 ├── Producto.kt              (clase abstracta base)
 ├── ProductoElectronico.kt   (subclase — hereda de Producto)
 ├── ProductoAccesorio.kt     (subclase — hereda de Producto)
 ├── Carrito.kt               (encapsula la lista y la lógica de negocio)
 └── Main.kt                  (punto de entrada, arma el ejemplo y lo ejecuta)
```

## Funciones implementadas

- `Carrito.agregarProducto()` — agrega un producto y lo confirma por consola.
- `Carrito.calcularSubtotal()` — suma el importe (`precio × cantidad`) de todos los productos.
- `Carrito.calcularIGV()` — calcula el 18% del subtotal.
- `Carrito.calcularTotal()` — suma subtotal + IGV.
- `Carrito.calcularDescuento()` — usa `when` para aplicar 10% (>S/5000), 5% (>S/3000) o 0%.
- `Carrito.mostrarDetalle()` — imprime el detalle con columnas alineadas (`String.format`).
- `Carrito.productoMasCaro()` — usa `maxByOrNull` sobre la lista.
- `Carrito.buscarProducto()` — reto adicional, usa `find`.
- `Carrito.eliminarProducto()` — reto adicional, usa `removeIf`.
- `Producto.calcularImpuestoAdicional()` — método abstracto, cada subclase lo implementa distinto (demostración de polimorfismo).

## val vs var — respuesta

En `Producto`, `nombre` es `val` porque el nombre de un producto no debería cambiar una vez creado — no tiene sentido que "Laptop HP" se convierta en otra cosa a media ejecución. `cantidad` es `var` porque sí es razonable que cambie (agregar más unidades al carrito, por ejemplo).

El `precio`, en esta versión, se manejó distinto: la variable real (`precioReal`) es `private var`, pero fuera de la clase solo se expone como `val precio` de solo lectura (con `get()`). Si alguien fuera de la clase intenta hacer `producto.precioReal = -100`, el código **no compila**, porque `precioReal` es privada — eso es encapsulamiento aplicado directamente a la pregunta de val/var: no basta con elegir `val`, a veces hay que además **ocultar** la variable para que nadie la manipule desde afuera.

## Captura de la consola final
<img width="551" height="590" alt="image" src="https://github.com/user-attachments/assets/9229e8e0-f39e-4e33-82d6-23969395b128" />
