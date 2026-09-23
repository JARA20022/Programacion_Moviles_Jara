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
| RF05 | Menú lateral desde el icono de Inicio con al menos tres destinos. | ModalNavigationDrawer permite acceder a Inicio, Mis citas e Historial médico. |
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
- El menú implementa los tres destinos exigidos. El cuarto destino Perfil que aparece en la imagen no forma parte del mínimo requerido.
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
| `screens/` | Inicio, perfil, agenda, confirmación, mis citas e historial. |
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

## Estado de la sesión

Las citas nuevas se conservan al navegar porque pertenecen al estado compartido de NavegacionClinica. No se guardan en disco: pueden perderse al recrearse la actividad o reiniciarse la aplicación. Las fechas son opciones del ejemplo del Word, sin mes o año; no se consulta un calendario de disponibilidad real.

## Comprobación manual

Estos son los pasos de comprobación, no una afirmación de pruebas automatizadas:

1. Todas muestra tres médicos; cada especialidad muestra el correspondiente.
2. Cada tarjeta abre el perfil correcto y permite regresar.
3. Solo una fecha y una hora quedan seleccionadas.
4. La confirmación refleja los datos elegidos, no un resumen fijo.
5. Una reserva nueva aparece en Mis citas con estado Confirmada.
6. El menú abre sus tres destinos y resalta la sección actual.
7. Historial muestra las citas completadas.

## Preparación para la sustentación

- El ID del médico pasa de Inicio a Perfil y Agenda; al confirmar se crea una Cita y se pasa su ID al resumen.
- ModalNavigationDrawer envuelve la navegación; cada destino conserva su Scaffold.
- Los chips de fecha y hora tienen selección única porque cada grupo compara sus opciones con una sola variable de estado.
- Separar modelos, datos, componentes y pantallas facilita ubicar cambios sin introducir ViewModel.

## Mejora de la rama mejora-ia

Se añadió la cancelación de citas, siguiendo el ejemplo de mejora propuesto en el Word:

1. Mis citas ofrece Cancelar cita solo para reservas Confirmadas.
2. La versión final abre un AlertDialog que muestra el médico, la fecha y la hora.
3. Conservar cita, tocar fuera o pulsar Atrás cierra el diálogo sin cancelar.
4. Sí, cancelar actualiza únicamente esa cita a Cancelada.
5. La cita permanece visible con una etiqueta roja. Las Completadas y Canceladas no ofrecen otra cancelación.

La lista continúa en remember/mutableStateOf. El callback onCancelar actualiza una copia de la cita dentro de una nueva lista. El estado del diálogo también utiliza remember/mutableStateOf.

### Comprobar la mejora

- Registra dos citas confirmadas.
- En una de ellas, abre el diálogo y elige Conservar cita: ambas deben seguir Confirmadas.
- Abre nuevamente el diálogo y confirma: solo esa cita debe aparecer Cancelada.
- Comprueba que la otra cita mantiene sus datos y estado.
- Comprueba que las citas Completadas y Canceladas no muestran Cancelar cita.
- Cambia de sección y vuelve: el estado debe mantenerse durante la sesión.

### Avances de la mejora y correcciones de integración

1. Cancelación de una cita confirmada y representación del estado Cancelada.
2. Confirmación mediante AlertDialog y posibilidad de conservar la cita.
3. Documentación de los prompts, ajustes y uso en README y [PROMPTS.md](PROMPTS.md).

Además de esos avances, el commit `01f9dd5` registró MainActivity y ClinicaColors, que habían quedado pendientes. El mínimo de tres commits no impide registrar correcciones adicionales.

La rama remota mejora-ia ya contenía trabajo de Semana03. Sus commits deben conservarse al integrar la rama; para revisar esta aplicación se puede filtrar el historial con `git log --oneline -- Semana05/Clinica` desde la raíz del repositorio.

La primera versión fue un avance intermedio con cancelación directa. La entrega final de esta rama utiliza el diálogo. La documentación de la asistencia utilizada y el prompt reutilizable están en PROMPTS.md.
