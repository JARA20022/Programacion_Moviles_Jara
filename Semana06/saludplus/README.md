\# Clínica SaludPlus — App Paciente



\*\*Autor:\*\* Ivan Jara Ayala  

\*\*Curso:\*\* Programación en Móviles — TECSUP  

\*\*Laboratorio:\*\* Semana 06  

\*\*Proyecto:\*\* Semana06/saludplus  

\*\*Rama:\*\* main  

\*\*Paquete:\*\* com.saludplus.citas



\## Descripción



Aplicación de citas médicas desarrollada con Kotlin y Jetpack Compose. Permite crear una cuenta, iniciar sesión, buscar especialidades, elegir un médico, reservar una cita, consultar las citas del paciente y cerrar sesión.



El proyecto se inició desde cero por indicación del profesor.



\## Funcionamiento



1\. Crear una cuenta o iniciar sesión.

2\. Entrar a Agendar cita desde Inicio.

3\. Seleccionar una especialidad y un médico.

4\. Elegir una fecha y un horario disponible.

5\. Confirmar la reserva.

6\. Consultar la cita en Mis citas.



La barra inferior permite ingresar a Inicio, Citas, Resultados y Perfil.



\## Ubicación de los requisitos de la rúbrica



Las rutas de esta tabla comienzan en:



`app/src/main/java/com/saludplus/citas/`



| Requisito | Archivo o ubicación | Qué revisar |

|---|---|---|

| Estructura por paquetes | `data/model`, `data/repository`, `navigation` y `ui/screens` | Los datos, la navegación y las pantallas están separados. |

| Modelos | `data/model/Usuario.kt`, `Especialidad.kt`, `Medico.kt` y `Cita.kt` | Representan los datos utilizados por la aplicación. |

| Repositorio con colecciones | `data/repository/Repositorio.kt` | Listas de usuarios, especialidades, médicos y citas; funciones de búsqueda y reserva. |

| Registro y validaciones | `ui/screens/auth/RegistroScreen.kt` | Campos del registro, mensajes de error y creación del usuario. |

| Inicio de sesión | `ui/screens/auth/LoginScreen.kt` | Comprobación del correo y contraseña mediante `iniciarSesion`. |

| Saludo al paciente | `ui/screens/home/HomeScreen.kt` | Nombre obtenido de `Repositorio.usuarioActual`. |

| Perfil y cierre de sesión | `ui/screens/perfil/PerfilScreen.kt` y `navigation/AppNavigation.kt` | Datos del paciente y llamada a `cerrarSesion`. |

| Flujo completo de reserva | `ui/screens/agendamiento/` | Especialidades → Médicos → Fecha y hora → Confirmar → Cita exitosa. |

| Paso de parámetros | `navigation/Rutas.kt` y `navigation/AppNavigation.kt` | Envío y lectura de `especialidadId`, `medicoId`, `fecha`, `hora` y `citaId`. |

| Uso de `popUpTo` | `navigation/AppNavigation.kt` | En `onCitaAgendada`, retira del historial las pantallas anteriores de la reserva. |

| `NavigationBar` con cuatro destinos | `navigation/AppNavigation.kt` | Función `BarraPrincipal`: Inicio, Citas, Resultados y Perfil. |

| `LazyRow` de especialidades destacadas | `ui/screens/home/HomeScreen.kt` | Lista horizontal obtenida con `especialidadesDestacadas`. |

| `LazyColumn` y búsqueda de especialidades | `ui/screens/agendamiento/EspecialidadesScreen.kt` | Lista que cambia según el texto buscado. |

| Médicos por especialidad | `ui/screens/agendamiento/MedicosScreen.kt` | Lista vertical de médicos filtrados por especialidad y ordenados por calificación. |

| Citas y mensaje de lista vacía | `ui/screens/citas/MisCitasScreen.kt` | `LazyColumn` de citas del paciente y aviso cuando no tiene reservas. |

| `LazyVerticalGrid` de horarios | `ui/screens/agendamiento/FechaHoraScreen.kt` | Horarios en tres columnas y selección de fecha y hora. |

| Bloqueo de horarios reservados | `data/repository/Repositorio.kt` | `horariosDisponibles` excluye turnos ocupados; `agendarCita` comprueba nuevamente la disponibilidad. |

| Botón Continuar condicionado | `ui/screens/agendamiento/FechaHoraScreen.kt` | Solo se habilita con una fecha válida y una hora disponible seleccionadas. |

| Mínimo de ocho commits en main | Historial de Git | Revisar los commits correspondientes a `Semana06/saludplus`. |



La asistencia y la puntualidad de entrega son criterios que verifica el profesor; no se demuestran mediante archivos Kotlin.



\## Calendario de main



Esta rama utiliza una lista fija en `FechaHoraScreen.kt`:



\- 12, 13, 14, 15 y 16 de octubre de 2026.

\- Título fijo: Octubre 2026.

\- No incluye flechas para cambiar de semana.

\- Al cambiar de día se reinicia la selección de hora.



Son fechas de prueba. Cuando pasen, deben actualizarse manualmente porque el repositorio impide reservar citas pasadas.



El calendario automático con los próximos cinco días hábiles, las flechas y el mes dinámico corresponde a la rama `mejora-ia`.



\## Validaciones



`data/ValidacionDatos.kt` centraliza las comprobaciones de nombre, correo, celular y contraseña. Es un archivo auxiliar adicional a la estructura mostrada en la guía.



`Repositorio.kt` también comprueba los datos antes de guardar:



\- Evita registrar correos o celulares repetidos.

\- Rechaza fechas incorrectas, pasadas o de fin de semana.

\- Rechaza médicos inexistentes y horarios no disponibles.

\- Impide reservar sin una sesión válida.

\- Evita duplicar un turno del mismo médico y fecha.

\- Limita la consulta y cancelación de citas al paciente propietario.



\## Alcance



Los datos se conservan en memoria. No se utiliza Room, SQLite, Firebase ni un servidor. Al finalizar el proceso de la aplicación se pierden los usuarios y las citas.



La pantalla Resultados contiene un mensaje informativo. El desarrollo completo de resultados, detalle de cita y notificaciones pertenece a los retos extra y no se presenta aquí como terminado.



Este README ubica las funciones implementadas; no certifica una calificación ni la ausencia total de errores. Las correcciones posteriores recibieron asistencia de IA.



\## Abrir y comprobar



Abrir la carpeta `Semana06/saludplus` en Android Studio, sincronizar Gradle y ejecutar la aplicación en un emulador o dispositivo.



Para comprobar la compilación desde PowerShell:



&#x20;   .\\gradlew.bat :app:assembleDebug



Después, verificar manualmente el registro, el inicio de sesión, la reserva, el bloqueo del horario ocupado, Mis citas y el cierre de sesión.



\## Consultar los commits



Desde la carpeta del proyecto:



&#x20;   git log --oneline main -- .



El informe con capturas, preguntas de reflexión, observaciones y conclusiones se entrega por separado.

