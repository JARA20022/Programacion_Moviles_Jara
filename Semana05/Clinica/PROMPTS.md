# Registro de asistencia con IA — Clínica Salud+

## Alcance

Los archivos de esta propuesta se prepararon con ayuda de ChatGPT. La mejora elegida es cancelar una cita con un AlertDialog de confirmación, ejemplo expresamente mencionado en el Word actualizado. Esta mejora se aplica en la rama mejora-ia, creada desde main.

La fase base también recibió asistencia durante esta conversación. Los nombres de las ramas distinguen las etapas de la entrega; no se afirma que main se haya desarrollado sin ayuda.

## Solicitudes usadas en la conversación

Este registro resume las instrucciones relevantes; no presenta como mensajes enviados los prompts reutilizables escritos después.

### 1. Seguir el documento actualizado y trabajar por fases

**Solicitud resumida:** desarrollar únicamente el caso de la clínica, basarse en los laboratorios anteriores del ZIP, respetar el diseño y los requerimientos del Word, trabajar por partes con commits y push, y atender las dos ramas solicitadas.

**Restricciones reiteradas:** no usar ViewModel ni MVVM; administrar el estado con remember/mutableStateOf; entregar archivos completos y comentados para la sustentación.

**Aplicación:** la mejora comparte los modelos, el estado y los componentes de la base; no introduce una base de datos, dependencias adicionales ni otra arquitectura.

### 2. Redactar los commits en primera persona

**Mensaje del usuario:** «Sigamos y los commits de aqui para adelante que sean como si yo lo hiciera, Implemente, hice.. algo asi., sigamos».

**Aplicación:** los mensajes propuestos describen cambios concretos con verbos como Implementé, Agregué y Documenté. La redacción en primera persona no sustituye este registro de asistencia.

### 3. Terminar las fases pendientes

**Mensaje del usuario:** «ya a que acabarlo ya porque ya tenemos 8 commits, ademas elimino model y data donde creamos DatosClinica?».

**Respuesta aplicada:** conservar model y data, terminar la extracción de la navegación y preparar el cierre de ambas fases. El asistente seleccionó la cancelación con diálogo a partir del ejemplo del Word y la dividió en dos avances de código y uno de documentación.

## Ajustes realizados en la propuesta

| Punto | Ajuste |
|---|---|
| Navegación mezclada con la actividad | Se trasladó NavegacionClinica a navigation/AppNavigation.kt, conservando un único NavController y el estado existente. |
| Cancelación directa del primer avance | El segundo avance agregó AlertDialog; abrirlo ya no cambia el estado de la cita. |
| Posible cancelación de una cita completada | El botón solo aparece para Confirmada y el callback también comprueba ese estado. |
| Borrado de información al cancelar | La cita se conserva y se cambia su estado mediante copy; no se elimina de la lista. |
| Colores preparados solo para dos estados | TarjetaCita distingue Confirmada en verde, Completada en gris y Cancelada en rojo. |
| Salir del diálogo | onDismissRequest y Conservar cita cierran el diálogo sin modificar la lista. |
| Verificación | Se comprobaron estáticamente las firmas y referencias de los archivos. La compilación y las pruebas de uso deben realizarse en Android Studio; no se inventan resultados de ejecución. |

## Prompt reutilizable para reproducir la mejora

El siguiente texto se preparó para reproducir la mejora en otro asistente. No es una afirmación de que se haya ejecutado en Gemini:

> Trabaja sobre la rama mejora-ia de mi aplicación Clínica Salud+, creada desde main. Inspecciona primero los archivos existentes en Semana05/Clinica. Usa el paquete com.jara.clinicasalud y conserva la organización actual: model, data, screens, ui/components, ui/theme y navigation. Implementa la mejora de cancelación propuesta en el Word actualizado. En Mis citas, muestra Cancelar cita únicamente para citas Confirmadas. Antes de modificar el estado, abre un AlertDialog con el médico, la fecha y la hora, y las opciones Sí, cancelar y Conservar cita. Confirmar debe cambiar únicamente esa cita a Cancelada, manteniéndola en la lista con una etiqueta roja. Cerrar el diálogo por cualquier vía debe conservar la reserva. Las citas Completadas y Canceladas no deben permitir cancelación. Mantén todo el estado con remember y mutableStateOf; no uses ViewModel, MVVM, base de datos ni nuevas dependencias. Reutiliza el Scaffold, el padding, los colores y los componentes existentes. Entrega los archivos modificados completos, con comentarios explicativos. Divide el trabajo en tres commits descriptivos: primero la cancelación y su estado visual, después el diálogo de confirmación, y finalmente README y PROMPTS.md. No ejecutes commits vacíos ni alteres las fechas del historial. No declares pruebas superadas si no se ejecutaron.

## Qué explicar en la sustentación

- citaPendienteId identifica la reserva sobre la que se abre el diálogo.
- Abrir el diálogo solo cambia esa selección local; todavía no cancela.
- onCancelar comunica la acción a NavegacionClinica, donde reside la lista compartida.
- map y copy actualizan una cita manteniendo las demás.
- El cambio de la lista observada provoca la actualización de Mis citas.
- Cancelada es el estado adicional de la mejora; la base main mantiene los estados exigidos Confirmada y Completada.

## Incidencias reales al registrar y publicar los cambios

Las salidas de Git compartidas durante la conversación muestran:

- `7db09c8`: cancelación de citas confirmadas.
- `01f9dd5`: registro de MainActivity y ClinicaColors que habían quedado pendientes.
- `d107e3c`: AlertDialog para confirmar la cancelación.
- El intento de añadir README falló porque el archivo aún no existía con ese nombre en la carpeta Clinica.
- El push de mejora-ia fue rechazado porque origin/mejora-ia ya contenía tres commits de Semana03/lab03-2. La corrección propuesta integra ese historial sin forzar el push.
- La inspección de main mostró archivos incompletos de navegación, componentes y colores. La reparación utiliza las versiones completas de la fase base y conserva por separado la mejora de cancelación.

Estos hechos describen el historial proporcionado, no resultados de pruebas de ejecución. El README y este registro se incorporan después del commit del diálogo.

## Corrección posterior: Perfil en el menú lateral

**Mensaje del usuario:** «que paso con el perfil? te dije que sigas la documentacion, ahora nos falta eso, eso era de la rama main y supongo que mejorada en la rama mejora-ia esta en el word».

**Hallazgo:** la figura 2 incluye Perfil como cuarto destino. La implementación anterior solo incluía los tres destinos mínimos indicados en el texto. El asistente reconoció la omisión y corrigió también la documentación que justificaba excluirlo.

**Corrección preparada:** añadir Perfil al drawer y la ruta `perfil_paciente` al mismo NavHost, manteniendo `perfil/{medicoId}` para el médico. La pantalla del paciente utiliza Scaffold, topBar, botón de menú, innerPadding y colores existentes. Muestra únicamente JP, Juan Pérez y Paciente. El Word no proporciona una maqueta interior del perfil del paciente en la opción A, por lo que esta disposición es una adaptación, no una reproducción de una pantalla inexistente en el documento. No se incorporan DNI, teléfono, estadísticas, edición ni datos médicos nuevos.

**Aplicación por ramas:** la corrección se prepara para main y mejora-ia. En mejora-ia se conservan el callback de cancelación, el AlertDialog y el estado Cancelada. El perfil no necesita otra mejora independiente; la mejora funcional elegida continúa siendo cancelar con confirmación.

**Prompt reutilizable para esta corrección (redactado después de la solicitud):**

> Revisa la figura 2 de la opción A del Word actualizado. Completa el drawer con Inicio, Mis citas, Historial médico y Perfil. Usa el NavController y NavHost existentes y la ruta perfil_paciente, distinta de perfil/{medicoId}. Crea PerfilPacienteScreen.kt con Scaffold, TopAppBar, botón de menú y aplicación de innerPadding. Muestra únicamente los datos del paciente visibles en el documento: JP, Juan Pérez y Paciente. Conserva la paleta existente. No inventes campos, estadísticas ni edición. Aplica la corrección a la versión base main y a mejora-ia conservando la cancelación con AlertDialog en esta última. Entrega los archivos completos y actualiza los README y PROMPTS.md. No uses ViewModel, MVVM ni nuevas dependencias. No declares compilación o pruebas superadas si no se han ejecutado.

**Validación pendiente:** compilar y ejecutar ambas ramas en Android Studio; abrir Perfil, comprobar su selección en el drawer y regresar a Inicio. En mejora-ia, comprobar además que las citas se conservan al navegar y que la cancelación con diálogo sigue funcionando. La preparación de estos archivos no equivale a ejecutar estas pruebas.
