# Registro de uso de IA — Laboratorio 06

**Estudiante:** Iván Jara Ayala  
**Proyecto:** IVAN JARA AYALA Store  
**Rama de las mejoras:** mejora-ia

## Herramientas utilizadas

Utilicé ChatGPT para preparar las instrucciones y revisar las diferencias
con la imagen del laboratorio. Utilicé Gemini en Android Studio para
modificar el proyecto.

Los siguientes apartados resumen las solicitudes realizadas. Algunas
necesitaron correcciones adicionales para acercar el resultado al diseño.

## 1. Contador de favoritos

### Solicitud
Agregar un contador junto a Favoritos en el menú lateral. El número debía
actualizarse al marcar o quitar productos desde el menú de tres puntos.

Utilizar la misma lista de favoritos que ya tenía la aplicación, evitando
crear un contador independiente. Mostrar también el número cero y conservar
la navegación existente.

### Propósito
Conectar una acción del menú de cada producto con el menú principal,
cumpliendo la mejora obligatoria del laboratorio.

## 2. Colores y presentación general

### Solicitud
Adaptar la aplicación a la imagen del laboratorio utilizando morado,
tarjetas lavanda, fondos blancos y bordes redondeados.

Mostrar IVAN JARA AYALA en lugar de TECSUP Store. Ajustar el encabezado,
las tarjetas, los iconos, el menú de productos y el menú lateral.
Conservar los productos, las categorías, la navegación y el contador.

### Revisión
El primer resultado cambió los colores, pero todavía tenía diferencias
en el tamaño del título, los iconos y la distribución de las tarjetas.

## 3. Corrección del encabezado, las tarjetas y los iconos

### Solicitud
Aumentar el título a 24sp y agregar debajo el texto “Más vendidos”.
Reemplazar la bolsa de contorno por una bolsa rellena de color morado.

Quitar el botón visible “Ver producto” y conservar el acceso al detalle
al tocar la tarjeta. Mantener el botón de tres puntos como una acción
independiente.

Ajustar los iconos del menú lateral y conservar el contador de favoritos.

### Propósito
Acercar la composición de la pantalla a la referencia del laboratorio.

## 4. Espacio para el menú del producto

### Solicitud
Al abrir el menú de tres puntos, dejar espacio debajo de la tarjeta
seleccionada para mostrar Favoritos, Compartir y Reportar sin cubrir
el siguiente artículo.

Conservar un DropdownMenu real y sus DropdownMenuItem. Medir su altura
para reservar el espacio necesario y eliminar ese espacio al cerrar
el menú.

### Revisión
En el resultado observado se abrió el espacio, pero el menú quedó
demasiado abajo y todavía cubría una parte del siguiente producto.

## 5. Corrección de la posición del menú

### Solicitud
Colocar el borde superior del menú a 8dp del borde inferior de la tarjeta.
Alinear ambos bordes derechos y dejar 16dp entre el menú y el siguiente
producto.

Revisar el anclaje, las medidas y los desplazamientos para evitar sumar
dos veces las distancias. No utilizar un desplazamiento arbitrario que
solamente funcione con el primer producto.

Conservar las acciones del menú, el contador y el resto del diseño.

## Revisión del trabajo con IA

No acepté automáticamente el primer resultado. Ejecuté la aplicación
y comparé su apariencia con la referencia.

Solicité correcciones porque el título era pequeño, la bolsa tenía una
forma diferente y el menú del producto se superponía con otro artículo.

El contador fue comprobado durante el desarrollo. Los ajustes visuales
también necesitaron revisión en el emulador.