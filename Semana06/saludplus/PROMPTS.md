Trabaja sobre mi proyecto Android Studio «Clínica SaludPlus», paquete com.saludplus.citas, rama mejora-ia. Usa la imagen adjunta del GLAB como referencia visual para las siete pantallas: Splash, Registro, Inicio, Especialidades, Médicos, Fecha y hora y Confirmar cita. Quiero que ajustes el diseño de las clases existentes con Jetpack Compose. Conserva la navegación, los callbacks, las validaciones y Repositorio; no empieces otro proyecto, no agregues base de datos, XML, ViewModel ni dependencias innecesarias.

MEDIDAS Y ESTILO COMÚN
Todas las medidas siguientes son dp (unidades de pantalla Android), no píxeles ni dpi. Usa fondo #F8FBFF o blanco, azul principal #2864E8, azul oscuro #142C68, texto principal #1F2937, texto secundario #687587 y bordes claros #E6EBF2. Los botones principales deben tener fondo azul, texto blanco, altura de 52 dp, esquinas de 12 dp y ocupar el ancho disponible. Usa 20 dp de margen horizontal en las pantallas interiores; separación de 8 a 12 dp entre elementos relacionados y de 20 a 28 dp entre secciones. Las tarjetas deben tener esquinas de 12 dp, fondo blanco o pastel según la referencia y una elevación discreta de 1 a 2 dp. Los títulos de pantalla deben medir aproximadamente 20 sp y ser seminegrita o negrita; títulos de sección 16 a 18 sp; texto normal 13 a 14 sp; descripciones 11 a 12 sp. Los íconos habituales deben ocupar 20 a 24 dp. No uses tarjetas grises oscuras como las que aparecían en las capturas anteriores.

PANTALLA 1 — SPLASH
Muestra arriba el símbolo médico azul con corazón blanco que ya está implementado, centrado y de unos 80 dp. Debajo, «Clínica» y «SaludPlus» centrados, en dos líneas de aproximadamente 30 sp, azul oscuro y en negrita; luego «Tu salud, nuestra prioridad» en 13 sp gris. La ilustración doctor_splash.png debe ocupar el espacio central disponible, mantenerse completa sin deformarse y no tapar los botones. Cerca de la parte inferior coloca «Comenzar» en un botón azul de 52 dp de alto y debajo «Ya tengo una cuenta» como enlace azul. Mantén los callbacks hacia Registro y Login.

PANTALLA 2 — REGISTRO
Encabezado «Crear cuenta» centrado o alineado como aparece en la referencia, con una breve indicación debajo. Conserva Nombre completo, Teléfono, Correo electrónico, Contraseña y Confirmar contraseña porque este proyecto ya valida esos datos. Cada campo debe ocupar el ancho disponible, medir aproximadamente 52 dp de alto, tener esquinas de 10 a 12 dp, borde #E6EBF2 y un ícono azul a la izquierda: persona, teléfono, correo y candado. Deja 10 dp entre campos y 22 dp antes del botón azul «Registrarme». Mantén «Ver términos y condiciones» y «Ya tengo una cuenta». No elimines validaciones ni cambies registrarUsuario.

PANTALLA 3 — INICIO
Mantén el saludo «¡Hola, [primer nombre]!» en 20 a 22 sp, debajo «¿Qué deseas hacer hoy?» en gris y la campana de notificaciones a la derecha. Distribuye cuatro accesos en dos columnas y dos filas con 10 a 12 dp de separación. Cada tarjeta debe medir aproximadamente 110 a 116 dp de alto y tener un ícono de 28 a 30 dp sobre el texto: Agendar cita en azul pastel #E6F1FF con calendario azul; Mis citas en verde pastel #E4F8EE con calendario verde; Mis datos en lila pastel #F1E8FF con persona morada; Resultados en naranja pastel #FFF0DB con documento naranja. Debajo deja 24 dp y muestra «Especialidades destacadas» y un LazyRow de tarjetas de unos 130 × 108 dp. Cada especialidad debe tener su ícono propio y nombre legible, no la misma cruz para todas. Conserva el NavigationBar inferior de Inicio, Citas, Resultados y Perfil y todos los onClick existentes.

PANTALLA 4 — ESPECIALIDADES
Encabezado con flecha de regreso de 24 dp y título «Especialidades». Debajo, buscador de ancho completo y unos 48 a 52 dp de alto, con lupa, fondo blanco y borde fino claro. Después muestra las especialidades en LazyColumn con 8 a 10 dp entre filas. Cada fila debe medir aproximadamente 64 a 72 dp, fondo blanco, esquinas de 12 dp y 12 dp de padding. A la izquierda usa IconoEspecialidad dentro de un círculo pastel de 40 a 42 dp: Medicina general azul con símbolo médico, Pediatría naranja con símbolo infantil, Cardiología rojo con corazón, Dermatología naranja claro con símbolo de piel y Traumatología azul con símbolo de atención a huesos. A la derecha deja una flecha pequeña. En el centro conserva nombre en 14 sp seminegrita y descripción en 11 a 12 sp gris. El buscador debe seguir filtrando en tiempo real y tocar la fila debe abrir los médicos de esa especialidad. Utiliza las especialidades reales de Repositorio, sin inventar otras para llenar la imagen.

PANTALLA 5 — MÉDICOS
Encabezado con flecha y «Médicos de [especialidad]». Muestra las tarjetas en LazyColumn con 10 a 12 dp de separación. Cada tarjeta debe tener fondo blanco, esquinas de 12 dp, padding de 12 dp y retrato circular de 52 a 56 dp a la izquierda usando FotoMedico(medico.id, medico.nombre), nunca la silueta azul genérica. A la derecha muestra nombre seminegrita, especialidad en gris, estrella y calificación en naranja #EB9A24, años de experiencia y precio. No sustituyas los médicos de Repositorio por los nombres de la figura; conserva los datos reales y la acción de seleccionar al médico. La foto de un médico debe ser la misma en esta pantalla y en las dos siguientes.

PANTALLA 6 — FECHA Y HORA
Encabezado con flecha y «Seleccionar fecha y hora». Debajo muestra tarjeta clara del médico con retrato circular de 48 a 56 dp, nombre y especialidad. Separa 20 dp antes del calendario. En la cabecera del calendario coloca flecha izquierda, mes y año centrados, y flecha derecha; cada flecha debe tener zona táctil suficiente, cercana a 48 dp. Muestra cinco días hábiles en tarjetas de unos 60 × 74 dp con 8 a 10 dp entre ellas; la fecha elegida debe tener fondo azul #2864E8 y texto blanco, las demás fondo #F3F6FB y texto oscuro. Debajo coloca «Horarios disponibles» y una cuadrícula de tres columnas con 10 dp de separación; cada horario mide aproximadamente 50 a 52 dp de alto. El horario seleccionado también debe quedar azul con texto blanco. El botón «Continuar» debe estar al final y habilitarse solo con día y hora seleccionados. Conserva LocalDate, las semanas navegables, los cinco días hábiles, el reinicio de la hora al cambiar de día y el bloqueo de horarios reservados.

PANTALLA 7 — CONFIRMAR CITA
Encabezado con flecha y «Confirmar cita». Muestra arriba la foto circular del médico de 48 a 56 dp, nombre y especialidad. Debajo, una tarjeta blanca con filas para Fecha, Hora, Tipo de atención o datos existentes del proyecto, y Precio; cada fila lleva un ícono azul de unos 20 dp, etiqueta gris y valor oscuro, con separación vertical de 12 a 16 dp. Presenta la fecha en español de forma legible, por ejemplo «Martes 16 de setiembre 2026», sin modificar la fecha que recibe Repositorio.agendarCita. El botón «Agendar cita» debe medir 52 dp de alto y conservar la comprobación de sesión y horario ocupado.

REVISION FINAL
Revisa los archivos actuales antes de editar. Usa las imágenes ya presentes en drawable y drawable-nodpi; no dupliques retratos ni dejes referencias R.drawable inexistentes. Conserva la visualización en teléfonos de tamaño medio: usa scroll donde haga falta para que el teclado o una pantalla pequeña no corte los botones. No cambies las firmas de los composables ni las rutas de AppNavigation. Al terminar, indica exactamente qué archivos modificaste y compila con :app:assembleDebug. Si una parte ya coincide con estas medidas y funciona, déjala como está.

PROMPTS DE CORRECCIÓN — APLICAR SOBRE EL MISMO PROYECTO

Corrección 1 — Imagen de la portada:
Si la ilustración aparece en rojo en Android Studio o no se muestra al ejecutar, verifica que el archivo esté en app/src/main/res/drawable/doctor_splash.png y que SplashVisual use R.drawable.doctor_splash. Corrige el nombre o la referencia; no sustituyas la ilustración por un ícono.

Corrección 2 — Error de sintaxis del calendario:
Si FechaHoraScreen.kt muestra «Syntax error: Expecting '}'», revisa el archivo completo, especialmente el cierre de LazyVerticalGrid, los bloques if/else, Column y FechaHoraScreen. Corrige las llaves sin eliminar los cinco días hábiles, las flechas, la selección de horarios ni el botón Continuar.

Corrección 3 — LocalDate e íconos:
Si la compilación falla por java.time en Android con minSdk 24, revisa en app/build.gradle.kts isCoreLibraryDesugaringEnabled y la dependencia desugar_jdk_libs. Si los imports de androidx.compose.material.icons aparecen en rojo, comprueba material-icons-extended. Conserva compileSdk 37 si ya está configurado y compila; no cambies versiones sin un error que lo requiera.

Corrección 4 — Fotos inexistentes o equivocadas:
Si R.drawable.medico_1 a R.drawable.medico_6 aparecen en rojo, comprueba los nombres exactos de los PNG en app/src/main/res/drawable-nodpi. Revisa el when de FotoMedico y los identificadores de Repositorio.medicos para que el retrato corresponda al nombre mostrado. Una misma persona debe conservar su foto en MedicosScreen, FechaHoraScreen y ConfirmarCitaScreen. No copies otra vez las imágenes ni crees componentes duplicados.

Corrección 5 — Cruz repetida y silueta:
Si todas las especialidades siguen mostrando la misma cruz azul, reemplaza la llamada al ícono fijo por IconoEspecialidad(nombre = especialidad.nombre) tanto en EspecialidadesScreen como en las especialidades destacadas de HomeScreen. Si la lista de médicos sigue mostrando una silueta, utiliza FotoMedico(medicoId = medico.id, nombre = medico.nombre). Conserva los onClick y las listas existentes.

Corrección 6 — Fecha en español:
Si ConfirmarCitaScreen muestra una fecha como 2026-09-16, formatea solo el texto visible con Locale de español de Perú para que se lea «Miércoles 16 de setiembre 2026». No cambies el String original en formato ISO que recibe Repositorio.agendarCita ni los parámetros de navegación.

Corrección 7 — Selección anterior que permanece marcada:
Al cambiar de fecha o avanzar o retroceder una semana, establece horaSeleccionada en null. Consulta nuevamente Repositorio.horariosDisponibles(medicoId, fechaSeleccionada). No permitas que una hora elegida para otro día permanezca marcada ni que aparezca una hora ya reservada.

Corrección 8 — Contenido cortado:
Prueba las siete pantallas en un emulador de tamaño medio. Si el teclado, la barra inferior o la altura de la pantalla ocultan campos o botones, ajusta el desplazamiento vertical y los espacios flexibles. Conserva los márgenes horizontales de 20 dp y el botón principal de 52 dp; no reduzcas el texto hasta hacerlo ilegible.

Después de cada corrección, indica el archivo modificado y el motivo. Al terminar ejecuta :app:assembleDebug. Si una corrección ya está aplicada y funciona, repórtalo sin volver a modificar ese archivo.