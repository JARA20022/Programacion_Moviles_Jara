# CentroTareas

## Observación del Paso 1

Pulsé «Tarea larga (MAL)» y enseguida toqué «Tócame». Durante unos cuatro segundos el contador no cambió en pantalla. Al terminar la espera vi «Terminó».

El ViewModel asigna «Trabajando...» y ejecuta Thread.sleep(4000) en el hilo principal. Mientras ese hilo duerme, Android no puede dibujar el mensaje ni atender los toques. Al recuperar el control, Compose ya recibe el estado «Terminó».

Si la espera durara diez segundos y yo intentara interactuar, Android podría mostrar el aviso «La aplicación no responde» (ANR).
