# Lab03 - Registro de Producto (Jetpack Compose)

**Alumno:** IVAN JARA AYALA
**Curso:** Programación en Móviles - 4to Ciclo
**Docente:** Juan León

## Descripción
Aplicación desarrollada en Jetpack Compose que permite registrar un producto
ingresando su nombre, precio y cantidad. Al presionar "AGREGAR PRODUCTO" se
muestra una Card con el resumen del producto y el importe total calculado
(precio × cantidad, con 2 decimales).

## Capturas

### Pantalla inicial (formulario vacío)
![Pantalla inicial](capturas/pantalla_inicial.png)

### Después de presionar AGREGAR PRODUCTO
![Pantalla con resultado](capturas/pantalla_resultado.png)

## Reflexión

**¿Qué pasaría si declaras las variables de los campos SIN `remember`?**

Al quitar `remember` y dejar solo `mutableStateOf("")`, el campo de texto
deja de conservar su valor correctamente entre recomposiciones. Al escribir
se ve el texto momentáneamente, pero en cuanto Compose vuelve a ejecutar la
función `PantallaRegistro` (por ejemplo al rotar la pantalla o al cambiar
el tamaño de la ventana), el estado se reinicia a su valor inicial (`""`)
porque se crea una nueva variable de estado en cada recomposición.
`remember` es lo que le dice a Compose que debe conservar ese valor mientras
el composable permanezca en pantalla, en lugar de recrearlo desde cero.