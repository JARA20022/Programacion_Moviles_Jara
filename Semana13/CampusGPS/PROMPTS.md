# CampusGPS · Fase 2

IA utilizada: ChatGPT. Primera mejora del GLAB: filtrar por categoría.

| Prompt que escribí (extracto) | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «dame el script del sieño tomtalmente diferente, algo moderno, animaciones» | Propuso un atlas con cabecera de brújula, índice de lugares, chips animados y una vista horizontal en dos paneles. Implementó el filtro en MapaViewModel. | La implementación usa una sola lista filtrada para el mapa y las tarjetas y conserva las distancias originales. Al cambiar de categoría, quita la selección si el lugar deja de estar visible. | Evita que una tarjeta desaparezca mientras el mapa sigue apuntando a un elemento seleccionado que ya no corresponde. La vista sigue observando al ViewModel. |

## Mejora 2 · Radio de cercanía

| Prompt que escribí | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «ahora que sigue» (continuación de las mejoras del GLAB) | Propuso los radios de 100, 200 y 500 m y preparó el cambio para el mapa y las tarjetas. | La implementación guarda el radio en MapaViewModel y reutiliza las distancias ya calculadas. Conserva en la lista los lugares que quedan fuera y etiqueta los que están dentro. | Puedo comparar distancias y combinar el radio con el filtro por categoría sin repetir el cálculo en Compose. |


## Mejora 3 · Mapa en modo oscuro

| Prompt que escribí (extracto) | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «en la lista voy presionando y no cambie el verde no se si eso deberia cambiar en el paso 3» | Aclaró que el verde indica el lugar más cercano de la categoría visible. Para la tercera mejora propuso aplicar un estilo nocturno al mapa según el tema de Android. | Conservé el verde para la cercanía y la tarjeta resaltada para la selección. En el tema oscuro cambié los colores del mapa y el contraste del círculo. | Así puedo explorar otra tarjeta sin perder la referencia de cuál es el destino más cercano. El cambio de tema pertenece a la vista. |
