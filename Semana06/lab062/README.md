# Laboratorio 06 — IVAN JARA AYALA Store

**Estudiante:** Iván Jara Ayala  
**Curso:** Programación de Aplicaciones Móviles  
**Paquete:** com.jara.lab06  
**Carpeta del proyecto:** Semana06/lab062

## Descripción

Aplicación de una tienda desarrollada con Kotlin y Jetpack Compose.
Retoma el uso de categorías, listas de productos y navegación trabajado
en los laboratorios anteriores.

En este laboratorio se agregaron opciones para cada producto y un menú
lateral para recorrer las pantallas. En la rama mejora-ia se incorporó
el contador de favoritos y se ajustó la presentación visual.

## Funciones

- Categorías en una lista horizontal.
- Secciones de productos en una lista vertical.
- Pantalla con el detalle de un producto.
- Menú de tres puntos en las tarjetas.
- Opciones para agregar o quitar favoritos, compartir y reportar.
- Iconos y separadores en el menú de productos.
- Menú lateral con Inicio, Mis pedidos, Favoritos y Perfil.
- Encabezado con mis iniciales, nombre y correo.
- Resaltado de la pantalla seleccionada.
- Contador de favoritos en la rama mejora-ia.

## Pantallas

### Inicio
Muestra las categorías y los productos. Permite consultar un producto
y abrir sus opciones.

### Detalle del producto
Muestra la información del producto seleccionado.

### Mis pedidos
Muestra el mensaje de que todavía no hay pedidos. Este laboratorio
no incluye un proceso de compra ni el registro de pedidos.

### Favoritos
Muestra los productos marcados como favoritos y permite quitarlos
desde sus opciones.

### Perfil
Muestra mis datos como estudiante.

## Menú de cada producto

### Favoritos
Agrega o quita el producto de la lista de favoritos. En mejora-ia,
la acción también actualiza el contador del menú lateral.

### Compartir
Abre las opciones de Android para compartir la información del producto.

### Reportar
Permite confirmar un reporte de práctica. No envía información a un
servidor ni a un servicio de atención.

## Organización principal

- MainActivity.kt: entrada de la aplicación.
- model/Producto.kt: información que contiene cada producto.
- data/DatosTienda.kt: productos, categorías y listas de la aplicación.
- navigation/AppNavegacion.kt: conexión entre las pantallas.
- ui/components/TarjetaProducto.kt: tarjeta y opciones del producto.
- ui/components/AppDrawer.kt: contenido del menú lateral.
- screens/: pantallas de la aplicación.
- PROMPTS.md: registro de las solicitudes utilizadas con IA.

## Ramas

### main
Contiene la versión base del laboratorio con el menú de productos
y la navegación lateral.

### mejora-ia
Contiene el contador de favoritos y los ajustes realizados con ayuda
de IA. Las solicitudes se documentan en PROMPTS.md.

## Cómo ejecutar

1. Clonar el repositorio:
   git clone https://github.com/JARA20022/Programacion_Moviles_Jara.git

2. Para revisar las mejoras, seleccionar la rama:
   git switch mejora-ia

3. Abrir la carpeta Semana06/lab062 en Android Studio.

4. Esperar a que termine la sincronización del proyecto.

5. Seleccionar un emulador o un dispositivo Android compatible
   con la configuración del proyecto.

6. Ejecutar la aplicación.

Para comparar con la versión base, cambiar a main y volver a ejecutar.

## Comprobación del contador

1. Abrir las opciones de un producto.
2. Seleccionar Favoritos.
3. Abrir el menú lateral y comprobar que el contador aumentó.
4. Marcar otro producto y comprobar el nuevo total.
5. Quitar un favorito y comprobar que el contador disminuyó.
6. Revisar que la pantalla Favoritos coincida con ese total.

## Otras comprobaciones

- Abrir los cuatro destinos del menú lateral.
- Comprobar el resaltado de la pantalla seleccionada.
- Abrir y cerrar las opciones de distintos productos.
- Comprobar las opciones Compartir y Reportar.
- Revisar que el menú no cubra el siguiente producto después
  de los ajustes de posición.

Esta lista describe las comprobaciones que deben realizarse;
no representa una ejecución automática de pruebas.

## Alcance y limitaciones

Los favoritos y los reportes se mantienen en memoria. No se utiliza
una base de datos, por lo que no se garantiza conservarlos después
de cerrar el proceso de la aplicación.

No se implementan pagos, compras, autenticación ni envío real de reportes.

El trabajo corresponde a la tienda del laboratorio. No incluye
la actividad adicional de otra aplicación.
