# Registro de consultas a IA

- **Proyecto:** Productos · Laboratorio 11–12
- **Autor:** Iván Jara Ayala
- **Rama:** `mejora-ia`

Los prompts están reconstruidos a partir de mis indicaciones durante la segunda fase.

## 1. Rediseño general de la aplicación

**Fecha y herramienta:** 11 de noviembre de 2026 · ChatGPT (Codex).

**Prompt**

> Actúa como diseñador UX/UI y desarrollador Android senior con ocho años de experiencia en Kotlin y Jetpack Compose. Rediseña mi app Productos para que se vea como un catálogo profesional de smartphones. Cambia el inicio de sesión, el catálogo, el detalle, favoritos y el panel Admin. Usa las fotos de la API y organiza bien el nombre, la marca y la acción de cada tarjeta. Conserva Firebase, los roles, Room y la navegación.

**Respuesta de la IA.** Propuso un tema oscuro, una portada con degradado, tarjetas con fotos y una ficha de detalle con una imagen más grande.

**Qué apliqué o ajusté.** Apliqué el diseño a las cinco pantallas, reutilicé las tarjetas en catálogo y favoritos y agregué una ilustración para cuando la foto no está disponible.

**Qué aprendí.** Puedo cambiar la presentación en Compose y seguir usando los mismos ViewModel, repositorios y servicios.

## 2. Colores, botones y animaciones

**Fecha y herramienta:** 11 de noviembre de 2026 · ChatGPT (Codex).

**Prompt**

> Mejora la interfaz que me propusiste. Usa fondo oscuro, azul en la portada y verde para resaltar las acciones principales. Agranda Ingresar y dales más espacio a Sincronizar y Limpiar caché para que sean fáciles de tocar. Anima el corazón al guardar un favorito, agrega una transición suave al abrir el detalle y permite buscar por marca o modelo.

**Respuesta de la IA.** Propuso centralizar los colores en el tema, usar botones de ancho completo y aplicar animaciones cortas al corazón, la navegación y los totales del panel.

**Qué apliqué o ajusté.** Dejé Ingresar con 50 dp de altura mínima y las acciones del panel Admin con 48 dp. Usé `AnimatedContent` para el corazón y los totales. La búsqueda filtra por modelo o marca y muestra un mensaje si no hay coincidencias.

**Qué aprendí.** El tamaño y la ubicación de los botones ayudan a identificar las acciones. Una animación corta permite ver que cambió el estado.

## 3. Mensajes de error y modo sin conexión

**Fecha y herramienta:** 11 de noviembre de 2026 · ChatGPT (Codex).

**Prompt**

> Mejora los errores. Si pierdo internet y ya tengo productos en Room, quiero seguir viendo el catálogo con un aviso. Si la caché está vacía o falla el detalle, muestra un mensaje claro y un botón Reintentar. Mantén la decisión sobre qué estado mostrar en el ViewModel.

**Respuesta de la IA.** Propuso separar los errores con y sin caché y reutilizar un componente de error con la opción Reintentar.

**Qué apliqué o ajusté.** El catálogo muestra los productos locales si hay caché. Cuando la caché está vacía o falla el detalle, la interfaz muestra un mensaje y permite reintentar.

**Qué aprendí.** El ViewModel decide el estado según los datos disponibles y la pantalla lo presenta.

## 4. Pruebas unitarias del ViewModel

**Fecha y herramienta:** 11 de noviembre de 2026 · ChatGPT (Codex).

**Prompt**

> Ayúdame a crear tres pruebas del ViewModel del catálogo con un repositorio falso. Comprueba la carga exitosa, el uso de la caché cuando falla la conexión y el error sin caché seguido de una recuperación al reintentar. Quiero ejecutarlas con Gradle sin depender de la API real.

**Respuesta de la IA.** Propuso un `ItemRepository` con datos en memoria, un dispatcher de prueba para las corrutinas y un notificador falso.

**Qué apliqué o ajusté.** Agregué los tres casos a `ListaViewModelTest`. En el tercero cambié el resultado del repositorio falso antes de reintentar para simular que volvió la conexión.

**Qué aprendí.** Puedo reproducir fallas y recuperaciones en las pruebas sin depender de la red, Firebase o el emulador.
