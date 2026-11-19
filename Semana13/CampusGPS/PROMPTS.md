# CampusGPS · Prompts de la Fase 2

Trabajé las tres mejoras con ChatGPT. Para cada una incluí una versión detallada de la instrucción que se puede pegar en el agente de Android Studio, seguida del extracto de la conversación y de la decisión técnica que tomé.

## 1. Rediseño de CampusGPS y filtro por categoría

### Instrucción para el agente de Android Studio

> Cámbiame el diseño de CampusGPS para que parezca un atlas de bolsillo. Trabaja en `Semana13/CampusGPS`, en la rama `mejora-ia`. Usa Kotlin, Jetpack Compose y Material 3. Pon una cabecera color tinta `#30223B`, un fondo color papel `#F8F4EB`, detalles terracota `#93391E` y un botón **Ubicarme** color melón `#FFB68E`. A la izquierda pon una brújula pequeña que gire al buscar. Al lado coloca **CampusGPS** con letra serif y debajo mis coordenadas con letra monoespaciada. Mientras busca, cambia el texto del botón a **Buscando…**.
>
> En vertical, coloca el mapa arriba y el índice de lugares debajo. En horizontal, pon el mapa a la izquierda y el índice a la derecha para que pueda usar ambos al girar el celular. En cada tarjeta muestra la categoría encima del nombre y la distancia alineada a la derecha. Anima el cambio de color de la tarjeta seleccionada y el movimiento de las filas cuando cambie el orden.
>
> Agrega los chips **Todos**, **Estudio**, **Comida**, **Eventos** y **Servicios**. Guarda la categoría seleccionada en `MapaViewModel` y expón con `StateFlow` una lista filtrada que alimente tanto el mapa como las tarjetas. Conserva el orden por distancia. Si cambio de categoría y el lugar seleccionado desaparece, limpia esa selección. El marcador rojo debe seguir indicando mi posición y el verde debe señalar el lugar más cercano entre los visibles.
>
> Comprueba tres casos: con **Comida** quiero ver solo Cafetería Campus; con **Estudio**, Biblioteca Central y Laboratorio de Cómputo; con **Todos**, los cinco lugares. En **Estudio**, toca una tarjeta y gira el emulador: el filtro debe seguir activo y la cámara debe poder enfocar el lugar seleccionado.

### Registro de la conversación

| Prompt que escribí (extracto) | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «dame el script del sieño tomtalmente diferente, algo moderno, animaciones» | Propuso un atlas con brújula, filtros animados, tarjetas y dos paneles en horizontal. | Usé la misma lista filtrada para el mapa, las tarjetas y el cálculo del más cercano visible. Dejé el filtro en el ViewModel. | Si elijo Comida, el botón debe llevarme a la cafetería aunque la biblioteca esté más cerca en la lista completa. |

## 2. Radio de cercanía

### Instrucción para el agente de Android Studio

> Añádeme un selector **Radio** con tres opciones: **100 m**, **200 m** y **500 m**. Déjalo inicialmente en **200 m**. Sigue los colores del atlas: pinta la opción activa en terracota con texto blanco y usa un fondo claro para las demás. Conserva el filtro de categorías que ya funciona.
>
> Guarda el radio en `MapaViewModel`. Usa la distancia en metros que ya tiene cada `LugarConDistancia` y marca como cercano todo lugar visible cuya distancia sea menor o igual al radio. En `MapaUbicacion`, dibuja un `Circle` centrado en mi posición cuando se conozcan mis coordenadas. Ponle borde terracota y relleno melón translúcido para que todavía pueda leer las calles. Al cambiar de radio, actualiza el círculo sin pedirme otra vez la ubicación.
>
> En las tarjetas, agrega **Dentro del radio** debajo de la distancia cuando corresponda. Quiero seguir viendo los lugares que quedan fuera para poder comparar. Comprueba el resultado en `-12.0464, -77.0428` con **Todos** activo: a 100 m debe marcarse solo la biblioteca; a 200 m también la cafetería y el laboratorio; a 500 m deben marcarse los cinco. Después elige **Comida** y cambia de 200 m a 100 m: la cafetería debe seguir en la lista, pero su etiqueta debe desaparecer.

### Registro de la conversación

| Prompt que escribí | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «ahora que sigue» | Propuso el radio de cercanía del GLAB y preparó el selector, el círculo y la etiqueta en las tarjetas. | Reutilicé las distancias calculadas y conservé los lugares que quedan fuera del círculo. | A 109 m, la cafetería está fuera de 100 m y dentro de 200 m. El cambio de etiqueta permite comprobar el límite sin perder la tarjeta. |

## 3. Mapa nocturno y significado de los marcadores

### Instrucción para el agente de Android Studio

> Haz que el mapa de CampusGPS siga el tema de Android. Cuando active **Tema oscuro**, quiero ver la tierra en ciruela oscuro `#2A2533`, las calles en gris violáceo `#514755`, los parques en verde apagado `#293D38` y el agua en azul profundo `#172C3A`. Usa etiquetas claras `#D9CBD8` con un contorno oscuro para que los nombres de las calles se lean.
>
> Crea `app/src/main/res/raw/mapa_nocturno.json`. En `MapaUbicacion.kt`, detecta el tema con `isSystemInDarkTheme()`, carga el recurso con `MapStyleOptions` y aplícalo mediante `MapProperties`. Cuando vuelva al tema claro, recupera el mapa diurno. Ajusta el borde del círculo a melón en modo oscuro y reduce la opacidad del relleno. Mantén este cambio visual en la vista.
>
> Conserva estas señales: rojo para mi ubicación, verde para el lugar más cercano de la categoría visible y azul para los demás. Al tocar una tarjeta, resáltala y muestra su nombre sobre el marcador. Prueba con **Todos** en el punto base: aunque toque Cafetería Campus, la biblioteca debe seguir en verde porque está más cerca. Luego elige **Eventos**: el auditorio debe pasar a verde. Finalmente alterna entre tema oscuro y claro y verifica que el círculo, las calles y los marcadores se distingan en ambos.

### Registro de la conversación

| Prompt que escribí (extracto) | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «en la lista voy presionando y no cambie el verde no se si eso deberia cambiar en el paso 3» | Explicó que el verde representa cercanía y propuso adaptar el estilo del mapa al tema de Android. | Mantuve el verde para el más cercano y la tarjeta resaltada para la selección. Adapté el mapa y el borde del círculo al tema. | Puedo consultar otro lugar sin perder de vista qué destino tengo más cerca. |
