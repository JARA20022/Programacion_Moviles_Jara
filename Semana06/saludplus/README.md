# Clínica SaludPlus — App Paciente

**Autor:** Ivan Jara Ayala  
**Curso:** Programación en Móviles — TECSUP  
**Laboratorio:** Semana 06  
**Proyecto:** Semana06/saludplus  
**Rama:** main  
**Paquete:** com.saludplus.citas

## Descripción

Aplicación de citas médicas desarrollada con Kotlin y Jetpack Compose. Permite registrarse, iniciar sesión, buscar especialidades, seleccionar un médico, reservar una cita, consultar las citas del paciente y cerrar sesión.

El proyecto se inició desde cero por indicación del profesor.

## Funcionamiento

1. Crear una cuenta o iniciar sesión.
2. Seleccionar Agendar cita desde Inicio.
3. Elegir una especialidad y un médico.
4. Seleccionar una fecha y un horario disponible.
5. Confirmar la reserva.
6. Revisar la cita en Mis citas.

La barra inferior permite ingresar a Inicio, Citas, Resultados y Perfil.

## Ubicación de los requisitos de la rúbrica

Las rutas de esta tabla comienzan en:

`app/src/main/java/com/saludplus/citas/`

| Requisito | Archivo o ubicación | Qué revisar |
|---|---|---|
| Organización por paquetes | `data/model`, `data/repository`, `navigation` y `ui/screens` | Separación de datos, navegación y pantallas. |
| Modelos | `data/model/Usuario.kt`, `Especialidad.kt`, `Medico.kt` y `Cita.kt` | Datos utilizados por la aplicación. |
| Repositorio con colecciones | `data/repository/Repositorio.kt` | Listas de usuarios, especialidades, médicos y citas; operaciones de búsqueda y reserva. |
| Registro con validaciones | `ui/screens/auth/RegistroScreen.kt` y `data/repository/Repositorio.kt` | Revisión de campos, confirmación de contraseña y creación del usuario. |
| Inicio de sesión | `ui/screens/auth/LoginScreen.kt` | Llamada a `iniciarSesion` y mensajes cuando los datos son incorrectos. |
| Sesión del paciente | `data/repository/Repositorio.kt` | Propiedad `usuarioActual` y funciones `iniciarSesion` y `cerrarSesion`. |
| Saludo con el nombre | `ui/screens/home/HomeScreen.kt` | Obtiene el nombre de `Repositorio.usuarioActual`. |
| Perfil y cierre de sesión | `ui/screens/perfil/PerfilScreen.kt` y `navigation/AppNavigation.kt` | Datos del paciente y regreso a la bienvenida al cerrar sesión. |
| Flujo completo de reserva | `ui/screens/agendamiento/` | Especialidades → Médicos → Fecha y hora → Confirmar → Cita exitosa. |
| Paso de parámetros | `navigation/Rutas.kt` y `navigation/AppNavigation.kt` | Envío y lectura de `especialidadId`, `medicoId`, `fecha`, `hora` y `citaId`. |
| Uso de `popUpTo` | `navigation/AppNavigation.kt` | En `onCitaAgendada`, retira del historial las pantallas anteriores de la reserva. |
| `NavigationBar` con cuatro destinos | `navigation/AppNavigation.kt` | Función `BarraPrincipal`: Inicio, Citas, Resultados y Perfil. |
| `LazyRow` de especialidades destacadas | `ui/screens/home/HomeScreen.kt` | Lista horizontal obtenida mediante `especialidadesDestacadas`. |
| `LazyColumn` y búsqueda de especialidades | `ui/screens/agendamiento/EspecialidadesScreen.kt` | Lista que cambia según el texto buscado. |
| Médicos por especialidad | `ui/screens/agendamiento/MedicosScreen.kt` | Lista vertical de médicos filtrados por especialidad y ordenados por calificación. |
| Citas y mensaje de lista vacía | `ui/screens/citas/MisCitasScreen.kt` | `LazyColumn` de citas del paciente y aviso cuando no tiene reservas. |
| `LazyVerticalGrid` de horarios | `ui/screens/agendamiento/FechaHoraScreen.kt` | Horarios organizados en tres columnas. |
| Horarios reservados bloqueados | `data/repository/Repositorio.kt` | `horariosDisponibles` excluye turnos ocupados y `agendarCita` vuelve a comprobarlos antes de guardar. |
| Botón Continuar condicionado | `ui/screens/agendamiento/FechaHoraScreen.kt` | Solo se habilita con fecha seleccionada y hora disponible. |
| Mínimo de ocho commits en main | Historial de Git | Revisar los commits correspondientes a `Semana06/saludplus`. |

La asistencia y la puntualidad de entrega son criterios que verifica el profesor; no se demuestran mediante archivos Kotlin.

## Validaciones

Las comprobaciones están dentro de `data/repository/Repositorio.kt`, principalmente en `errorRegistro`, `errorLogin`, `registrarUsuario`, `horariosDisponibles` y `agendarCita`.

Las pantallas de registro e ingreso utilizan el repositorio para mostrar los mensajes de error. No se utiliza un archivo separado de validaciones.

### Registro e ingreso

- Nombre y apellido con letras y separadores permitidos, hasta 60 caracteres.
- Correo con formato válido y hasta 254 caracteres.
- Celular de nueve números que comienza con 9.
- Contraseña de 6 a 64 caracteres, con letras y números.
- Confirmación de contraseña comprobada en Registro.
- Bloqueo de correos y celulares repetidos.
- Comprobación del correo y contraseña contra los usuarios registrados.

### Reservas

- Comprueba que exista una sesión válida.
- Rechaza médicos inexistentes y fechas incorrectas.
- Rechaza fechas pasadas, fines de semana y fechas a más de 90 días.
- Para el día actual, descarta horarios que ya pasaron.
- Evita duplicar un turno del mismo médico y fecha.
- Permite consultar y cancelar únicamente las citas del paciente que inició sesión.

Estas comprobaciones reducen errores de entrada; no garantizan la ausencia total de fallos.

## Calendario de main

Esta rama utiliza una lista fija dentro de `FechaHoraScreen.kt`:

- 12, 13, 14, 15 y 16 de octubre de 2026.
- Título fijo: Octubre 2026.
- Sin flechas para cambiar de semana.
- Al cambiar de día, se reinicia la selección de hora.
- Los horarios se consultan nuevamente para el médico y la fecha elegidos.

Son fechas de prueba. Cuando pasen, deben actualizarse manualmente porque el repositorio impide reservar citas pasadas.

El calendario automático con los próximos cinco días hábiles, las flechas y el mes dinámico corresponde a `mejora-ia`.

## Alcance

Los datos se conservan en colecciones dentro de `Repositorio`. No se utiliza Room, SQLite, Firebase ni un servidor.

Al finalizar el proceso de la aplicación se pierden los usuarios registrados y las citas. Este almacenamiento temporal corresponde al alcance del laboratorio.

La pantalla Resultados muestra un mensaje informativo. El desarrollo completo de resultados, detalle de cita y notificaciones pertenece a los retos extra y no se presenta aquí como terminado.

## Abrir y comprobar

1. Abrir `Semana06/saludplus` en Android Studio.
2. Sincronizar Gradle.
3. Seleccionar un emulador o dispositivo.
4. Ejecutar la aplicación.

Para comprobar la compilación desde PowerShell:

    .\gradlew.bat :app:assembleDebug

La compilación debe terminar con `BUILD SUCCESSFUL`.

El archivo adicional `ValidacionesSaludPlusTest.kt` fue retirado. La compilación no sustituye las pruebas manuales de funcionamiento.

## Comprobaciones manuales

- Intentar registrar nombres, correos, celulares y contraseñas incorrectos.
- Registrar una cuenta válida e iniciar sesión.
- Buscar una especialidad y seleccionar un médico.
- Comprobar que Continuar esté desactivado sin fecha y hora.
- Cambiar de día y comprobar que se desmarque la hora.
- Reservar una cita y verificarla en Mis citas.
- Volver al mismo médico y fecha: el horario reservado no debe aparecer.
- Cerrar sesión y comprobar el regreso a la bienvenida.

## Historial y documentación

Para consultar los commits del proyecto desde esta carpeta:

    git log --oneline main -- .

Este README describe la implementación y ubica los requisitos; no certifica una calificación ni acredita por sí solo la condición de desarrollo sin IA. Las correcciones posteriores recibieron asistencia de IA.

El informe con capturas, respuestas de reflexión, observaciones y conclusiones se entrega por separado.