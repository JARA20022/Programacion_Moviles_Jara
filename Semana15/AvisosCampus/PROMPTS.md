# Registro de asistencia con IA

<!-- AVISOSCAMPUS_MEJORA -->
## Registro real — ChatGPT

| Prompt que escribí | Qué respondió ChatGPT | Qué se incorporó o descartó | Por qué |
|---|---|---|---|
| “Dame el script para la rama mejora-ia que ya existe, cambia desde main y mejora toda la interfaz con colores vivos, animaciones y un diseño muy diferente.” | Script para macOS con cambio de rama, compilación por etapa, tres commits y pushes, diseño azul noche/violeta/turquesa con contador animado. | El script implementa un tema nuevo y distribución por ancho; mantiene permisos, avisos, recordatorio, receptor e historial. | Cambiar la presentación conservando las funciones del laboratorio. |
| “Con todo lo que pide el GLAB, responsive, vertical y horizontal y las mejoras que pide.” | Selección de historial persistente con DataStore, dos canales y BigTextStyle. | Repository sin Context, ViewModel con manejo de borrado y errores, Urgentes para recordatorios y texto expandible. | Son tres mejoras expresamente enumeradas en la guía; la persistencia cambia Repository y ViewModel. |
| “Que no me pida obligatoriamente tomar una captura.” | Capturas opcionales, instrucciones de prueba y documentación. | Se preservan las evidencias de main y no se crean capturas ficticias ni se marcan pruebas sin realizarlas. | Permitir completar el flujo sin bloquearlo por capturas nuevas. |

## Revisión con Gemini en Android Studio — pendiente de realizar

El script no ha conversado con Gemini. Para completar esa parte del GLAB, abre Gemini en Android Studio, adjunta los archivos pertinentes y pega este prompt:

> Revisa AvisosCampus en la rama mejora-ia. Es un laboratorio de Kotlin + Jetpack Compose + Material 3 con MVVM + Repository. Hemos añadido DataStore para el historial, canal Urgentes para recordatorios y BigTextStyle. Revisa estos archivos: AvisosApp.kt, AvisosRepository.kt, AvisosViewModel.kt, Notificador.kt, PantallaAvisos.kt e HistorialAvisos.kt. Comprueba que no haya Context ni NotificationManager en ViewModel, que Repository no use Context, que exista una sola instancia de DataStore, que el BroadcastReceiver se quite en onDispose y que los PendingIntent sean FLAG_IMMUTABLE. Comprueba que la cuenta no se reinicie al girar y que el diseño funcione en vertical y horizontal. Propón una mejora concreta o corrige un problema real sin romper estas reglas. Explica los cambios y cómo probarlos.

Después de usarlo, registra los resultados reales:

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Prompt de revisión anterior (solo registrar como realizado después de enviarlo) | Pendiente: pegar resumen de la respuesta real. | Pendiente: describir lo que realmente se cambió o descartó. | Pendiente: justificar la decisión y la prueba realizada. |

No se atribuye a Gemini el código generado por ChatGPT. La revisión y cualquier corrección posterior pueden añadirse en un commit adicional; el GLAB pide un mínimo de tres, no un máximo.
