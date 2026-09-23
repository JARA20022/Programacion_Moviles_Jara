# TECSUP Fit â€” OpciÃ³n B

AplicaciÃ³n acadÃ©mica de reserva de clases de gimnasio con Kotlin y Jetpack Compose. Integra layouts, controles, LazyRow, LazyColumn y navegaciÃ³n de las semanas 1 a 6.

- **Alumno:** IvÃ¡n Jara Ayala.
- **Documento:** LAB_ACTUALIZADO-ACTIVIDAD_SEMANA1-6.docx, opciÃ³n B.
- **Proyecto:** `Semana05/TecsupFit`, dentro del repositorio `Programacion_Moviles_Jara`.
- **Paquete:** `com.jara.tecsupfit`.
- **VersiÃ³n documentada:** fase base de `main`.

## Alcance de esta entrega

El Word permite elegir una de las dos opciones. TECSUP Fit se prepara como alternativa adicional; la clÃ­nica se conserva en su carpeta.

Esta versiÃ³n base se organiza en ocho avances con commits descriptivos. El Word exige que estÃ©n distribuidos durante el desarrollo, no todos el mismo dÃ­a. Un nÃºmero de commits por sÃ­ solo no acredita esa distribuciÃ³n.

La fase de mejora con IA requiere tres commits adicionales y PROMPTS.md en mejora-ia. Esa fase de TECSUP Fit todavÃ­a no estÃ¡ implementada en estos archivos. La mejora de la clÃ­nica no se presenta como mejora de este proyecto.

## Requerimientos funcionales del Word

| CÃ³digo | Requerimiento | ImplementaciÃ³n |
|---|---|---|
| RF01 | LazyRow con Hoy y Esta semana; LazyColumn con al menos tres clases, nombre y horario. | Inicio muestra Yoga funcional, Cross Training y Spinning. Esta semana incluye tambiÃ©n la sesiÃ³n de Yoga de ayer mostrada en la referencia. |
| RF02 | Detalle recibe la clase elegida por parÃ¡metro y ofrece Reservar cupo. | `detalle/{claseId}` usa un argumento entero y recupera la clase. |
| RF03 | ConfirmaciÃ³n resume clase y horario y permite ver reservas. | `confirmacion/{reservaId}` consulta la reserva creada y muestra sus datos. |
| RF04 | BottomBar con Inicio, Reservas, Rutinas y Perfil, resaltando el destino activo. | NavigationBar en el bottomBar del Scaffold; la selecciÃ³n se obtiene de la ruta del NavController. |
| RF05 | Reservas en LazyColumn con Confirmada y Completada diferenciadas. | Nuevas reservas con etiqueta verde y franja lateral; Yoga de ayer con etiqueta gris. |
| RF06 | Perfil con datos del usuario y estadÃ­sticas simples. | Diego Ramos, DR, Plan Premium, 14 Clases y 3 Rachas, como en la figura 4. |

AdemÃ¡s, la rÃºbrica pide selecciÃ³n Ãºnica de horario/cupo. Se implementa un selector de **cantidad de cupos: 1, 2 o 3**, controlado con una sola variable. Es una decisiÃ³n de implementaciÃ³n para ese criterio: la maqueta no dibuja este control ni fija esas cantidades.

## DiseÃ±o y datos de la referencia

- Encabezado verde y texto blanco: TECSUP Fit / Hola, Diego.
- Verde aproximado `#10735E`; acentos y estados `#169E7C`; fondo verde claro `#E0F5EE`.
- Fondo blanco, tarjetas `#F0F0F0`, estado completado `#E5E5E5`.
- MÃ¡rgenes principales 16 dp, tarjetas con esquinas de 12 dp; valores adaptados a la referencia, que no especifica medidas exactas ni cÃ³digos de color.
- Yoga funcional: 7:00 am, Sala 2. Cross Training: 6:00 pm, Sala 1. Spinning: 7:30 pm, Sala 3.
- Solo Cross Training tiene en el documento descripciÃ³n, duraciÃ³n de 45 min, capacidad de 12 y 8 cupos disponibles. Estos campos no se inventan para las otras clases.
- Las reservas confirmadas de Cross Training descuentan la cantidad seleccionada de sus ocho cupos iniciales. Se impide superar ese saldo. Para las otras clases no se simula una capacidad total desconocida.
- Hoy muestra las tres sesiones de hoy. Esta semana aÃ±ade Yoga de ayer a las 7:00 am, dato visible en Reservas. Esta sesiÃ³n pasada puede consultarse, pero su botÃ³n de reserva estÃ¡ deshabilitado.
- La sesiÃ³n de ayer se considera parte de la semana de demostraciÃ³n. No se consulta la fecha real del dispositivo ni un calendario.
- Rutinas es una pestaÃ±a funcional con estado vacÃ­o: el documento no define su contenido ni ejercicios. No se inventa un programa de entrenamiento.
- La figura no muestra barras en ConfirmaciÃ³n; esa pantalla utiliza Scaffold con su contenido y padding. La barra inferior estÃ¡ en las cuatro secciones principales, no en Detalle ni ConfirmaciÃ³n.

## EstadÃ­sticas y estado

Las estadÃ­sticas 14 Clases y 3 Rachas son datos histÃ³ricos de demostraciÃ³n de la figura 4. No se calculan a partir de una Ãºnica reserva precargada ni aumentan al reservar: reservar una clase no acredita asistencia. No se implementa un registro de asistencia porque no estÃ¡ solicitado.

`remember` y `mutableStateOf` mantienen las reservas y el filtro en NavegacionFit. La selecciÃ³n de cupos pertenece al detalle. No se usa ViewModel ni MVVM. Navegar entre pestaÃ±as conserva los datos durante la sesiÃ³n; recrear la actividad o cerrar la aplicaciÃ³n puede reiniciarlos. No hay base de datos ni servidor.

## OrganizaciÃ³n final

| Archivo o carpeta | Responsabilidad |
|---|---|
| MainActivity.kt | Iniciar Compose y aplicar la paleta clara. |
| model/ModelosFit.kt | ClaseFit, ReservaFit y UsuarioFit. |
| data/DatosFit.kt | Clases, usuario y reserva completada del ejemplo. |
| navigation/AppNavigation.kt | NavHost, Ãºnico NavController, argumentos y estado compartido. |
| screens/InicioScreen.kt | Filtros y lista de clases. |
| screens/DetalleScreen.kt | Datos de clase y selector de cupos. |
| screens/ConfirmacionScreen.kt | Resumen de la reserva. |
| screens/ReservasScreen.kt | Lista de reservas y estados. |
| screens/PerfilScreen.kt | Datos y estadÃ­sticas del usuario. |
| screens/RutinasScreen.kt | Destino Rutinas y su estado vacÃ­o. |
| ui/components/ComponentesFit.kt | Barra inferior, tarjetas, icono de pesa y estadÃ­sticas. |
| ui/theme/FitColors.kt | Paleta compartida. |

Inicialmente se trabaja en MainActivity.kt. El octavo avance separa estos archivos sin cambiar el comportamiento. Se reutiliza el enfoque utilizado en la clÃ­nica: Scaffold con padding, navegaciÃ³n por ID entero, componentes que reciben datos y estado compartido con remember. No se afirma que se haya copiado literalmente cÃ³digo del ZIP.

## Abrir y ejecutar

1. Clona `https://github.com/JARA20022/Programacion_Moviles_Jara.git`.
2. Cambia a `main` y abre en Android Studio la carpeta `Semana05/TecsupFit`, donde estÃ¡n settings.gradle.kts y gradlew.bat.
3. Sincroniza Gradle. Se usa el proyecto Empty Activity generado por Android Studio, Material 3 y Navigation Compose 2.7.7, la dependencia ya utilizada en la clÃ­nica.
4. El SDK de compilaciÃ³n de esta preparaciÃ³n es 37, igual al que resolviÃ³ la compilaciÃ³n de la clÃ­nica. Debe estar instalado y admitido por la configuraciÃ³n Gradle del proyecto generado. Conserva el minSdk y las versiones de plugins del proyecto generado; no pegues un build.gradle completo de otro proyecto.
5. Selecciona emulador o dispositivo y pulsa Run.

Desde PowerShell, dentro de `Semana05/TecsupFit`:

```powershell
.\gradlew.bat assembleDebug
```

Ese comando compila el APK; no instala ni inicia la aplicaciÃ³n. Para verla utiliza Run.

## Uso y comprobaciÃ³n manual

Estas comprobaciones deben ejecutarse en Android Studio; no se presentan como pruebas ya superadas.

1. En Hoy aparecen las tres clases del diseÃ±o; Esta semana muestra ademÃ¡s Yoga de ayer.
2. Abre cada clase y comprueba nombre, hora y sala. La sesiÃ³n de ayer no permite reservar.
3. En Cross Training selecciona 2 cupos: solo esa opciÃ³n debe quedar seleccionada.
4. Reserva: la confirmaciÃ³n muestra Cross Training, Hoy, 6:00 pm, Sala 1 y 2 cupos.
5. Ver mis reservas muestra la reserva Confirmada y Yoga de ayer Completada.
6. Regresa al detalle de Cross Training: debe indicar 6 de 12 cupos disponibles.
7. Abre Inicio, Reservas, Rutinas y Perfil: cada pestaÃ±a debe funcionar y resaltar el cÃ­rculo y texto activos.
8. Perfil muestra los datos de la figura y mantiene sus estadÃ­sticas histÃ³ricas al reservar.
9. Recorre las pestaÃ±as y vuelve a Reservas: las reservas de la sesiÃ³n se conservan.
10. Prueba AtrÃ¡s desde Detalle y desde las secciones principales; comprueba que puedes volver a Inicio.

## PreparaciÃ³n para la sustentaciÃ³n

- Inicio envÃ­a el ID de la clase a detalle/{claseId}; se crea una ReservaFit y su ID pasa a confirmacion/{reservaId}.
- El bottomBar pertenece a Scaffold y recibe la ruta actual; no hay un NavController por pestaÃ±a.
- La selecciÃ³n Ãºnica compara cada cantidad con una sola variable cupos. selectableGroup y Role.RadioButton expresan esa selecciÃ³n tambiÃ©n para accesibilidad.
- Asignar una nueva lista de reservas provoca la actualizaciÃ³n de Compose.
- Los modelos y archivos separados organizan el cÃ³digo; no constituyen MVVM.

## Asistencia y referencias

La preparaciÃ³n de los archivos recibiÃ³ asistencia de ChatGPT. La solicitud fue desarrollar la opciÃ³n B siguiendo el Word, con ocho commits de base y entregas completas. Los avances describen cambios reales; no deben utilizarse para alterar fechas o simular pruebas no realizadas.

- Documento de evaluaciÃ³n actualizado y sus figuras 3 y 4.
- [Navigation â€” Android Developers](https://developer.android.com/guide/navigation).
- [Navigation bar â€” Android Developers](https://developer.android.com/develop/ui/compose/components/navigation-bar).