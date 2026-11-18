# CampusGPS · Fase 2

IA utilizada: ChatGPT. Primera mejora del GLAB: filtrar por categoría.

| Prompt que escribí (extracto) | Qué respondió ChatGPT | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «dame el script del sieño tomtalmente diferente, algo moderno, animaciones» | Propuso un atlas con cabecera de brújula, índice de lugares, chips animados y una vista horizontal en dos paneles. Implementó el filtro en MapaViewModel. | La implementación usa una sola lista filtrada para el mapa y las tarjetas y conserva las distancias originales. Al cambiar de categoría, quita la selección si el lugar deja de estar visible. | Evita que una tarjeta desaparezca mientras el mapa sigue apuntando a un elemento seleccionado que ya no corresponde. La vista sigue observando al ViewModel. |
