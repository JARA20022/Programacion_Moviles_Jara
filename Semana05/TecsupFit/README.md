# TECSUP Fit — Opción B

Aplicación académica de reserva de clases de gimnasio con Kotlin y Jetpack Compose. Integra layouts, controles, LazyRow, LazyColumn y navegación de las semanas 1 a 6.

- **Alumno:** Iván Jara Ayala.
- **Documento:** LAB_ACTUALIZADO-ACTIVIDAD_SEMANA1-6.docx, opción B.
- **Proyecto:** `Semana05/TecsupFit`, dentro del repositorio `Programacion_Moviles_Jara`.
- **Paquete:** `com.jara.tecsupfit`.
- **Versión documentada:** fase base de `main`.

## Alcance de esta entrega

El Word permite elegir una de las dos opciones. TECSUP Fit se prepara como alternativa adicional; la clínica se conserva en su carpeta.

Esta versión base se organiza en ocho avances con commits descriptivos. El Word exige que estén distribuidos durante el desarrollo, no todos el mismo día. Un número de commits por sí solo no acredita esa distribución.

La fase de mejora con IA requiere tres commits adicionales y PROMPTS.md en mejora-ia. Esa fase de TECSUP Fit todavía no está implementada en estos archivos. La mejora de la clínica no se presenta como mejora de este proyecto.

## Requerimientos funcionales del Word

| Código | Requerimiento | Implementación |
|---|---|---|
| RF01 | LazyRow con Hoy y Esta semana; LazyColumn con al menos tres clases, nombre y horario. | Inicio muestra Yoga funcional, Cross Training y Spinning. Esta semana incluye también la sesión de Yoga de ayer mostrada en la referencia. |
| RF02 | Detalle recibe la clase elegida por parámetro y ofrece Reservar cupo. | `detalle/{claseId}` usa un argumento entero y recupera la clase. |
| RF03 | Confirmación resume clase y horario y permite ver reservas. | `confirmacion/{reservaId}` consulta la reserva creada y muestra sus datos. |
| RF04 | BottomBar con Inicio, Reservas, Rutinas y Perfil, resaltando el destino activo. | NavigationBar en el bottomBar del Scaffold; la selección se obtiene de la ruta del NavController. |
| RF05 | Reservas en LazyColumn con Confirmada y Completada diferenciadas. | Nuevas reservas con etiqueta verde y franja lateral; Yoga de ayer con etiqueta gris. |
| RF06 | Perfil con datos del usuario y estadísticas simples. | Diego Ramos, DR, Plan Premium, 14 Clases y 3 Rachas, como en la figura 4. |

Además, la rúbrica pide selección única de horario/cupo. Se implementa un selector de **cantidad de cupos: 1, 2 o 3**, controlado con una sola variable. Es una decisión de implementación para ese criterio: la maqueta no dibuja este control ni fija esas cantidades.

## Diseño y datos de la referencia

- Encabezado verde y texto blanco: TECSUP Fit / Hola, Diego.
- Verde aproximado `#10735E`; acentos y estados `#169E7C`; fondo verde claro `#E0F5EE`.
- Fondo blanco, tarjetas `#F0F0F0`, estado completado `#E5E5E5`.
- Márgenes principales 16 dp, tarjetas con esquinas de 12 dp; valores adaptados a la referencia, que no especifica medidas exactas ni códigos de color.
- Yoga funcional: 7:00 am, Sala 2. Cross Training: 6:00 pm, Sala 1. Spinning: 7:30 pm, Sala 3.
- Solo Cross Training tiene en el documento descripción, duración de 45 min, capacidad de 12 y 8 cupos disponibles. Estos campos no se inventan para las otras clases.
- Las reservas confirmadas de Cross Training descuentan la cantidad seleccionada de sus ocho cupos iniciales. Se impide superar ese saldo. Para las otras clases no se simula una capacidad total desconocida.
- Hoy muestra las tres sesiones de hoy. Esta semana añade Yoga de ayer a las 7:00 am, dato visible en Reservas. Esta sesión pasada puede consultarse, pero su botón de reserva está deshabilitado.
- La sesión de ayer se considera parte de la semana de demostración. No se consulta la fecha real del dispositivo ni un calendario.
- Rutinas es una pestaña funcional con estado vacío: el documento no define su contenido ni ejercicios. No se inventa un programa de entrenamiento.
- La figura no muestra barras en Confirmación; esa pantalla utiliza Scaffold con su contenido y padding. La barra inferior está en las cuatro secciones principales, no en Detalle ni Confirmación.

## Estadísticas y estado

Las estadísticas 14 Clases y 3 Rachas son datos históricos de demostración de la figura 4. No se calculan a partir de una única reserva precargada ni aumentan al reservar: reservar una clase no acredita asistencia. No se implementa un registro de asistencia porque no está solicitado.

`remember` y `mutableStateOf` mantienen las reservas y el filtro en NavegacionFit. La selección de cupos pertenece al detalle. No se usa ViewModel ni MVVM. Navegar entre pestañas conserva los datos durante la sesión; recrear la actividad o cerrar la aplicación puede reiniciarlos. No hay base de datos ni servidor.

## Organización final

| Archivo o carpeta | Responsabilidad |
|---|---|
| MainActivity.kt | Iniciar Compose y aplicar la paleta clara. |
| model/ClaseFit.kt | Datos de una clase y su sesión. |
| model/ReservaFit.kt | Clase, cupos y estado de una reserva. |
| model/UsuarioFit.kt | Datos y estadísticas del usuario. |
| data/DatosFit.kt | Clases, usuario y reserva completada del ejemplo. |
| navigation/AppNavigation.kt | NavHost, único NavController, argumentos y estado compartido. |
| screens/InicioScreen.kt | Filtros y lista de clases. |
| screens/DetalleScreen.kt | Datos de clase y selector de cupos. |
| screens/ConfirmacionScreen.kt | Resumen de la reserva. |
| screens/ReservasScreen.kt | Lista de reservas y estados. |
| screens/PerfilScreen.kt | Datos y estadísticas del usuario. |
| screens/RutinasScreen.kt | Destino Rutinas y su estado vacío. |
| ui/components/ComponentesFit.kt | Barra inferior, tarjetas, icono de pesa y estadísticas. |
| ui/theme/FitColors.kt | Paleta compartida. |

Cada modelo tiene su propio archivo. No se conserva ModelosFit.kt junto a los tres modelos separados, porque duplicaría sus declaraciones. Inicialmente se trabaja en MainActivity.kt. El octavo avance separa estos archivos sin cambiar el comportamiento. Se reutiliza el enfoque utilizado en la clínica: Scaffold con padding, navegación por ID entero, componentes que reciben datos y estado compartido con remember. No se afirma que se haya copiado literalmente código del ZIP.

## Abrir y ejecutar

1. Clona `https://github.com/JARA20022/Programacion_Moviles_Jara.git`.
2. Cambia a `main` y abre en Android Studio la carpeta `Semana05/TecsupFit`, donde están settings.gradle.kts y gradlew.bat.
3. Sincroniza Gradle. Se usa el proyecto Empty Activity generado por Android Studio, Material 3 y Navigation Compose 2.7.7, la dependencia ya utilizada en la clínica.
4. El SDK de compilación de esta preparación es 37, igual al que resolvió la compilación de la clínica. Debe estar instalado y admitido por la configuración Gradle del proyecto generado. Conserva el minSdk y las versiones de plugins del proyecto generado; no pegues un build.gradle completo de otro proyecto.
5. Selecciona emulador o dispositivo y pulsa Run.

Desde PowerShell, dentro de `Semana05/TecsupFit`:

```powershell
.\gradlew.bat assembleDebug
```

Ese comando compila el APK; no instala ni inicia la aplicación. Para verla utiliza Run.

## Uso y comprobación manual

Estas comprobaciones deben ejecutarse en Android Studio; no se presentan como pruebas ya superadas.

1. En Hoy aparecen las tres clases del diseño; Esta semana muestra además Yoga de ayer.
2. Abre cada clase y comprueba nombre, hora y sala. La sesión de ayer no permite reservar.
3. En Cross Training selecciona 2 cupos: solo esa opción debe quedar seleccionada.
4. Reserva: la confirmación muestra Cross Training, Hoy, 6:00 pm, Sala 1 y 2 cupos.
5. Ver mis reservas muestra la reserva Confirmada y Yoga de ayer Completada.
6. Regresa al detalle de Cross Training: debe indicar 6 de 12 cupos disponibles.
7. Abre Inicio, Reservas, Rutinas y Perfil: cada pestaña debe funcionar y resaltar el círculo y texto activos.
8. Perfil muestra los datos de la figura y mantiene sus estadísticas históricas al reservar.
9. Recorre las pestañas y vuelve a Reservas: las reservas de la sesión se conservan.
10. Prueba Atrás desde Detalle y desde las secciones principales; comprueba que puedes volver a Inicio.

## Preparación para la sustentación

- Inicio envía el ID de la clase a detalle/{claseId}; se crea una ReservaFit y su ID pasa a confirmacion/{reservaId}.
- El bottomBar pertenece a Scaffold y recibe la ruta actual; no hay un NavController por pestaña.
- La selección única compara cada cantidad con una sola variable cupos. selectableGroup y Role.RadioButton expresan esa selección también para accesibilidad.
- Asignar una nueva lista de reservas provoca la actualización de Compose.
- Los modelos y archivos separados organizan el código; no constituyen MVVM.

## Asistencia y referencias

La preparación de los archivos recibió asistencia de ChatGPT. La solicitud fue desarrollar la opción B siguiendo el Word, con ocho commits de base y entregas completas. Los avances describen cambios reales; no deben utilizarse para alterar fechas o simular pruebas no realizadas.

- Documento de evaluación actualizado y sus figuras 3 y 4.
- [Navigation — Android Developers](https://developer.android.com/guide/navigation).
- [Navigation bar — Android Developers](https://developer.android.com/develop/ui/compose/components/navigation-bar).
