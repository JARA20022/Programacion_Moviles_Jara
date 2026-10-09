# Clínica SaludPlus — App Paciente

**Autor:** Ivan Jara Ayala  
**Curso:** Programación en Móviles — TECSUP  
**Laboratorio:** Semana 06  
**Proyecto:** Semana06/saludplus  
**Paquete:** com.saludplus.citas  
**Rama:** mejora-ia

## Descripción

Aplicación de citas médicas creada desde cero por indicación del profesor. Permite registrarse, iniciar sesión, buscar especialidades, elegir un médico, reservar una cita, consultar las citas del paciente y cerrar sesión.

Esta rama conserva la base de main y agrega el calendario dinámico, mejoras visuales y correcciones asistidas por IA.

Los usuarios y las citas se almacenan en memoria dentro de Repositorio. No utiliza base de datos, servidor, Room ni Firebase. Los datos se pierden al finalizar el proceso de la aplicación.

## Flujo principal

Inicio → Especialidades → Médicos → Fecha y hora → Confirmar cita → Cita exitosa → Mis citas.

La barra inferior permite ingresar a Inicio, Citas, Resultados y Perfil.

## Ubicación de los requisitos de la rúbrica

Todas las rutas de esta tabla comienzan en:

`Semana06/saludplus/app/src/main/java/com/saludplus/citas/`

| Requisito | Ruta y ubicación |
|---|---|
| Organización por capas y módulos | Paquetes `data/model`, `data/repository`, `navigation`, `ui/components` y `ui/screens`. |
| Modelos | `data/model/Usuario.kt`, `data/model/Especialidad.kt`, `data/model/Medico.kt` y `data/model/Cita.kt`. |
| Repositorio con colecciones | `data/repository/Repositorio.kt`: usuarios, especialidades, médicos y citas en memoria. |
| Registro con validaciones | `ui/screens/auth/RegistroScreen.kt` y `data/repository/Repositorio.kt`. |
| Inicio de sesión | `ui/screens/auth/LoginScreen.kt`: comprobación mediante `Repositorio.iniciarSesion`. |
| Sesión del paciente | `data/repository/Repositorio.kt`: propiedad `usuarioActual`. |
| Saludo con el nombre | `ui/screens/home/HomeScreen.kt`: obtiene el nombre de la sesión. |
| Perfil y cierre de sesión | `ui/screens/perfil/PerfilScreen.kt` y `navigation/AppNavigation.kt`. |
| NavigationBar con cuatro destinos | `navigation/AppNavigation.kt`, función `BarraPrincipal`: Inicio, Citas, Resultados y Perfil. |
| Rutas y paso de parámetros | `navigation/Rutas.kt` y `navigation/AppNavigation.kt`: especialidadId, medicoId, fecha, hora y citaId. |
| LazyRow de especialidades destacadas | `ui/screens/home/HomeScreen.kt`. |
| LazyColumn y búsqueda de especialidades | `ui/screens/agendamiento/EspecialidadesScreen.kt`. |
| LazyColumn de médicos por especialidad | `ui/screens/agendamiento/MedicosScreen.kt`. |
| Selección de fecha y hora | `ui/screens/agendamiento/FechaHoraScreen.kt`. |
| LazyVerticalGrid de horarios | `ui/screens/agendamiento/FechaHoraScreen.kt`: cuadrícula de tres columnas. |
| Bloqueo de horarios reservados | `data/repository/Repositorio.kt`: `horariosDisponibles` y `agendarCita`. |
| Continuar habilitado con selección válida | `ui/screens/agendamiento/FechaHoraScreen.kt`: comprueba fecha visible y hora disponible. |
| Confirmación y creación de la cita | `ui/screens/agendamiento/ConfirmarCitaScreen.kt`. |
| Resultado de la reserva | `ui/screens/agendamiento/CitaExitosaScreen.kt`. |
| popUpTo después de confirmar | `navigation/AppNavigation.kt`, dentro de `onCitaAgendada`. |
| LazyColumn de citas y mensaje de lista vacía | `ui/screens/citas/MisCitasScreen.kt`. |
| Calendario dinámico de mejora-ia | `ui/screens/agendamiento/FechaHoraScreen.kt`: LocalDate, días hábiles, flechas y mes actualizado. |
| Fecha visible en español | `ui/screens/agendamiento/ConfirmarCitaScreen.kt`. |
| Commits de ambas fases | Historial de las ramas main y mejora-ia correspondiente a `Semana06/saludplus`. |
| Documentación de la asistencia | `Semana06/saludplus/PROMPTS.md`. |

La asistencia y la puntualidad de entrega son criterios que verifica el profesor; no se demuestran mediante archivos Kotlin.

## Funciones del repositorio

Ubicación:

`Semana06/saludplus/app/src/main/java/com/saludplus/citas/data/repository/Repositorio.kt`

| Función | Qué hace |
|---|---|
| registrarUsuario | Valida los datos, evita duplicados, agrega el usuario y establece la sesión. |
| iniciarSesion | Busca el usuario por correo y contraseña y actualiza la sesión. |
| cerrarSesion | Limpia la sesión actual. |
| buscarEspecialidades | Filtra especialidades por el texto escrito. |
| especialidadesDestacadas | Obtiene las primeras especialidades para Inicio. |
| obtenerEspecialidad | Busca una especialidad por su ID. |
| obtenerMedico | Busca un médico por su ID. |
| obtenerCita | Busca una cita que pertenezca al paciente de la sesión. |
| medicosPorEspecialidad | Filtra médicos por especialidad y los ordena por calificación. |
| buscarMedicos | Busca médicos por nombre y los ordena por calificación. |
| horariosDisponibles | Excluye turnos ocupados y fechas u horas no permitidas. |
| agendarCita | Comprueba la sesión y la disponibilidad antes de guardar una reserva. |
| citasDelUsuario | Obtiene las citas del paciente y las ordena por fecha y hora. |
| cancelarCita | Elimina una cita del paciente de la sesión; corresponde al reto extra. |
| errorRegistro | Función auxiliar para comprobar los campos del registro. |
| errorLogin | Función auxiliar para comprobar los campos de ingreso. |

Las funciones auxiliares privadas normalizarNombre, normalizarCorreo y correoValido preparan y comprueban los datos antes de utilizarlos.

Se utilizan operaciones de colecciones como any, find, filter, map, take, sortedByDescending y sortedWith.

## Mejora obligatoria del calendario

En main, los días se muestran mediante una lista fija. En mejora-ia:

- LocalDate genera cinco días hábiles desde hoy.
- Se excluyen sábados, domingos y días anteriores.
- Las flechas avanzan o retroceden una semana.
- No se permite retroceder antes de la posición inicial.
- El título muestra el mes y año de las fechas presentadas.
- Cambiar de día reinicia la hora seleccionada.
- Cambiar de semana reinicia fecha y hora.
- Los horarios reservados dejan de estar disponibles.
- Confirmar cita muestra la fecha en español y conserva internamente el formato ISO.

La implementación mantiene un límite de navegación de 12 semanas y de reserva de 90 días. Estos límites son decisiones del proyecto, no requisitos adicionales del PDF.

La generación utiliza siete fechas consecutivas y filtra los fines de semana para obtener cinco días hábiles, evitando una búsqueda indefinida de fechas.

## Diseño y componentes

Rutas relativas a `Semana06/saludplus/app/src/main/java/com/saludplus/citas/`:

- `ui/screens/auth/SplashScreen.kt`: portada ilustrada y función auxiliar SplashVisual.
- `ui/components/Componentes.kt`: BotonPrincipal, FotoMedico, IconoEspecialidad e IconoInicio.
- `ui/screens/auth/RegistroScreen.kt`: campos con íconos y desplazamiento.
- `ui/screens/auth/LoginScreen.kt`: ingreso con adaptación al teclado.
- Las pantallas de médicos, fecha y confirmación comparten la misma asignación de fotografías.

Recursos relativos a `Semana06/saludplus/`:

- `app/src/main/res/drawable/doctor_splash.png`.
- `app/src/main/res/drawable-nodpi/medico_1.png`.
- `app/src/main/res/drawable-nodpi/medico_2.png`.
- `app/src/main/res/drawable-nodpi/medico_3.png`.
- `app/src/main/res/drawable-nodpi/medico_4.png`.
- `app/src/main/res/drawable-nodpi/medico_5.png`.
- `app/src/main/res/drawable-nodpi/medico_6.png`.

Las imágenes son ilustrativas.

## Correcciones realizadas

- Las validaciones se integraron en Repositorio.kt.
- Las funciones de RecursosVisuales.kt se trasladaron a Componentes.kt.
- La función de SplashVisual.kt se trasladó a SplashScreen.kt.
- Los archivos adicionales anteriores se retiraron después de trasladar sus funciones.
- Se retiró el archivo adicional ValidacionesSaludPlusTest.kt.
- Los horarios volvieron a utilizar LazyVerticalGrid.
- Se conservó una altura definida para la cuadrícula dentro del desplazamiento de la pantalla.
- Se corrigió la pérdida del import Dp al reunir componentes: Dp y dp deben coexistir porque Kotlin distingue mayúsculas y minúsculas.
- Se conservaron las fotografías, los íconos, las rutas y los callbacks existentes.

## Validaciones conservadas

### Registro e ingreso

- Nombre y apellido con formato y longitud permitidos.
- Correo con formato válido y límite de longitud.
- Celular de nueve dígitos que comienza con 9.
- Contraseña de 6 a 64 caracteres, con letras y números.
- Confirmación de contraseña.
- Rechazo de correo o celular duplicados.
- Comprobación de credenciales contra los usuarios registrados.

### Reservas y acceso a citas

- Comprobación de sesión antes de reservar.
- Rechazo de médicos inexistentes y fechas inválidas.
- Rechazo de fechas pasadas, fines de semana y fechas a más de 90 días.
- Exclusión de horarios que ya pasaron durante el día actual.
- Bloqueo de turnos ocupados.
- Revisión de disponibilidad antes de continuar y antes de guardar.
- Consulta y cancelación de citas limitadas a su propietario.

Estas comprobaciones reducen errores; no garantizan que la aplicación esté libre de fallos.

## Alcance de los retos extra

Rutas relativas al paquete `com.saludplus.citas`:

- Términos: `ui/screens/auth/TerminosScreen.kt`.
- Resultados: `ui/screens/resultados/ResultadosScreen.kt`, con mensaje informativo.
- Detalle: `ui/screens/citas/DetalleCitaScreen.kt`.
- Notificaciones: `ui/screens/notificaciones/NotificacionesScreen.kt`; la navegación actual utiliza una vista temporal.

No se presenta el desarrollo completo de resultados, detalle y notificaciones como terminado. Son retos extra según la guía.

## Ejecutar y comprobar

1. Abrir `Semana06/saludplus` en Android Studio.
2. Sincronizar Gradle.
3. Seleccionar un emulador o dispositivo.
4. Ejecutar la aplicación.

Para comprobar la compilación desde PowerShell:

    .\gradlew.bat :app:assembleDebug

Después, comprobar manualmente:

1. Registro e ingreso con datos válidos e inválidos.
2. Fotografías, íconos y navegación.
3. Cinco días hábiles y cambio de semana, mes y año.
4. Imposibilidad de retroceder antes de la posición inicial.
5. Reinicio de la hora al cambiar de día.
6. Continuar desactivado sin una selección válida.
7. Fecha en español al confirmar.
8. Reserva, bloqueo del turno ocupado y aparición en Mis citas.
9. Cierre de sesión.

BUILD SUCCESSFUL confirma la compilación, no todas las pruebas de funcionamiento. Esta lista describe qué comprobar y no constituye por sí sola un registro de pruebas ejecutadas.

## Git y documentación

La guía solicita un mínimo de ocho commits en main y tres en mejora-ia.

Desde la carpeta del proyecto:

    git log --oneline main -- .
    git log --oneline --no-merges main..mejora-ia -- .

`Semana06/saludplus/PROMPTS.md` contiene la secuencia de instrucciones para reproducir las mejoras. Debe completarse con las respuestas resumidas y correcciones de las ejecuciones reales; la lista de prompts por sí sola no acredita su ejecución.

El README ubica los requisitos del código. La asistencia, puntualidad, condición de desarrollo sin IA y evaluación final no se certifican mediante este archivo.

## Informe del laboratorio

El informe se presenta por separado e incluye:

- Capturas comentadas del funcionamiento.
- Las seis respuestas de reflexión de la tarea Clínica SaludPlus.
- Un mínimo de dos observaciones.
- Un mínimo de dos conclusiones.

Las preguntas abordan la organización de archivos, el uso de Repositorio como object, la actualización de listas y horarios, navigate frente a popUpTo, las correcciones de IA y la comparación entre NavigationDrawer y NavigationBar.

## Barras superiores

`app/src/main/java/com/saludplus/citas/navigation/AppNavigation.kt` muestra una TopAppBar con flecha en Login, Cita agendada, Mis citas y Perfil.

Login regresa a la pantalla anterior o a la bienvenida. Las otras tres flechas llevan a Inicio. La barra inferior y el contenido de las pantallas se conservan.