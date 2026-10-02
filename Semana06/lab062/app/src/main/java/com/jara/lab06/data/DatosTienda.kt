package com.jara.lab06.data

import com.jara.lab06.model.Producto

// Productos guardados en memoria.
val productosTienda = listOf(
    Producto(
        1, "Laptop para estudiar", 2499.00, "Tecnología",
        "Ideal para realizar trabajos, estudiar y practicar programación."
    ),
    Producto(
        2, "Audífonos inalámbricos", 129.00, "Tecnología",
        "Escucha tus clases y música con comodidad."
    ),
    Producto(
        3, "Mouse inalámbrico", 59.00, "Tecnología",
        "Un accesorio práctico para trabajar con tu computadora."
    ),
    Producto(
        4, "Cuaderno universitario", 18.00, "Útiles",
        "Organiza tus apuntes y actividades de clase."
    ),
    Producto(
        5, "Juego de lapiceros", 12.00, "Útiles",
        "Lapiceros de diferentes colores para tus apuntes."
    ),
    Producto(
        6, "Mochila para clases", 89.00, "Accesorios",
        "Lleva tus cuadernos y materiales de forma cómoda."
    ),
    Producto(
        7, "Tomatodo", 35.00, "Accesorios",
        "Lleva agua durante tus clases y actividades."
    )
)

val categoriasTienda = listOf(
    "Todas",
    "Tecnología",
    "Útiles",
    "Accesorios"
)