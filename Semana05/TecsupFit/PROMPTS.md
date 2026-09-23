# PROMPTS — TECSUP Fit

## Asistencia utilizada

Se utilizó ChatGPT para preparar la base, la separación de archivos y esta mejora. Los nombres main y mejora-ia distinguen las fases, no implican que la base se desarrollara sin asistencia. Este registro describe las solicitudes de la conversación y las decisiones de implementación; no atribuye ejecuciones a Gemini.

## Solicitudes relevantes

### 1. Opción B y ocho avances de base

Solicitud resumida: preparar TECSUP Fit según el Word, conservar la clínica y organizar la base en ocho commits completos para avanzar con menos intercambios. Se prepararon avances en MainActivity y después su separación, manteniendo remember/mutableStateOf y sin ViewModel ni MVVM.

### 2. Separación completa de archivos

Mensaje del usuario: «listo con esos mainactivity que me diste de Tecsupfit doy por terminado no? ahora separamos las clases de forma independiente, hagamoslo completos dame los codigos completos de cada clase ya no por partes, no me da tiempo, terminemos de una vez, separado funcional, completo y commit diciendo separados y completos».

Respuesta preparada: modelos individuales, datos, navegación, componentes, colores y seis pantallas, con MainActivity reducido al punto de entrada. Se conservaron las declaraciones del avance 7 y se actualizó README.

### 3. Mejora con IA

Mensaje del usuario: «listo ahora sigamos con la mejora-ia rapido nomas hagamoslo dame ya completo no me da la hora».

Mejora seleccionada por el asistente: cancelar reservas con AlertDialog, siguiendo el ejemplo del Word. La cancelación conserva la reserva y libera los cupos de Cross Training mediante el cálculo existente de ocupación.

## Archivos preparados para la mejora

| Archivo | Cambio |
|---|---|
| navigation/AppNavigation.kt | Callback de cancelación con validación del estado y actualización de la lista mediante map/copy. |
| screens/ReservasScreen.kt | Botón solo para Confirmadas, selección del ID y diálogo con datos de la reserva. |
| ui/components/ComponentesFit.kt | Etiqueta roja para Cancelada, conservando los estilos de Confirmada y Completada. |
| README.md | Funcionamiento de la mejora y pasos de comprobación. |
| PROMPTS.md | Registro de solicitudes, decisiones y ajustes. |

## Decisiones y ajustes

- La reserva no se elimina. Solo se cambia su estado a Cancelada.
- Abrir o cerrar el diálogo no cancela; el callback se ejecuta únicamente al confirmar.
- Tanto la interfaz como el callback comprueban que el estado sea Confirmada.
- Se conserva el cálculo de disponibilidad que suma solo reservas Confirmadas. No se añade un contador separado que pueda liberar dos veces los mismos cupos.
- El diálogo guarda el ID y consulta el registro actual, evitando operar sobre una copia antigua de la reserva.
- El perfil conserva sus estadísticas históricas: una cancelación no representa una asistencia ni altera la racha.
- La rama mejora-ia ya existe en el monorepositorio. La propuesta copia solo la carpeta TecsupFit desde main; no recrea ni fuerza la rama ni mezcla los archivos de la clínica.
- No se añadieron dependencias ni se cambiaron los modelos, el catálogo o las demás pantallas.

## Tres avances de esta fase

1. Incorporar la base de TECSUP Fit desde main a la rama mejora-ia existente, sin modificar otros laboratorios.
2. Implementar cancelación con confirmación y representación visual de Cancelada.
3. Documentar el funcionamiento, las comprobaciones y la asistencia recibida.

Son instrucciones de trabajo. Los hashes y fechas efectivos se consultan en el historial de Git; aquí no se inventan commits ejecutados ni resultados de compilación.

## Prompt reutilizable

Este texto se redactó para reproducir la mejora; no es una transcripción de un prompt ya ejecutado en otro asistente:

> Trabaja en Semana05/TecsupFit, paquete com.jara.tecsupfit, sobre la rama mejora-ia existente. Inspecciona los archivos antes de modificar. Conserva la aplicación base y los otros laboratorios del repositorio. Implementa cancelar reservas con AlertDialog. Muestra Cancelar reserva solo para Confirmadas. El diálogo debe mostrar nombre de la clase, día, hora, sala y cantidad de cupos, con Sí, cancelar y Conservar reserva. Cerrar el diálogo por cualquier vía debe conservar la reserva. Confirmar debe cambiar únicamente esa reserva a Cancelada, mantenerla en la lista y mostrar etiqueta roja. Valida también su estado en el callback. Conserva el cálculo de cupos basado en la suma de Confirmadas, para que Cross Training recupere exactamente los cupos cancelados sin superar sus ocho cupos iniciales. No modifiques las estadísticas históricas del perfil. Usa remember/mutableStateOf, el NavController existente, Scaffold y padding; no añadas ViewModel, MVVM, persistencia ni dependencias. Entrega AppNavigation.kt, ReservasScreen.kt y ComponentesFit.kt completos, y actualiza README y PROMPTS.md. Distingue revisión estática de pruebas efectivamente ejecutadas.

## Verificación

Se revisan las firmas del callback, las rutas, los tres estados visuales y la sintaxis Kotlin. La compilación y la prueba con dos reservas, conservación y cancelación quedan para Android Studio. Los pasos exactos están en README y no se afirman como superados antes de ejecutarlos.
