# AvisosCampus — Semana 15

Autor: IVAN RODRIGO JARA AYALA. Programación en Móviles, TECSUP.

Aplicación local con Kotlin, Jetpack Compose y Material 3. Muestra avisos, programa un recordatorio de 10 segundos y escucha cambios del modo avión. El historial queda en memoria y se conserva al rotar; se pierde al terminar el proceso.

## Ejecución

Abrir esta carpeta en Android Studio, sincronizar Gradle y ejecutar en Android 13 o superior para probar POST_NOTIFICATIONS. No necesita internet ni claves para funcionar. La primera compilación sí puede descargar dependencias.

## Estructura

```text
com.tecsup.avisoscampus
  MainActivity.kt
  AvisosApp.kt
  model/Modelos.kt
  data/AvisosRepository.kt
  viewmodel/AvisosViewModel.kt
  notificacion/Notificador.kt
  receiver/ModoAvionReceiver.kt
  ui/PantallaAvisos.kt
  ui/HistorialAvisos.kt
```

## MVVM + Repository

```mermaid
flowchart TD
 V["PantallaAvisos"] --> VM["AvisosViewModel"]
 VM --> R["AvisosRepository: interface"]
 I["AvisosRepositoryMemoria"] -. implementa .-> R
 V --> N["Notificador"]
 N --> A["Android"]
```

El ViewModel registra avisos y emite eventos. La pantalla recoge los eventos y llama al Notificador. Solo las clases que interactúan con Android usan Context. El receptor únicamente entrega el cambio del modo avión al ViewModel, y se desregistra en onDispose.

## Evidencias

### Notificación en el panel

![Notificación en el panel](capturas/01-notificacion.png)

### Recordatorio con cuenta regresiva

![Recordatorio con cuenta regresiva](capturas/02-recordatorio.png)

### Cambio del modo avión

![Cambio del modo avión](capturas/03-modo-avion.png)

### Historial de avisos

![Historial de avisos](capturas/04-historial.png)

### Permiso denegado

![Permiso denegado](capturas/05-permiso-denegado.png)

## Para pensar

1. **Canal y permiso:** desde Android 8 se requiere un canal; si falta, la notificación no se muestra. Desde Android 13 se requiere la autorización POST_NOTIFICATIONS. Si se deniega, esta app guarda el aviso en el historial sin mostrarlo en el panel.
2. **Eventos del ViewModel:** separan la lógica de la app del sistema Android. El ViewModel no necesita Context ni NotificationManager y se puede probar de forma independiente.
3. **Mismo id:** una notificación con el mismo id y tag actualiza o sustituye la anterior. Se usan identificadores distintos para conservar varias notificaciones.
4. **onReceive corto y desregistro:** el receptor no debe bloquear el hilo principal; un trabajo largo puede provocar que la app no responda. Al salir de la composición se quita el receptor para evitar registros duplicados y referencias que ya no se necesitan.
5. **Local o push:** una notificación local sirve para un recordatorio generado en el teléfono o un cambio del sistema. Una push de Firebase sirve para avisos que llegan desde un servidor, como un mensaje o una novedad compartida.

## Alcance

El recordatorio usa viewModelScope: continúa al minimizar y al rotar, mientras el proceso sigue vivo. No garantiza ejecución después de matar el proceso ni tras reiniciar el teléfono. El historial de esta fase no es persistente.

## Registro del trabajo

Implementación asistida por IA y revisión manual por etapas. Las capturas corresponden al emulador utilizado durante la ejecución. Esta entrega cubre Fase 1, rama main; la Fase 2 del GLAB se desarrolla después en mejora-ia.

## Pruebas de aceptación

- [x] Existe el canal Avisos (verificado por el estudiante durante la ejecución).
- [x] Se solicita el permiso y cambia la etiqueta (verificado por el estudiante durante la ejecución).
- [x] Mostrar aviso crea Tarea lista (verificado por el estudiante durante la ejecución).
- [x] Dos avisos tienen ids diferentes (verificado por el estudiante durante la ejecución).
- [x] Tocar abre la app y retira la notificación (verificado por el estudiante durante la ejecución).
- [x] Cuenta regresiva finaliza con un aviso (verificado por el estudiante durante la ejecución).
- [x] Recordatorio funciona al minimizar (verificado por el estudiante durante la ejecución).
- [x] Cancelar evita el aviso (verificado por el estudiante durante la ejecución).
- [x] Switch activo recibe activado y desactivado (verificado por el estudiante durante la ejecución).
- [x] Switch apagado no registra avisos de modo avión (verificado por el estudiante durante la ejecución).
- [x] Permiso denegado conserva historial y muestra error sin cierre (verificado por el estudiante durante la ejecución).
- [x] Rotación conserva cuenta e historial (verificado por el estudiante durante la ejecución).
- [x] No hay Context en ViewModel y el receptor se quita en onDispose (verificado por el estudiante durante la ejecución).
