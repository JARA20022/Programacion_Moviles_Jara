# Clínica Salud+

Aplicación académica de reserva de citas médicas, desarrollada con Kotlin y Jetpack Compose para integrar los contenidos de las semanas 1 a 6.

**Alumno:** Iván Jara Ayala.  
**Proyecto:** `Semana05/Clinica`, dentro de `Programacion_Moviles_Jara`.  
**Paquete:** `com.jara.clinicasalud`.  
**Caso elegido:** opción A, Clínica Salud+.

## Alcance y ramas

- `main`: aplicación base y organización del código en archivos independientes.
- `mejora-ia`: mejora funcional realizada con ayuda de IA a partir de `main`, documentada en `PROMPTS.md`.

El Word solicita como mínimo ocho commits descriptivos y distribuidos en `main`, y tres adicionales en `mejora-ia`. Los mensajes y las fechas se consultan en el historial real de Git.

El estado se maneja con `remember` y `mutableStateOf`. El proyecto no utiliza ViewModel ni MVVM. La separación en paquetes organiza el código sin cambiar esta forma de manejar el estado.

## Requerimientos funcionales de la opción A

| Código | Requerimiento | Implementación |
|---|---|---|
| RF01 | Inicio con al menos dos especialidades en LazyRow y tres médicos en LazyColumn, con nombre, especialidad y calificación. | Inicio muestra tres médicos. Los chips Todas, Cardiología, Pediatría y Dermatología permiten filtrar la lista. |
| RF02 | Perfil que recibe el médico elegido mediante un parámetro de navegación y ofrece Agendar cita. | La ruta `perfil/{medicoId}` recibe un entero, recupera el médico y abre su perfil. |
| RF03 | Agenda con al menos tres fechas y tres horarios, ambos de selección única. | Jue 26, Vie 27 y Sáb 28; horarios 9:00, 10:30 y 3:00. Una variable controla cada grupo. |
| RF04 | Confirmación con médico, fecha y hora, y botón para volver al inicio. | La cita se registra en el estado compartido y `confirmacion/{citaId}` presenta su resumen. |
| RF05 | Menú lateral desde el icono de Inicio con al menos tres destinos. | ModalNavigationDrawer permite acceder a Inicio, Mis citas, Historial médico y Perfil, como muestra la figura 2. |
| RF06 | Mis citas con LazyColumn y estados Confirmada y Completada diferenciados. | Las nuevas reservas son Confirmadas, con etiqueta verde. La cita completada de ejemplo tiene etiqueta gris. |

El historial consulta las citas completadas de la misma lista. La cita inicial de Luis, Miércoles 15 a las 3:00 pm, procede de la referencia y permite visualizar el estado Completada; no es una reserva creada por el usuario durante esa sesión.

## Referencia visual y decisiones de implementación

- Morado principal: `#5B2A86`.
- Fondo blanco, tarjetas `#F3F1F7`, círculos lavanda `#EEE6F7`.
- Texto principal `#1E1E1E`, secundario `#6E6E6E` y estrellas `#BA8A00`.
- Confirmación verde `#1D9E75` sobre `#E1F5EE`.
- Cada pantalla utiliza Scaffold y aplica el innerPadding del contenido.
- Los tamaños y espacios en dp/sp son una adaptación de las imágenes, que no proporcionan medidas numéricas.
- El botón de Confirmación dice Volver al inicio, siguiendo el requerimiento escrito, aunque la imagen muestra Ver mis citas.
- El menú incluye los cuatro destinos de la figura 2: Inicio, Mis citas, Historial médico y Perfil. El texto exige al menos tres; la implementación también incorpora el cuarto de la referencia.
- Perfil del paciente utiliza la ruta `perfil_paciente`, distinta de `perfil/{medicoId}`. Muestra JP, Juan Pérez y Paciente, los datos visibles en el drawer del Word. El documento no incluye una maqueta de esta pantalla para la opción A: su distribución interior es una adaptación sencilla con Scaffold, topBar, padding y los colores existentes; no se añaden campos personales ni edición.
- El filtrado real incorpora Todas y Dermatología para mantener accesibles los tres médicos.

## Organización del código

| Ubicación dentro de `app/src/main/java/com/jara/clinicasalud` | Responsabilidad |
|---|---|
| `MainActivity.kt` | Iniciar Compose y aplicar el tema. |
| `navigation/AppNavigation.kt` | NavController, NavHost, drawer, rutas y estado compartido de las citas. |
| `model/Medico.kt` | Datos del médico. |
| `model/FechaCita.kt` | Datos de las opciones de fecha. |
| `model/Cita.kt` | Médico, fecha, hora y estado de una cita. |
| `data/DatosClinica.kt` | Médicos y cita completada de ejemplo. |
| `screens/` | Inicio, perfil del médico, agenda, confirmación, mis citas, historial y perfil del paciente. |
| `screens/PerfilPacienteScreen.kt` | Pantalla Perfil del drawer, con los datos de presentación del paciente. |
| `ui/components/ComponentesClinica.kt` | Menú, tarjetas, botones y elementos visuales reutilizables. |
| `ui/theme/ClinicaColors.kt` | Paleta compartida. |
| `ui/theme/Theme.kt`, `Color.kt`, `Type.kt` | Configuración del tema generado por el proyecto. |

La organización toma como referencia los laboratorios anteriores del ZIP: Scaffold, TopAppBar y padding de Semana03; listas, tarjetas y navegación con ID entero del Lab-05. El desarrollo comenzó dentro de MainActivity y posteriormente se separó en archivos.

## Abrir y ejecutar

1. Obtén el repositorio:

   ```powershell
   git clone https://github.com/JARA20022/Programacion_Moviles_Jara.git
   ```

2. En Android Studio, utiliza Open y selecciona `Programacion_Moviles_Jara/Semana05/Clinica`, donde están `settings.gradle.kts` y `gradlew.bat`.
3. Sincroniza Gradle. El proyecto utiliza `compileSdk 37` y Navigation Compose `2.7.7`; instala la plataforma SDK requerida si Android Studio lo solicita.
4. Selecciona un emulador o dispositivo compatible con el `minSdk` indicado en `app/build.gradle.kts`.
5. Ejecuta con Run.

Para compilar desde PowerShell, dentro de `Semana05/Clinica`:

```powershell
.\gradlew.bat assembleDebug
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Uso

1. En Inicio, selecciona una especialidad o Todas.
2. Toca la tarjeta de un médico.
3. Pulsa Agendar cita y elige fecha y hora.
4. Confirma y revisa el resumen.
5. Vuelve a Inicio y abre el menú lateral.
6. Consulta Mis citas y, si corresponde, Historial médico.
7. Abre Perfil desde el menú para consultar los datos de presentación del paciente; puedes volver a Inicio desde el mismo menú.

## Estado de la sesión

Las citas nuevas se conservan al navegar porque pertenecen al estado compartido de NavegacionClinica. No se guardan en disco: pueden perderse al recrearse la actividad o reiniciarse la aplicación. Las fechas son opciones del ejemplo del Word, sin mes o año; no se consulta un calendario de disponibilidad real.

## Comprobación manual

Estos son los pasos de comprobación, no una afirmación de pruebas automatizadas:

1. Todas muestra tres médicos; cada especialidad muestra el correspondiente.
2. Cada tarjeta abre el perfil correcto y permite regresar.
3. Solo una fecha y una hora quedan seleccionadas.
4. La confirmación refleja los datos elegidos, no un resumen fijo.
5. Una reserva nueva aparece en Mis citas con estado Confirmada.
6. El menú abre sus cuatro destinos y resalta la sección actual, incluido Perfil.
7. Historial muestra las citas completadas.
8. Perfil muestra JP, Juan Pérez y Paciente; el botón de menú permite navegar a las otras secciones. Entrar en Perfil no modifica las citas.

## Preparación para la sustentación

- El ID del médico pasa de Inicio a Perfil y Agenda; al confirmar se crea una Cita y se pasa su ID al resumen.
- ModalNavigationDrawer envuelve la navegación; cada destino conserva su Scaffold.
- Los chips de fecha y hora tienen selección única porque cada grupo compara sus opciones con una sola variable de estado.
- Separar modelos, datos, componentes y pantallas facilita ubicar cambios sin introducir ViewModel.

## Corrección de la referencia visual

Se incorporó Perfil, que se había omitido del drawer al implementar solo los tres destinos mínimos del texto. Esta corrección pertenece a la base y se conserva en mejora-ia. No sustituye la mejora de cancelación ni añade un requerimiento funcional escrito que el Word no enumera.
