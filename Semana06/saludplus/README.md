# Clínica SaludPlus

Aplicación de citas médicas para pacientes realizada con Kotlin y Jetpack Compose.

## Funciones

- Registro e inicio de sesión con datos guardados durante la ejecución de la app.
- Búsqueda de especialidades y selección de médicos.
- Selección de días hábiles y horarios disponibles.
- Confirmación de citas y consulta de las citas del paciente.
- Navegación inferior entre Inicio, Citas, Resultados y Perfil.

## Ejecución

Abrir la carpeta `Semana06/saludplus` en Android Studio, esperar la sincronización de Gradle y ejecutar la aplicación en un emulador o teléfono Android. También se puede comprobar la compilación con:

`.\gradlew.bat :app:assembleDebug`

## Datos

Los usuarios y las citas se guardan en colecciones del objeto `Repositorio`. Se pierden al cerrar completamente la aplicación; este laboratorio no utiliza base de datos.

## Ramas

- `main`: desarrollo inicial de las pantallas y el flujo de citas.
- `mejora-ia`: calendario dinámico, correcciones y mejoras visuales asistidas por IA.

Las solicitudes a la IA y las correcciones realizadas están documentadas en `PROMPTS.md`.