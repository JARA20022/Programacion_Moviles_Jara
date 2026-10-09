\# Prompts de mejora — Clínica SaludPlus



Autor: Ivan Jara Ayala

Proyecto: Semana06/saludplus

Paquete: com.saludplus.citas

Base: rama main

Destino: rama mejora-ia



Esta secuencia permite reproducir las mejoras desde la base de main. Los prompts de corrección se aplican únicamente cuando el problema descrito está presente.



Las medidas y los colores representan el diseño trabajado. No se presentan como valores numéricos especificados por el PDF.



Prompt 1: Revisa la base y conserva lo que funciona



Trabaja sobre el proyecto existente Semana06/saludplus, paquete com.saludplus.citas. Primero lee los archivos actuales y la sección “Tarea: Clínica SaludPlus — App Paciente” del GLAB.



La base es la rama main. Las modificaciones deben realizarse en mejora-ia. Comprueba la rama antes de editar. Si mejora-ia ya existe, no la recrees, no la sobrescribas y no fusiones ramas automáticamente. Si encuentras cambios pendientes ajenos a esta tarea, consérvalos.



No empieces otro proyecto. Mantén los modelos Usuario, Especialidad, Medico y Cita, sus propiedades, los datos existentes del repositorio, las rutas y los parámetros de las pantallas.



Conserva el registro, el inicio de sesión, la búsqueda de especialidades, la selección del médico, las reservas, Mis citas, el perfil y el cierre de sesión.



Usa Kotlin y Jetpack Compose con colecciones en memoria. No agregues Room, SQLite, Firebase, backend, XAMPP, ViewModel ni una arquitectura MVVM nueva.



Mantén las firmas de las funciones existentes. En particular:



\- horariosDisponibles(medicoId: Int, fecha: String).

\- agendarCita(usuarioId: Int, medicoId: Int, fecha: String, hora: String).

\- FechaHoraScreen(medicoId, onVolver, onContinuar).

\- ConfirmarCitaScreen(medicoId, fecha, hora, onVolver, onCitaAgendada).



No añadas un parámetro público “ahora” para realizar las validaciones.



Utiliza Repositorio.kt para las comprobaciones de datos y Componentes.kt para los elementos visuales compartidos. Implementa la portada dentro de SplashScreen.kt. No crees ValidacionDatos.kt, RecursosVisuales.kt ni SplashVisual.kt como archivos separados.



La base actual ya tiene validaciones. Inspecciónalas y reutilízalas; no las reemplaces por una versión más débil.



No borres pantallas de retos extra ni recursos utilizados solo porque no formen parte del flujo principal. No cambies lo que ya cumple.





Prompt 2: Prepara los recursos de imágenes existentes



Antes de modificar las pantallas, revisa los recursos de app/src/main/res.



La portada utiliza:



app/src/main/res/drawable/doctor\_splash.png



Los retratos disponibles en la versión trabajada están en:



app/src/main/res/drawable-nodpi/medico\_1.png

app/src/main/res/drawable-nodpi/medico\_2.png

app/src/main/res/drawable-nodpi/medico\_3.png

app/src/main/res/drawable-nodpi/medico\_4.png

app/src/main/res/drawable-nodpi/medico\_5.png

app/src/main/res/drawable-nodpi/medico\_6.png



Si no están en main, recupera únicamente esos recursos de la versión existente de mejora-ia del mismo repositorio:



https://github.com/JARA20022/Programacion\_Moviles\_Jara.git



No copies toda la rama ni sus pantallas para evitar realizar el trabajo de reconstrucción. Transfiere los PNG como archivos binarios, sin convertirlos a texto.



Si no puedes obtener las imágenes, solicita los archivos faltantes. No inventes referencias R.drawable, no guardes páginas HTML con extensión .png y no afirmes que una imagen fue insertada cuando no existe.



Son imágenes ilustrativas. No afirmes que son fotografías reales de las personas cuyos nombres aparecen en los datos de prueba.



Conserva los recursos del ícono de la aplicación. No elimines imágenes basándote únicamente en su nombre.





Prompt 3: Aplica el estilo visual común



Usa el diseño adjunto del GLAB como referencia de composición y conserva las funciones actuales.



Medidas:

\- dp para márgenes, tamaños y espacios.

\- sp para texto.

\- No confundir dp con píxeles ni con dpi.

\- Margen horizontal de 20 dp en pantallas interiores.

\- Margen horizontal de 24 dp en bienvenida y registro.

\- Separación entre elementos relacionados de 8 a 12 dp.

\- Separación entre secciones de 20 a 28 dp.

\- Botones principales de 52 dp de alto.

\- Íconos habituales de 20 a 24 dp.

\- Zonas táctiles de los botones con ícono cercanas a 48 dp.



Paleta:

\- Azul principal: #2864E8.

\- Azul oscuro: #142C68.

\- Fondo claro: #F8FBFF.

\- Texto principal: #1F2937.

\- Texto secundario: #687587.

\- Bordes claros: #E6EBF2.

\- Fondo de fechas sin seleccionar: #F3F6FB.



Tipografía:

\- Títulos de pantalla: aproximadamente 20 a 22 sp, seminegrita o negrita.

\- Títulos de sección: 16 a 18 sp.

\- Texto normal: 13 a 14 sp.

\- Descripciones: 11 a 12 sp.



Tarjetas:

\- Esquinas de aproximadamente 12 dp.

\- Fondo blanco o pastel según la sección.

\- Padding interior de 12 a 16 dp.

\- Elevación discreta, de 1 a 2 dp cuando corresponda.



Conserva los estilos que ya estén aplicados correctamente. No modifiques todas las pantallas innecesariamente ni reduzcas el tamaño de letra para ocultar problemas de espacio.



No fuerces alturas pequeñas en campos de texto si recortan la etiqueta o el contenido.





Prompt 4: Implementa los componentes visuales dentro de Componentes.kt



Mantén BotonPrincipal y sus parámetros texto, onClick, modifier y enabled.



En el mismo archivo ui/components/Componentes.kt, implementa o conserva:



\- FotoMedico.

\- IconoEspecialidad.

\- IconoInicio.



FotoMedico debe recibir medicoId, nombre, modifier y tamaño. Usa imágenes locales, ContentScale.Crop y recorte circular.



Para reproducir la asignación de imágenes que ya tenía el proyecto, conserva este mapa:



\- Médico 1: medico\_1.

\- Médico 2: medico\_3.

\- Médico 3: medico\_2.

\- Médico 4: medico\_5.

\- Médico 5: medico\_4.

\- Médico 6: medico\_3.



La misma identificación debe mostrar la misma imagen en la lista de médicos, la selección de fecha y la confirmación.



IconoEspecialidad debe utilizar el nombre de la especialidad:



\- Medicina general: MedicalServices, azul.

\- Pediatría: ChildCare, naranja.

\- Cardiología: Favorite, rojo.

\- Dermatología: Spa, naranja claro.

\- Traumatología: Healing, azul.



Usa un círculo de unos 42 dp con el color del ícono al 13 % de opacidad y el ícono de aproximadamente el 58 % del tamaño del círculo.



IconoInicio debe representar:

\- Agendar cita: calendario azul.

\- Mis citas: calendario verde.

\- Mis datos: persona morada.

\- Resultados: documento naranja.



No agregues especialidades al repositorio únicamente porque exista un ícono para ellas.



Conserva ambos imports si usas el tipo Dp y las extensiones numéricas:



import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp





Prompt 5: Mejora SplashScreen conservando sus callbacks



Trabaja directamente en ui/screens/auth/SplashScreen.kt.



Conserva:

\- onRegistrarse.

\- onIniciarSesion.



Distribuye la pantalla así:



1\. Fondo #F8FBFF.

2\. Símbolo médico azul centrado, de unos 84 dp.

3\. Cruz formada por dos figuras redondeadas: una de 42 × 78 dp y otra de 78 × 42 dp.

4\. Corazón blanco central de aproximadamente 34 dp.

5\. Texto “Clínica” y “SaludPlus” en dos líneas, centrado, azul #142C68, negrita, aproximadamente 31 sp y 34 sp de interlineado.

6\. Debajo, “Tu salud, nuestra prioridad” en gris y 14 sp.

7\. Ilustración doctor\_splash en el espacio central, completa y sin deformarse.

8\. Botón “Comenzar”, de ancho disponible y 52 dp de alto.

9\. Enlace “Ya tengo una cuenta”.



Comenzar debe ejecutar onRegistrarse. El enlace debe ejecutar onIniciarSesion.



La imagen debe adaptarse al espacio sin tapar los botones. Conserva márgenes horizontales de 24 dp.



Si mantienes una función auxiliar llamada SplashVisual, declárala dentro del mismo SplashScreen.kt. No crees otro archivo para ella.





Prompt 6: Mejora RegistroScreen manteniendo las validaciones



Trabaja en ui/screens/auth/RegistroScreen.kt.



Conserva sus callbacks, los campos existentes y las llamadas a Repositorio.errorRegistro y Repositorio.registrarUsuario.



Diseño:

\- Márgenes horizontales de 24 dp.

\- Título “Crear cuenta”, centrado, 26 sp y negrita.

\- Subtítulo “Regístrate para agendar tus citas”, 14 sp y gris.

\- Separación de unos 27 dp antes del primer campo.

\- Campos con esquinas de 10 dp.

\- Separación de 10 dp entre campos.

\- Borde e ícono activos en #2864E8.

\- Íconos de persona, teléfono, correo y candado.



Orden:

1\. Nombre completo.

2\. Teléfono.

3\. Correo electrónico.

4\. Contraseña.

5\. Confirmar contraseña.



Conserva:

\- Nombre limitado a 60 caracteres y caracteres permitidos.

\- Celular limitado a nueve dígitos.

\- Correo limitado a 254 caracteres.

\- Contraseña y confirmación limitadas a 64 caracteres.

\- Contraseñas ocultas.

\- Comprobación de coincidencia de contraseñas.

\- Mensajes por correo o celular repetidos.



Usa teclado numérico para el celular, de correo para el correo y de contraseña para las claves. El tipo de teclado no reemplaza la validación.



Incluye scroll e imePadding para que el teclado no oculte el botón.



Mantén Registrarme, el acceso a Términos y Condiciones y el enlace Iniciar sesión. No cambies los parámetros de registrarUsuario.





Prompt 7: Conserva Login y adapta su contenido al teclado



Trabaja en ui/screens/auth/LoginScreen.kt.



Mantén el correo, la contraseña, los callbacks y las llamadas a:



\- Repositorio.errorLogin.

\- Repositorio.iniciarSesion.



Conserva los límites de longitud, la contraseña oculta y el mensaje “Correo o contraseña incorrectos”.



Agrega o conserva imePadding y desplazamiento vertical para que el botón Ingresar pueda alcanzarse con el teclado abierto.



No cambies el registro de usuarios ni introduzcas cuentas que permitan ingresar sin comprobar la contraseña.



Conserva el estilo de la aplicación y el acceso a Crear una cuenta.





Prompt 8: Mejora Inicio sin cambiar la navegación



Trabaja en ui/screens/home/HomeScreen.kt.



Conserva el saludo con el primer nombre obtenido de Repositorio.usuarioActual.



Encabezado:

\- “¡Hola, \[primer nombre]!” en 20 a 22 sp.

\- “¿Qué deseas hacer hoy?” en gris.

\- Campana de notificaciones a la derecha.



Accesos:

\- Dos columnas y dos filas.

\- Separación de 10 a 12 dp.

\- Tarjetas de aproximadamente 110 a 116 dp de alto.

\- Íconos de unos 28 a 30 dp.



Colores:

\- Agendar cita: fondo #E6F1FF, calendario azul.

\- Mis citas: fondo #E4F8EE, calendario verde.

\- Mis datos: fondo #F1E8FF, persona morada.

\- Resultados: fondo #FFF0DB, documento naranja.



Debajo, coloca “Especialidades destacadas” y conserva LazyRow con tarjetas de aproximadamente 130 × 108 dp.



Utiliza IconoEspecialidad para mostrar un símbolo distinto según la especialidad. Mantén los datos de especialidadesDestacadas y sus onClick.



Conserva la NavigationBar existente de Inicio, Citas, Resultados y Perfil. No la reemplaces por un NavigationDrawer; el drawer corresponde al otro ejercicio del laboratorio.





Prompt 9: Mejora EspecialidadesScreen conservando LazyColumn y búsqueda



Trabaja en ui/screens/agendamiento/EspecialidadesScreen.kt.



Mantén:

\- El callback de regreso.

\- El callback de selección.

\- La búsqueda en tiempo real.

\- Los datos de Repositorio.buscarEspecialidades.

\- LazyColumn.



Diseño:

\- Flecha superior y título “Especialidades”.

\- Buscador de ancho disponible con lupa y borde claro.

\- Separación entre filas de 8 a 10 dp.

\- Filas de aproximadamente 64 a 72 dp, permitiendo más altura si el texto lo necesita.

\- Esquinas de 12 dp.

\- Padding de 12 dp.

\- IconoEspecialidad dentro de un círculo de 40 a 42 dp.

\- Nombre en 14 sp seminegrita.

\- Descripción en 11 a 12 sp gris.

\- Flecha pequeña al extremo derecho.



Usa exclusivamente las especialidades existentes. Si la búsqueda no produce coincidencias, muestra un mensaje claro.



No sustituyas LazyColumn por una Column con todos los elementos.





Prompt 10: Inserta las fotografías en MedicosScreen



Trabaja en ui/screens/agendamiento/MedicosScreen.kt.



Conserva especialidadId, los callbacks y la lista obtenida mediante medicosPorEspecialidad.



Mantén LazyColumn y el orden por calificación del repositorio.



Cada tarjeta debe mostrar:

\- FotoMedico con el ID y nombre del médico.

\- Retrato circular de aproximadamente 52 a 56 dp.

\- Nombre seminegrita.

\- Especialidad.

\- Estrella y calificación.

\- Años de experiencia.

\- Precio de consulta.

\- Acción para seleccionar al médico.



Usa fondo blanco, esquinas de 12 dp, padding de 12 dp y separación de 10 a 12 dp entre tarjetas.



No reemplaces nombres, precios ni especialidades para imitar literalmente los datos de la figura. Utiliza los valores existentes de Repositorio.





Prompt 11: Implementa el calendario dinámico obligatorio



Trabaja únicamente en FechaHoraScreen.kt y conserva la firma:



FechaHoraScreen(

&#x20;   medicoId: Int,

&#x20;   onVolver: () -> Unit,

&#x20;   onContinuar: (String, String) -> Unit

)



La base de main tiene fechas fijas. En mejora-ia reemplázalas por fechas calculadas con java.time.LocalDate.



Comportamiento:

\- Obtener la fecha actual del dispositivo.

\- Mostrar cinco días hábiles a partir de hoy.

\- Excluir sábados, domingos y días anteriores a hoy.

\- Flecha derecha: avanzar una semana.

\- Flecha izquierda: retroceder una semana.

\- Desactivar la flecha izquierda en la posición inicial.

\- Reiniciar fecha y hora seleccionadas al cambiar de semana.

\- Reiniciar la hora al seleccionar otro día.

\- Actualizar el título del mes y año según las fechas mostradas.



Conserva el límite de navegación existente de 12 semanas, coherente con la validación de hasta 90 días del repositorio. Este límite pertenece a la implementación, no es una condición adicional del PDF.



Usa una generación finita:

1\. Calcular hoy más las semanas seleccionadas.

2\. Construir siete fechas consecutivas.

3\. Filtrar sábados y domingos.

4\. Obtener los cinco días hábiles resultantes.



Evita una secuencia infinita con un filtro de fecha máxima seguido de take(5): podría seguir buscando indefinidamente si ya no encuentra suficientes fechas válidas.



Controla el índice de semana para que permanezca entre 0 y 12.



Diseño:

\- Foto del médico de 56 dp, nombre y especialidad.

\- Fondo de su tarjeta #F3F6FB.

\- Separación de aproximadamente 19 a 20 dp antes del calendario.

\- Flechas y mes centrado.

\- Días de 60 × 74 dp.

\- Separación de 10 dp.

\- Selección azul #2864E8 con texto blanco.

\- Días sin seleccionar con fondo #F3F6FB.



Mantén las fechas internas en formato ISO yyyy-MM-dd.





Prompt 12: Recupera LazyVerticalGrid y conserva los horarios reactivos



Dentro de FechaHoraScreen.kt, muestra los horarios usando obligatoriamente:



LazyVerticalGrid(columns = GridCells.Fixed(3))



No utilices Column con horarios.chunked(3), porque la rúbrica solicita LazyVerticalGrid.



Diseño:

\- Tres columnas.

\- Separación horizontal y vertical de 10 dp.

\- Tarjetas de 51 a 52 dp de alto.

\- Horario seleccionado azul y texto blanco.

\- Horario no seleccionado con fondo claro.



Si la pantalla completa tiene verticalScroll, proporciona una altura finita a la cuadrícula. Para tarjetas de 51 dp y separación de 10 dp:



filas = (cantidadDeHorarios + 2) / 3

altura = filas \* 51 + (filas - 1) \* 10



Calcula esa altura solo cuando haya horarios. Puedes desactivar el desplazamiento interno de la cuadrícula y conservar el de la pantalla.



Obtén los horarios con Repositorio.horariosDisponibles(medicoId, fecha). No guardes una lista inicial que quede desactualizada al reservar.



Mensajes:

\- Sin día: “Selecciona primero un día”.

\- Sin turnos: “No quedan horarios disponibles para este día”.



Continuar solo debe habilitarse cuando:

\- La fecha pertenece a los días actualmente mostrados.

\- Hay una hora seleccionada.

\- Esa hora sigue en la lista disponible.



Al pulsar Continuar, vuelve a comprobar la disponibilidad. Si dejó de estar disponible, limpia la hora y muestra un mensaje.



Conserva BotonPrincipal y onContinuar(fecha, hora).





Prompt 13: Mejora ConfirmarCitaScreen y muestra la fecha en español



Trabaja en ui/screens/agendamiento/ConfirmarCitaScreen.kt.



Conserva medicoId, fecha, hora, onVolver y onCitaAgendada.



Muestra:

\- Flecha y título “Confirmar cita”.

\- Tarjeta clara con FotoMedico, nombre y especialidad.

\- Retrato circular de aproximadamente 56 a 58 dp.

\- Filas con íconos azules para fecha, hora y los demás datos ya existentes.

\- Etiquetas grises y valores oscuros.

\- Separadores claros entre filas.

\- Botón Agendar cita de 52 dp.



Formatea únicamente el texto visible de la fecha con Locale.forLanguageTag("es-PE"). Por ejemplo:



“Miércoles 16 de setiembre 2026”.



Mantén el valor original ISO para Repositorio.agendarCita y la navegación.



Si la fecha no puede convertirse, muestra un mensaje o una representación segura sin cerrar la aplicación.



Mantén:

\- Comprobación de sesión.

\- Comprobación del horario disponible.

\- Protección frente a pulsaciones repetidas.

\- Mensaje de error cuando no puede reservarse.

\- onCitaAgendada(cita.id) únicamente después de una reserva correcta.



No inventes direcciones ni datos clínicos nuevos. Conserva los datos de prueba que ya tenga esta pantalla e identifícalos como tales si se documentan.



No cambies AppNavigation para rehacer un flujo que ya funciona.





Prompt 14: Conserva el refuerzo de datos dentro de Repositorio.kt



Revisa las validaciones presentes en main y consérvalas en mejora-ia.



Todas las comprobaciones compartidas deben permanecer dentro de Repositorio.kt. No crees un archivo adicional ValidacionDatos.kt.



Registro:

\- Normalizar espacios del nombre.

\- Validar nombre y apellido.

\- Rechazar números o símbolos no permitidos en el nombre.

\- Validar el formato del correo.

\- Normalizar el correo para comparar duplicados.

\- Celular con exactamente nueve dígitos y comienzo en 9.

\- Contraseña de 6 a 64 caracteres, con letras y números.

\- Rechazar caracteres de control en la contraseña.

\- Evitar correo o celular repetidos.



Las pantallas deben seguir comprobando los límites de entrada. El repositorio debe validar también antes de guardar, aunque el dato no venga directamente de la pantalla.



Reservas:

\- Médico existente.

\- Usuario existente y sesión correspondiente al usuario que reserva.

\- Fecha válida.

\- Rechazo de días pasados y fines de semana.

\- Conservación del límite existente de 90 días.

\- Para hoy, exclusión de horarios que ya pasaron.

\- Hora perteneciente a horariosBase.

\- Bloqueo del mismo médico, fecha y hora ya reservados.



Conserva las comprobaciones de propiedad en obtenerCita, citasDelUsuario y cancelarCita.



No agregues el parámetro “ahora” a horariosDisponibles ni a agendarCita. Consulta la fecha actual internamente.



No agregues archivos de pruebas adicionales para esta entrega. Eso no significa afirmar que la aplicación ha sido probada exhaustivamente.





Prompt de corrección 1: Reúne los archivos adicionales sin perder funciones



Aplica esta corrección solamente si existen archivos creados en una versión anterior:



\- ValidacionDatos.kt.

\- RecursosVisuales.kt.

\- SplashVisual.kt.



Antes de eliminarlos, traslada su contenido:



\- Validaciones a Repositorio.kt.

\- FotoMedico, IconoEspecialidad e IconoInicio a Componentes.kt.

\- Portada ilustrada a SplashScreen.kt.



Actualiza RegistroScreen y LoginScreen para que usen Repositorio.errorRegistro y Repositorio.errorLogin.



Mantén los nombres y parámetros de las funciones visuales para que las pantallas sigan compilando.



Al reunir archivos:

\- Deja una sola declaración package.

\- Coloca todos los imports antes de las funciones.

\- Conserva los imports necesarios.

\- Evita declaraciones duplicadas.

\- No elimines funciones por el hecho de moverlas.

\- No borres imágenes.



Después de actualizar las referencias, elimina únicamente los archivos adicionales que ya no se necesitan.



Si existe ValidacionesSaludPlusTest.kt de la versión anterior, retíralo conforme a la organización acordada de esta entrega. Conserva las carpetas y archivos de prueba generados originalmente por Android Studio.





Prompt de corrección 2: Corrige Dp y dp después de reunir componentes



Si Componentes.kt muestra “Unresolved reference: Dp”, revisa sus imports.



Deben coexistir:



import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp



Dp es el tipo utilizado por el parámetro tamaño.

dp es la extensión utilizada en valores como 56.dp.



No son el mismo identificador.



Si utilizas PowerShell para reunir imports, no uses una eliminación de duplicados que ignore mayúsculas y minúsculas. Sort-Object -Unique sin distinguir mayúsculas puede eliminar uno de estos dos imports.



Corrige los imports sin cambiar los tamaños, las fotografías ni los íconos.





Prompt de corrección 3: Corrige la imagen de portada inexistente



Si R.drawable.doctor\_splash aparece en rojo:

\- Comprueba que doctor\_splash.png exista en res/drawable.

\- Verifica que el archivo sea una imagen válida.

\- Si el archivo original se llama img.png, comprueba su contenido antes de renombrarlo.

\- Actualiza la referencia en SplashScreen.kt.

\- Conserva import com.saludplus.citas.R.



No sustituyas la ilustración por un ícono para ocultar el problema.



La navegación debe seguir llamando a SplashScreen, que muestra la portada ilustrada y conserva sus callbacks.





Prompt de corrección 4: Corrige fotografías o íconos que no aparecen



Si aparecen referencias R.drawable en rojo, compara el nombre del recurso con los archivos reales de drawable y drawable-nodpi.



No presupongas que el número del recurso coincide con el ID del médico. Revisa el mapa de FotoMedico.



Comprueba que:

\- MedicosScreen utiliza FotoMedico.

\- FechaHoraScreen utiliza FotoMedico.

\- ConfirmarCitaScreen utiliza FotoMedico.

\- HomeScreen utiliza IconoEspecialidad en las especialidades destacadas.

\- EspecialidadesScreen utiliza IconoEspecialidad en sus filas.



No dupliques las funciones ni las imágenes. Conserva sus llamadas y los onClick.





Prompt de corrección 5: Corrige errores de llaves del calendario



Si FechaHoraScreen.kt muestra “Syntax error: Expecting '}'”, revisa el archivo completo.



Comprueba el cierre de:

\- FechaHoraScreen.

\- Column.

\- LazyRow e items.

\- LazyVerticalGrid y gridItems.

\- Card y Box.

\- Bloques if/else.

\- Callbacks onClick.



Corrige las llaves sin eliminar el calendario dinámico, las flechas, la fotografía del médico, las validaciones ni Continuar.



No entregues un archivo truncado.





Prompt de corrección 6: Verifica java.time, íconos y configuración Android



Inspecciona app/build.gradle.kts y el catálogo de versiones antes de cambiar dependencias.



La aplicación conserva minSdk 24 y utiliza java.time. Comprueba que esté configurado core library desugaring para los dispositivos anteriores a API 26, aunque assembleDebug compile.



Si falta, configura isCoreLibraryDesugaringEnabled y una dependencia desugar\_jdk\_libs compatible con la versión de Android Gradle Plugin del proyecto.



Si faltan los íconos de Compose, comprueba material-icons-extended. No añadas la misma dependencia dos veces.



Conserva compileSdk 37 cuando las dependencias actuales lo requieren. Mantén targetSdk y minSdk existentes salvo que haya una razón concreta y documentada para cambiarlos.



No cambies versiones al azar ni actualices todas las bibliotecas para resolver un único error.





Prompt de corrección 7: Corrige el calendario que mantiene una selección anterior



Si al cambiar de día o semana queda marcada una hora anterior:



\- Reinicia horaSeleccionada.

\- Al cambiar de semana, reinicia también fechaSeleccionada.

\- Recalcula horariosDisponibles para el médico y la fecha actuales.

\- Comprueba que la fecha seleccionada pertenezca a la lista visible.

\- Comprueba nuevamente el horario al pulsar Continuar.



No permitas continuar con una hora guardada de otra fecha.





Prompt de corrección 8: Corrige fechas visibles sin cambiar los datos internos



Si ConfirmarCitaScreen muestra solamente “2026-09-16”, formatea el texto visible en español.



Conserva la fecha ISO para:

\- La ruta.

\- horariosDisponibles.

\- agendarCita.

\- El modelo Cita.



No guardes el texto “Miércoles 16 de setiembre 2026” como valor interno de fecha, porque rompería la comparación y el orden de las reservas.





Prompt de corrección 9: Corrige contenido cortado y desplazamiento



Revisa las pantallas en un emulador de tamaño medio y, si es posible, con un tamaño de letra mayor.



Comprueba:

\- Botones accesibles con teclado abierto.

\- Campos de registro que pueden recorrerse con scroll.

\- Imagen de portada que no oculta los botones.

\- Contenido que no queda bajo la barra inferior.

\- Mensajes de error visibles.

\- Horarios completos y botón Continuar accesible.



No coloques una LazyColumn o LazyVerticalGrid sin altura limitada dentro de otro desplazamiento vertical.



En FechaHoraScreen conserva LazyVerticalGrid con altura finita si está dentro de una Column desplazable.



No reemplaces la cuadrícula por filas manuales ni reduzcas el texto hasta volverlo ilegible.





Prompt de corrección 10: Conserva las pantallas que ya cumplen



Antes de modificar Inicio, Mis citas, Perfil, Cita exitosa, Confirmar cita o AppNavigation, verifica si ya cumplen su función.



No rehagas rutas, callbacks, popUpTo ni la barra inferior sin un problema concreto.



Conserva:

\- Los cuatro destinos de NavigationBar.

\- El saludo del usuario.

\- LazyRow de especialidades.

\- LazyColumn de especialidades, médicos y citas.

\- El mensaje de lista vacía.

\- La fotografía del médico.

\- La fecha en español.

\- El cierre de sesión.

\- Los controles de acceso a citas de otro paciente.



No borres las pantallas de retos extra. Si una está incompleta, documenta su estado sin presentarla como terminada.





Prompt 15: Comprueba la compilación y el funcionamiento



Ejecuta desde la carpeta del proyecto:



.\\gradlew.bat :app:assembleDebug



Si falla, corrige el error concreto y vuelve a ejecutar. No afirmes que compiló si no obtuviste BUILD SUCCESSFUL.



Si no tienes acceso al SDK o al emulador, indica claramente qué no pudiste verificar.



Cuando puedas ejecutar la aplicación, comprueba:



1\. Portada con ilustración y botones funcionales.

2\. Registro con datos válidos.

3\. Rechazo de campos vacíos y datos inválidos.

4\. Inicio de sesión con contraseña correcta e incorrecta.

5\. Búsqueda de especialidades.

6\. Médicos filtrados por especialidad.

7\. Cinco días hábiles desde hoy.

8\. Avance y retroceso semanal.

9\. Imposibilidad de retroceder antes de la posición inicial.

10\. Cambio correcto de mes y año al navegar.

11\. Reinicio de selección al cambiar de día o semana.

12\. Continuar desactivado sin fecha y hora válidas.

13\. Confirmación con fecha en español.

14\. Reserva visible en Mis citas.

15\. Horario reservado ausente para el mismo médico y fecha.

16\. Horarios de otro médico no bloqueados por esa reserva.

17\. Cierre de sesión.

18\. Fotos e íconos conservados.



Incluye una comprobación del calendario alrededor de un fin de semana y de un cambio de mes.



Distingue entre:

\- Comprobaciones realizadas.

\- Revisión de código.

\- Pruebas pendientes.



No presentes BUILD SUCCESSFUL como prueba de que no existen errores de funcionamiento.





Prompt 16: Actualiza README.md y PROMPTS.md con resultados reales



Actualiza README.md para describir la rama mejora-ia:



\- Autor: Ivan Jara Ayala.

\- Proyecto y paquete.

\- Cómo abrir y ejecutar.

\- Datos en memoria y ausencia de base de datos.

\- Diferencias respecto de main.

\- Ubicación de cada requisito de la rúbrica.

\- Calendario dinámico en FechaHoraScreen.kt.

\- Fecha en español en ConfirmarCitaScreen.kt.

\- LazyVerticalGrid de horarios.

\- Validaciones en Repositorio.kt.

\- Componentes compartidos en Componentes.kt.

\- Portada ilustrada en SplashScreen.kt.

\- Estado real de los retos extra.

\- Comprobaciones realizadas y limitaciones conocidas.



No describas las fechas fijas de main como si fueran el calendario de mejora-ia.



Documenta las intervenciones ejecutadas en PROMPTS.md con:

\- Prompt utilizado.

\- Respuesta resumida.

\- Qué se tuvo que corregir.

\- Resultado de la verificación, cuando exista.



Esta lista es una guía reconstruida para reproducir el trabajo. No la presentes como una transcripción literal del historial ni afirmes que una corrección condicional se ejecutó si no fue necesaria.



No inventes pruebas superadas, resultados de Gemini ni problemas que no ocurrieron.





Prompt 17: Revisa los cambios antes de registrar la entrega



Comprueba la rama y el estado del proyecto:



git branch --show-current

git status

git diff --stat



La rama de destino debe ser mejora-ia.



Presenta los archivos modificados y el motivo de cada cambio. Comprueba que las eliminaciones correspondan únicamente a archivos cuyo contenido ya se trasladó o cuya retirada fue solicitada.



Conserva las imágenes, los modelos, las pantallas y las funciones existentes.



La guía solicita un mínimo de tres commits descriptivos durante la fase de mejora. Si se está reproduciendo el desarrollo desde cero, distribuye los commits en avances reales, por ejemplo:

1\. Calendario dinámico y horarios.

2\. Diseño, fotografías e íconos.

3\. Correcciones y documentación.



Si la rama ya supera el mínimo, no añadas commits vacíos ni reconstruyas artificialmente el historial.



No hagas reset, no uses push --force y no mezcles ramas completas para copiar un solo archivo.



Antes del commit y push, muestra el resultado de la compilación y las comprobaciones realizadas.

