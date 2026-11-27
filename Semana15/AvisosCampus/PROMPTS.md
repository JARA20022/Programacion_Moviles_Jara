# PROMPTS — AvisosCampus

**Estudiante:** IVAN RODRIGO JARA AYALA  
**Curso:** Programación en Móviles — TECSUP  
**Semana:** 15  
**Rama:** `mejora-ia`

## Prompt preparado para Gemini en Android Studio

Este es el texto listo para enviar a Gemini con el proyecto abierto. La respuesta y las decisiones deben registrarse después de utilizarlo. La implementación inicial de las mejoras se preparó con asistencia de ChatGPT; este documento no atribuye esa implementación a una conversación previa con Gemini.

### Prompt completo para copiar

```text
Estoy desarrollando AvisosCampus para el laboratorio de la Semana 15 de Programación en Móviles de TECSUP. Trabajo con Kotlin, Jetpack Compose y Material 3, usando MVVM + Repository. Estoy en la rama mejora-ia.

Quiero que revises el proyecto abierto y rediseñes toda la interfaz para conseguir una aplicación moderna, profesional, con colores vivos, animaciones y una presentación claramente diferente de la versión base. Mantén las funciones del laboratorio. Si alguna de las mejoras ya está implementada, revísala y perfecciónala sin duplicar clases, dependencias, canales ni archivos.

ANTES DE EDITAR
1. Examina MainActivity.kt, AvisosApp.kt, model/Modelos.kt, data/AvisosRepository.kt, viewmodel/AvisosViewModel.kt, notificacion/Notificador.kt, receiver/ModoAvionReceiver.kt, ui/PantallaAvisos.kt, ui/HistorialAvisos.kt y el tema actual.
2. Revisa app/build.gradle.kts y el catálogo de versiones. Conserva los plugins y las versiones compatibles que ya tiene el proyecto.
3. Usa el package com.tecsup.avisoscampus y trabaja únicamente dentro de Semana15/AvisosCampus. Conserva main y las demás semanas del repositorio.

OBJETIVO VISUAL
Quiero una identidad visual oscura, colorida y cuidada. Debe sentirse como un panel personal de actividad del campus. Evita que el resultado sea la misma pantalla original con otros colores: cambia la jerarquía, el encabezado, la distribución, la presentación del temporizador y el historial.

PALETA EXACTA
- Fondo principal: azul noche #0C1020.
- Superficie de paneles: #181E33.
- Texto principal: #F3F4FF.
- Texto secundario: #A9B3CF.
- Color primario violeta: #B5A5FF.
- Acento menta/turquesa: #69E2C4.
- Acento ámbar: #FFCA80.
- Error: rosa rojizo #FFA5AC.
- Degradado del encabezado: #45307C, #24345D y #153C43.
No uses un fondo blanco general. El texto y los controles deben conservar contraste y legibilidad.

ENCABEZADO
- Crea un encabezado con degradado y esquinas de 28 dp.
- Muestra CAMPUS / PERSONAL como etiqueta pequeña.
- Mantén AvisosCampus como nombre visible de la aplicación.
- Añade el texto “Tu día, bajo control.” y una descripción breve.
- Muestra la cantidad real de avisos guardados y el estado del permiso.
- Usa un indicador de estado; acompaña siempre el color con texto.
- Adapta las etiquetas al ancho para evitar recortes en teléfonos pequeños.

PANELES Y TIPOGRAFÍA
- Paneles con esquinas de 24 dp, borde sutil y espaciado interno aproximado de 20 dp.
- Márgenes de 16 dp en teléfonos y mayores cuando haya espacio.
- Títulos claros, jerarquía visual consistente y botones fáciles de pulsar.
- Usa MaterialTheme y un componente de panel reutilizable.
- Emplea violeta para permisos, menta para avisos y receptor, y ámbar para recordatorios.
- Mantén todos los textos de uso en español.

FUNCIONES QUE DEBEN SEGUIR DISPONIBLES
1. Permiso de notificaciones:
   - Estados Sin pedir, Permitido y Denegado.
   - Solicitud de POST_NOTIFICATIONS en Android 13 o superior.
   - Botón Permitir notificaciones y acceso Abrir ajustes.
   - Actualizar el estado al volver de Ajustes.
2. Avisos:
   - Botón Mostrar aviso.
   - Notificación con título Tarea lista y mensaje de entrega registrada.
   - Registrar primero el aviso en el Repository.
   - Cada aviso debe tener su identificador.
   - Tocar la notificación abre la app y la retira del panel.
3. Recordatorio:
   - Botón Recordar en 10 s.
   - Cuenta regresiva visible y botón Cancelar mientras esté activo.
   - Impedir recordatorios simultáneos.
   - Mantener la cuenta al rotar y al minimizar mientras el proceso siga vivo.
   - Limpiar el estado en finally; cancelar no debe generar un aviso.
4. Receptor de anuncios:
   - Switch Escuchar el modo avión.
   - Registrar Activado y Desactivado solo cuando la escucha esté habilitada.
   - Mantener onReceive corto.
5. Historial:
   - Mostrar lo más reciente arriba, origen, título, texto y fecha/hora.
   - Mostrar los 20 registros más recientes del filtro seleccionado.
   - Estado vacío claro.
6. Permiso denegado:
   - La app debe seguir funcionando sin cerrarse.
   - El aviso queda en el historial aunque no pueda mostrarse.
   - Mostrar un mensaje de error y permitir abrir Ajustes.

TEMPORIZADOR Y ANIMACIONES
- Reemplaza la presentación simple por un indicador circular con los segundos en el centro.
- Usa ámbar para el progreso y anima suavemente sus cambios.
- Añade animateContentSize para cambios de tamaño de los paneles.
- Usa AnimatedVisibility para el botón Cancelar y los mensajes.
- El encabezado puede tener una variación suave de iluminación o transparencia.
- Las animaciones no deben crear temporizadores adicionales ni reiniciar la corrutina.
- Evita parpadeos, movimiento excesivo y efectos que dificulten leer o pulsar los controles.

DISEÑO RESPONSIVE Y ROTACIÓN
- Debe funcionar en vertical y horizontal sin bloquear la orientación en el Manifest.
- Usa el ancho disponible, por ejemplo BoxWithConstraints.
- En pantallas estrechas presenta una sola columna con desplazamiento.
- A partir de aproximadamente 700 dp disponibles presenta dos columnas: controles a la izquierda e historial a la derecha.
- En horizontal estrecho conserva una columna si no hay espacio suficiente.
- Respeta safeDrawingPadding o los insets correspondientes.
- Evita tamaños rígidos que corten botones o textos.
- No anides listas con desplazamiento vertical sin límites dentro de otra lista vertical.
- Conserva filtros y diálogos con rememberSaveable; la cuenta regresiva y la escucha pertenecen al ViewModel.

TRES MEJORAS ESPECÍFICAS DEL GLAB
A. Historial persistente con DataStore:
   - Guarda los avisos con DataStore Preferences.
   - El historial debe sobrevivir al cierre del proceso y a la reapertura.
   - Crea una sola instancia de DataStore por archivo desde la composición de dependencias.
   - El Repository recibe la dependencia de almacenamiento y no recibe Context.
   - Persiste también el siguiente identificador y actualiza los datos de forma atómica.
   - Puedes conservar hasta 200 registros y mostrar 20 en pantalla.
   - Añade filtros Todos, Aviso, Recordatorio y Modo avión.
   - Añade Borrar historial con confirmación; la acción pasa por ViewModel y Repository.
B. Dos canales de notificación:
   - Conserva el canal Avisos, id avisos, con IMPORTANCE_DEFAULT.
   - Añade Urgentes, id urgentes, con IMPORTANCE_HIGH.
   - Los recordatorios utilizan Urgentes; los demás avisos utilizan Avisos.
   - No atribuyas a la app control sobre No molestar ni prometas una ventana emergente si Android o el usuario la deshabilitan.
C. Texto largo:
   - Usa NotificationCompat.BigTextStyle para leer el contenido completo al expandir.
   - Mantén título, icono pequeño, contenido, identificador, PendingIntent y autoCancel.
   - Usa un mensaje suficientemente largo para poder comprobar la expansión.

REGLAS DE ARQUITECTURA
- Mantén MVVM + Repository mediante interface.
- El ViewModel conoce la interface del Repository; no recibe Context, no usa NotificationManager y no llama directamente al Notificador.
- El Repository no contiene lógica de UI ni usa Context.
- El ViewModel emite eventos y la pantalla llama al Notificador.
- El receptor comunica el cambio; no realiza trabajo largo ni muestra notificaciones directamente.
- Registra el receptor con RECEIVER_NOT_EXPORTED y quítalo en onDispose.
- Usa PendingIntent.FLAG_IMMUTABLE.
- Conserva la cancelación de corrutinas: no consumas CancellationException como un error normal.
- Trata los errores de almacenamiento sin cerrar la app.

DOCUMENTACIÓN Y VALIDACIÓN
- Explica qué archivos cambiaste, por qué y cómo se mantiene MVVM.
- No inventes capturas, compilaciones exitosas ni pruebas realizadas.
- Mantén README con estructura, diagrama, funcionalidades y pruebas pendientes o realmente realizadas.
- Indica cómo verificar: persistencia, ambos canales, texto expandido, permiso denegado, cancelar, modo avión, rotación y disposición horizontal.
- Ejecuta assembleDebug si puedes. Si no puedes, indica que falta compilar y proporciona el comando.
- Propón mensajes de commit que describan las mejoras. El GLAB requiere un mínimo de tres commits en mejora-ia.

Primero resume los problemas reales que encuentres. Después aplica los cambios necesarios en archivos completos y coherentes, evitando duplicar lo ya implementado. Al terminar, explícame una mejora de forma sencilla para poder sustentarla ante el profesor.
```

## Registro solicitado por el GLAB

Completar después de enviar el prompt y revisar la respuesta.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Prompt de rediseño, adaptación y mejoras reproducido arriba. | Pendiente: resumir la respuesta real. | Pendiente: detallar los cambios aceptados o rechazados. | Pendiente: explicar el criterio y cómo se verificó. |

## Prompts de seguimiento preparados

### Revisar arquitectura

> Revisa si algún ViewModel usa Context o NotificationManager, si el Repository usa Context, si se crea más de una instancia de DataStore y si hay receptores que no se quitan en onDispose. Señala el archivo y el problema real antes de corregirlo. Conserva la funcionalidad existente.

### Revisar vertical y horizontal

> Revisa PantallaAvisos y el historial en ventanas estrechas y anchas. Corrige posibles textos cortados, botones fuera de pantalla y desplazamientos anidados. Usa una columna en espacios estrechos y dos cuando el ancho lo permita. Conserva la cuenta regresiva al rotar y no bloquees la orientación.

### Preparar la sustentación

> Explícame con palabras claras cómo funciona el historial con DataStore, por qué existen Avisos y Urgentes y qué aporta BigTextStyle. Después explícame por qué el ViewModel emite un evento y no muestra la notificación directamente. Basa la respuesta en el código real del proyecto.
