# Prompts y decisiones de mejora-ia · LavaCampus

## Contexto y procedencia

Proyecto `Semana08/LavaCampus`, paquete `com.jara.lavacampus`. La fase base utiliza Kotlin, Jetpack Compose, Material 3, Room, DataStore y MVVM + Repository. La fase 2 añade atrasados, descuento por volumen, repetición y el diseño Aurora.

**La asistencia original de esta práctica se realizó en ChatGPT.** Los cinco bloques siguientes son versiones técnicas ampliadas, organizadas desde el diagnóstico hasta el rediseño, para reproducir el trabajo con un agente dentro de Android Studio. **No son una transcripción literal de cinco mensajes enviados a Gemini ni una exportación de una sesión de Android Studio.** Más abajo se conservan extractos de las solicitudes reales de cambio.

Cada prompt presenta el contexto que debe recibir el agente, el trabajo solicitado, el resultado que consta en el código, las correcciones y la relación con MVVM. Una instrucción de comprobación no equivale a una prueba ya aprobada.

## Prompt 1 · Revisar la base y preparar la fase de mejoras

**Objetivo:** empezar la fase 2 desde la aplicación base existente, conocer el proyecto y conservar sus contratos.

**Texto ampliado para el agente de Android Studio:**

```text
Actúa como un desarrollador Android senior especializado en Kotlin, Jetpack
Compose, Material 3, Room, DataStore y MVVM. Trabaja sobre el proyecto existente
LavaCampus, dentro de Semana08/LavaCampus. No generes una aplicación distinta.

Antes de editar:
1. Lee las instrucciones locales del repositorio, README, la guía del laboratorio
   y los archivos Gradle, libs.versions.toml y gradle-wrapper.properties.
2. Revisa el estado de Git, el último commit y la rama activa. main representa la
   fase base y mejora-ia debe partir de ella. Si mejora-ia ya existe, comprueba
   su relación con main y qué trabajo contiene; no la reutilices a ciegas ni
   descartes historial para crear la nueva fase.
3. Inventaría entidades, DAOs, relaciones, Repository, preferencias, ViewModels,
   rutas, pantallas y componentes. Conserva com.jara.lavacampus.
4. Comprueba cómo se inicializan lavacampus.db y los datos de práctica. No borres
   datos ni ejecutes una migración destructiva para ocultar un problema.

Vamos a implementar tres mejoras del PDF: pedidos atrasados, descuento por
volumen y repetir un pedido. Haz primero un mapa de los archivos que cambiará
cada mejora y de las reglas que ya existen. Conserva los dos perfiles locales,
las cuentas ficticias de práctica y la comunicación reactiva de Room.

Reglas obligatorias de arquitectura:
- La View observa estado y envía acciones; no usa DAO, Repository ni SQL.
- Los ViewModels usan la interfaz LavaRepository, no LavaRepositoryRoom.
- Las funciones nuevas se declaran en la interfaz y se implementan después.
- Lecturas reactivas: Flow en DAO/Repository y StateFlow en ViewModel.
- Escrituras y consultas puntuales: suspend. Las operaciones relacionadas se
  agrupan en una transacción.
- No agregues XML de interfaz, un backend ni dependencias innecesarias.
- Si cambias entidades, define versión y migración antes de darlo por listo.

Preserva las validaciones existentes: nombres Unicode, correo, precios con dos
decimales, cantidades por unidad, máximo tres pedidos activos, fecha que evita
el domingo, casillero libre, cobro antes de entregar y restricciones al cancelar.
Reutiliza Validaciones.kt: no dupliques las reglas en cada Composable.

No rebajes AGP, Gradle, Kotlin, KSP ni Room solo para copiar las versiones de un
ejemplo de la guía. Usa el wrapper y comprueba la compatibilidad real del proyecto.

Entrega el diagnóstico, el plan de archivos y los casos de prueba. Identifica
qué observaciones requieren comprobación en un emulador. Implementaremos cada
mejora en su propio commit y cerraremos con PROMPTS.md y README. No marques
pruebas manuales ni capturas como completadas por haber compilado.
```

**Relación con la conversación:** desarrolla la solicitud de hacer la fase `mejora-ia` en un script, con las validaciones, sin alterar la aplicación base.

**Qué generó la IA:** separación por mejoras, conservación de la estructura existente, reutilización de validaciones y propuesta de commits A, B, C y documentación.

**Qué hubo que corregir:** la automatización inicial reutilizó una rama que tenía historial anterior. Después se reconstruyó la fase desde `main`, se reaplicaron los commits de LavaCampus y se conservó la rama anterior en un respaldo. Esta corrección está registrada en `docs/REPARACION_GIT.md`; no se presenta el flujo inicial como si siempre hubiera sido correcto.

**¿Respeta MVVM?:** sí como criterio de trabajo: conserva el Repository como contrato y separa estado, persistencia y presentación. Esta planificación no justifica consultas desde una pantalla.

**Comprobación:** revisar `git status`, la relación de ramas, el mapa de archivos y la compilación de la base antes de añadir comportamiento.

## Prompt 2 · Identificar pedidos atrasados y actualizar el panel

**Objetivo:** mostrar atrasos reales sin añadir consultas a los Composables.

**Texto ampliado para el agente de Android Studio:**

```text
En la rama mejora-ia de LavaCampus implementa Pedidos atrasados conservando el
contrato MVVM definido en el diagnóstico. Revisa primero PedidoDao,
LavaRepository, LavaRepositoryRoom, BandejaViewModel, PanelViewModel y sus vistas.

Un pedido está atrasado si fechaEntrega es anterior a la fecha local de hoy y
su estado no es ENTREGADO ni CANCELADO. Las fechas se guardan como ISO yyyy-MM-dd.
Un pedido de hoy todavía no está atrasado. No cambies su estado para señalarlo.

Capa de datos:
- Añade en PedidoDao una consulta parametrizada que devuelva los identificadores
  como Flow<List<Int>> y excluya explícitamente los dos estados finales.
- Expón la función en LavaRepository y delega en LavaRepositoryRoom.
- Esta mejora por sí sola no cambia entidades ni exige otra tabla.

Capa de estado:
- Usa la fecha local del dispositivo, coherente con las fechas de los pedidos.
- Permite actualizar el criterio si la pantalla permanece abierta al cambiar de
  día. Un flujo que compruebe la fecha cada minuto mientras se observa basta;
  evita un servicio permanente o un bucle de recomposición de la UI.
- BandejaViewModel debe combinar lista, búsqueda, filtro e identificadores.
- Añade el filtro Atrasados manteniendo Todos, Recibido, En proceso y Listo.
- PanelViewModel expone la cantidad de atrasados como StateFlow<Int>.
- Conserva los estados de carga y error y la cancelación de las corrutinas.

Presentación:
- Destaca los pedidos con texto de alerta y una superficie de error legible.
- No dependas solo del color: añade la palabra Atrasado y el motivo de atención.
- Usa errorContainer/onErrorContainer o los tokens equivalentes del tema.
- El panel muestra el contador y permite abrir la bandeja con ese filtro.
- La View no calcula la consulta SQL ni accede a Room.

Comprueba: ayer + RECIBIDO sí; hoy no; mañana no; ENTREGADO y CANCELADO no.
Al finalizar un pedido atrasado debe desaparecer del filtro y bajar el contador
sin reiniciar la aplicación. La búsqueda debe seguir funcionando junto al filtro.

Entrega los archivos modificados y una explicación breve del recorrido del dato.
Ejecuta las pruebas pertinentes y assembleDebug; registra por separado lo que
quede por probar en un emulador. Commit: ia: agrega pedidos atrasados en la bandeja.
```

**Qué generó la IA y consta en el código:** `RelojCampus`, consulta de identificadores atrasados, contrato del Repository, filtro y marca en bandeja, contador en panel y navegación al filtro.

**Qué se corrigió o precisó:** excluir también `CANCELADO`, usar comparación estricta anterior a hoy, conservar la búsqueda combinada y evitar una alerta basada únicamente en un color.

**¿Respeta MVVM?:** sí. El DAO consulta; el Repository expone; los ViewModels combinan y transforman; las pantallas muestran el resultado y envían eventos.

**Comprobación pendiente de dispositivo:** observar el cambio en tiempo real al finalizar un pedido y comprobar visualmente el filtro. Los resultados se registran en `docs/PRUEBAS_IA.md`.

## Prompt 3 · Aplicar descuento por volumen y migrar Room sin perder datos

**Objetivo:** calcular y persistir correctamente el ahorro, incluidos pedidos con servicios mezclados.

**Texto ampliado para el agente de Android Studio:**

```text
Implementa la mejora Descuento por volumen de LavaCampus. Revisa las entidades
Pedido y DetallePedido, LavaDatabase, la creación transaccional en el Repository,
NuevoPedidoViewModel y los resúmenes del estudiante y del administrador.

Regla exacta:
- Suma únicamente las cantidades de servicios cuya unidad sea KG.
- Si el total supera 5.0 kg, descuenta el 10 % de las líneas cobradas por KG.
- Exactamente 5.0 kg no activa el descuento.
- PRENDA y UNIDAD no reciben ese descuento ni se suman como kilos.
- Usa BigDecimal y HALF_UP a dos decimales para los importes por línea; después
  suma los resultados. No realices redondeos monetarios con una cadena visual.

Crea una regla pura reutilizable, por ejemplo PreciosCampus en PrecioPedido.kt,
que reciba servicios vigentes y cantidades y entregue subtotal bruto, descuento,
total neto y los importes de cada línea. Valida que los servicios existan, estén
activos y tengan tarifas/cantidades válidas. Reutiliza Validaciones.kt.

Persistencia:
- Guarda en Pedido el subtotal bruto, descuento y total neto.
- Conserva precioUnitario como la tarifa vigente al confirmar cada detalle.
- Guarda el descuento del detalle separado de su subtotal neto.
- Haz que los agregados de ingresos por servicio y el total cobrado sean coherentes.
- Vuelve a consultar servicios y recalcular los importes en el Repository dentro
  de la misma transacción que inserta pedido y detalles. No aceptes un descuento
  definitivo enviado por la View ni confíes solo en el resumen mostrado.

La base existente de main es v1. Para esta ampliación utiliza versión 2 y una
migración explícita 1 a 2:
1. Agregar descuento a pedidos con DEFAULT 0.
2. Agregar subtotal a pedidos con DEFAULT 0.
3. Rellenar subtotal con el total anterior.
4. Agregar descuento a detalles_pedido con DEFAULT 0.
Registra la migración en el builder de Room. Los pedidos antiguos mantienen
sus totales y descuento cero. No recalcules el pasado y no borres tablas.
Si estos cambios ya existen, verifícalos: no agregues las columnas dos veces.

Interfaz:
- El resumen cambia al variar las cantidades.
- Muestra líneas elegidas, subtotal, ahorro, total y fecha estimada.
- El descuento debe ser visible también al consultar el pedido guardado.
- Mantén la confirmación deshabilitada con el pedido vacío o mientras se guarda.

Ejemplos obligatorios:
- 3.5 kg a S/8 + 5 prendas a S/2.50: total S/40.50, ahorro S/0.
- 5 kg a S/8: total S/40, sin descuento.
- 5.5 kg a S/8: subtotal S/44, descuento S/4.40, total S/39.60.
- 6 kg a S/8 + 5 prendas a S/2.50: subtotal S/60.50,
  descuento S/4.80 y total S/55.70.

Añade pruebas de límites, servicios mixtos, redondeo y valores inválidos. Añade
una prueba de migración con una base temporal que compruebe datos anteriores,
esquema y claves foráneas. No uses la base real del usuario como base de prueba.
Commit: ia: agrega descuento por volumen.
```

**Qué generó la IA y consta en el código:** `PrecioPedido.kt`, campos persistidos de descuento/subtotal, `MIGRACION_1_2`, base v2, cálculo transaccional, resúmenes y pruebas de precios/migración.

**Qué se corrigió o precisó:** el descuento se limita a KG; se guarda por detalle para conservar ingresos netos coherentes; los pedidos previos se migran sin aplicarles un descuento retroactivo.

**¿Respeta MVVM?:** sí. La regla monetaria no está en un Composable. El ViewModel prepara el estado, el Repository recalcula y persiste y Room aplica la migración.

**Comprobación:** pruebas unitarias de `PreciosCampusTest`, compilación del APK de pruebas y ejecución de `MigracionCampusTest` en un dispositivo. Compilar la prueba instrumentada no equivale a ejecutarla.

## Prompt 4 · Repetir desde el historial y conservar todas las validaciones

**Objetivo:** agilizar una nueva solicitud sin reutilizar datos históricos como si fueran vigentes.

**Texto ampliado para el agente de Android Studio:**

```text
Añade Pedir de nuevo a Mis pedidos de LavaCampus. Usa el historial real de Room,
los ViewModels y la interfaz LavaRepository. Revisa las validaciones existentes
antes de escribir otras distintas para esta operación.

El botón aparece en pedidos propios ENTREGADOS o CANCELADOS. Al tocarlo se abre
Nuevo pedido con un identificador de origen y un borrador editable. No debe
insertarse un pedido automáticamente ni modificarse el original.

Preparación:
- Comprueba otra vez el propietario y el estado del pedido al recuperarlo.
- Agrupa sus líneas por servicio y recupera las cantidades.
- Consulta el catálogo actual. Utiliza la tarifa actual del servicio, no
  precioUnitario del detalle antiguo.
- Omite servicios eliminados, inactivos, de tarifa inválida o con cantidades
  incompatibles con la unidad actual. Informa cuántos se omitieron.
- Si no queda ningún servicio válido, muestra un borrador vacío y Confirmar
  deshabilitado; no cierres la app ni guardes un pedido sin detalles.
- Recupera la nota solo si cumple los límites y controles de texto.

Estado y navegación:
- NuevoPedidoViewModel recibe un pedidoOrigenId opcional a través de su factory.
- Mientras carga el origen, bloquea las acciones que puedan confirmar un resumen
  incompleto. Muestra carga, aviso y error de forma diferenciada.
- Usa una clave de ViewModel que distinga usuario y origen; no mezcles borradores
  de personas o pedidos distintos.
- Reutiliza el flujo normal de confirmar: precios vigentes, fecha, transacción,
  recepción habilitada y máximo tres pedidos activos.
- Si la tarifa cambió mientras se preparaba la solicitud, actualiza o rechaza
  coherentemente para que el usuario revise antes de confirmar.

Conserva las reglas de entrada de la base:
- Nombre: letras Unicode, tildes, separadores permitidos, mínimo tres letras,
  máximo 60 caracteres y duplicados comparados de forma normalizada.
- Precio: S/0.01 a S/9999.99, máximo dos decimales y punto/coma como separador.
  Rechaza NaN, infinito, exponentes, negativos y texto arbitrario.
- Nota/descripción: máximo 300 caracteres y sin controles invisibles inválidos.
- Correo válido y límites de credenciales; no rompas las claves ficticias de práctica.
- KG de 1 a 10 en pasos de 0.5; prenda/unidad enteras de 1 a 30.
- Servicios activos, líneas válidas, cantidades finitas y ausencia de duplicados.
- Al reforzar la cancelación del estudiante, consulta el estado RECIBIDO y escribe
  CANCELADO en la misma transacción para evitar cambios entre ambos pasos.

Extrae la preparación del borrador a una regla pura como RepetirPedido.preparar.
Añade pruebas de otro propietario, pedido no histórico, cambio de precio,
servicio inactivo y cambio de unidad. Revisa doble pulsación, Atrás y que nunca
se guarde hasta confirmar. La View no consulta el pedido ni calcula su tarifa.
Commit: ia: agrega repetir un pedido.
```

**Qué generó la IA y consta en el código:** regla `RepetirPedido`, origen opcional en la ruta y factory, carga de borrador en `NuevoPedidoViewModel`, botón en historial y pruebas de preparación.

**Qué se corrigió o precisó:** verificar propiedad y estado, omitir datos incompatibles con aviso, separar carga de repetición y guardado y mantener las validaciones heredadas de `main`. Estas validaciones no se atribuyen todas a una creación nueva de la fase 2.

**¿Respeta MVVM?:** sí. La pantalla solicita repetir; el ViewModel recupera el pedido por el contrato y usa la regla pura; el guardado sigue en el Repository.

**Comprobación pendiente de dispositivo:** cambiar una tarifa, repetir un pedido antiguo y verificar que el borrador y el nuevo pedido usan la tarifa vigente sin alterar el historial.

## Prompt 5 · Rediseñar Aurora con colores, texturas, animaciones y contraste

**Objetivo:** obtener una composición visual distinta de la versión base y corregir los problemas señalados al revisarla.

**Texto ampliado para el agente de Android Studio:**

```text
Rediseña las pantallas de mejora-ia de LavaCampus con una identidad profesional
llamada Aurora. Mantén intactas las operaciones, reglas, datos y MVVM. No te
limites a cambiar el color de las tarjetas de main: cambia composición,
jerarquía, accesos y navegación. Revisa primero Theme.kt, Type.kt, los componentes,
NavLava, MainActivity y todas las pantallas de ambos perfiles.

PALETA OSCURA
Fondo #101018; texto de fondo #F5F0FF.
Superficie #20202E y texto #F5F0FF.
Superficie de apoyo #2A293C y texto secundario #C0B9CE.
Primario #C4AEFF y texto sobre primario #27124F.
Contenedor primario #392B59 y texto #EBE0FF.
Secundario coral #FFB49F y texto #4B2319.
Contenedor secundario #4F302A y texto #FFDFD5.
Terciario #8FDCC8 y texto #00382F.
Contornos #91869F y #40394F.
Error #FFA8B8 y texto #5C1427.
Contenedor de error #482631 y texto #FFDDE5.

PALETA CLARA
Fondo #F4F1FA; texto #211B30.
Superficie #FFFBFF; texto #211B30.
Superficie de apoyo #EAE4F3; texto secundario #625A70.
Primario #6841C6; texto #FFFFFF.
Contenedor primario #E7DEFF; texto #2B1757.
Secundario #9C462E; texto #FFFFFF.
Contenedor secundario #FFDAD0; texto #451B13.
Terciario #006A60; texto #FFFFFF.
Contornos #81768E y #DBD2E9.
Error #AA2C3D; texto #FFFFFF.
Contenedor de error #FFDADF; texto #5F1424.

MARCA Y COMPOSICIÓN
- Colores de apoyo: #171324, #7046D2, #6452C6, #E0D5FF y #FFB49F.
- Login: marca superior, motivo orbital decorativo, titular grande y formulario
  independiente en superficie redondeada. Conserva selector de rol y credenciales.
- Inicio: saludo, acción Nuevo pedido, seguimiento actual, accesos en dos columnas
  y actividad reciente. Mantén el acceso a cuenta, historial y tarifas.
- Panel: ingresos como bloque protagonista, cifras integradas, alerta de atrasos,
  cuadrícula de acciones y selector para los gráficos. La tarjeta de ingresos
  utiliza un degradado #E3D9FF a #C1A4FC con texto #281843.
- Nuevo pedido: tarjetas de servicios con cantidad y estado seleccionado; resumen
  desglosado y barra inferior de total/confirmación. Conserva notas y errores.
- Usa esquinas redondeadas de 10, 16, 24, 30 y 36 dp según el componente.
- Usa espaciado de aproximadamente 20-24 dp en pantallas y 12-18 dp entre bloques;
  evita que los encabezados o botones queden apretados.

TIPOGRAFÍA
Sans serif. Importe protagonista 42/46 sp ExtraBold, tracking -1.5 sp.
Título principal 34/39 sp Bold; sección 28/34 o 24/30 sp.
Títulos de tarjeta 21/28 y 16/23 sp; cuerpo 16/24 y 14/21 sp;
auxiliar 12/18 sp. Respeta el escalado de fuente y permite desplazamiento.

TEXTURA Y MOVIMIENTO
- Dibuja grano de baja opacidad y motivos orbitales con Canvas; no generes una
  textura aleatoria nueva en cada frame. Usa drawWithCache donde corresponda.
- Entrada de bloques: fundido y desplazamiento vertical de unos 420 ms con
  retraso progresivo limitado a 260 ms.
- Motivo orbital: animación inicial de unos 900 ms, sin giro infinito permanente.
- Transiciones: fundido de 260 ms y desplazamiento de 280 ms al entrar;
  salida de 160 ms; navegación atrás de unos 220 ms.
- Botones: escala de pulsación aproximada 0.97 con resorte, sin duplicar acciones.
- Usa APIs estándar de Compose y respeta la duración de animación del sistema.

REVISIÓN OBLIGATORIA
La guía pide cuatro destinos de estudiante: Inicio, Pedidos, Tarifas y Perfil;
y cinco de administrador: Panel, Pedidos, Servicios, Clientes y Ajustes.
Oculta la barra en formularios cuando sea necesario para proteger la operación.
Mantén las rutas, el regreso y la limpieza de pila al cambiar de perfil.

Corrige el problema observado en el panel: títulos como Tu operación o Todo
bajo control no deben quedar negros sobre un fondo oscuro. Comprueba el color
heredado de contenido en los Scaffold transparentes. Usa onBackground para
texto sobre fondo y los pares onSurface/onPrimaryContainer adecuados dentro
de superficies. No fuerces todos los textos a blanco: el tema claro también
necesita contraste. El título concreto Tu operación es personalizable; lo que
exige el PDF es el panel y su información.

Verifica ambos roles y temas, teclado, pantallas pequeñas, texto grande, errores,
confirmación y acceso a Clientes. No declares terminada la revisión visual por
compilar. Conserva las pruebas y migración de Room y muestra los archivos que
cambiaste. Actualiza README y PROMPTS.md con resultados reales y las correcciones.
```

**Qué generó la IA y consta en el código:** paletas clara/oscura, tipografía sans serif, `Studio.kt`, grano y motivo orbital en Canvas, nuevas composiciones de login/inicio/panel, módulos en cuadrícula, barra de confirmación, transiciones y respuesta de pulsación.

**Qué hubo que cambiar:** la primera propuesta, Estudio Textil, conservaba demasiada semejanza con la versión base. Se sustituyó por Aurora con violeta/coral y otra composición.

**Correcciones posteriores incorporadas al código:** se completaron los cinco destinos administrativos, la herencia de color mediante `onBackground`, `AppTopBar` y `SplashScreen` como componentes efectivos y la cancelación atómica del estudiante. **Pendiente de evidencia manual:** revisar la presentación final en ambos temas y registrar las capturas; no se da por aprobada esa revisión solo porque el código compile.

**¿Respeta MVVM?:** sí como separación de presentación. El rediseño no necesita trasladar SQL, Repository ni reglas monetarias a las pantallas; se reutilizan los ViewModels y estados existentes.

**Comprobación:** comparar pantallas reales de main y mejora-ia, verificar contraste en ambos temas y registrar capturas propias de Aurora. Las capturas existentes de la fase 1 no sustituyen estas evidencias.

## Solicitudes reales de cambio y corrección

Extractos de mensajes de esta conversación. Se mantienen como solicitudes del usuario a ChatGPT; no se atribuyen a un historial de Gemini.

| Momento | Extracto real | Decisión o cambio |
|---|---|---|
| Validaciones de la base | «ahora aplicale validaciones como en tal campo esp uro texto puro numeros» | Reforzar entradas y reutilizar las validaciones al ampliar la aplicación. |
| Inicio de la fase IA | «dame todo de una nomas para hacerlo de mejora ia de un solo script con todas las vaciones, y un diseño diferente, animaciones ,entradas, diseños raros, texturizados pero diferentes profesional ahora si damelo» | Organizar tres mejoras, rediseño, migración y comprobaciones en un flujo ejecutable por terminal. |
| Separación de fases | «pero esto es para la rama main del pdf aun no hicimos nada de mejora-ia» | Separar la base de las mejoras y evitar atribuir a la fase IA lo realizado antes. |
| Cambio de propuesta visual | «no le veo diferentecia al de main tedije algo moderno» | Sustituir Estudio Textil por Aurora, cambiando composición y estilo. |
| Origen de la rama | «debe ser exacto al pdf» | Corregir la reutilización de una rama previa y hacer que la entrega de mejora-ia parta de main, conservando respaldo. |
| Contraste y revisión completa | «verifica eso que dice en negro opaco tu operacion si pide en el pdf y ni se ve» | Revisar color heredado, navegación requerida, estructura y contratos del laboratorio. |
| Documentación detallada | «en prompts explicas todos los prompts desde 0 como mejorar como si fuera el agente de android studio, con colores detallados» | Elaborar estos cinco prompts ampliados, conservar la procedencia y documentar resultados/pedidos pendientes. |

## Correcciones técnicas que guiaron el trabajo

1. **Origen de mejora-ia:** reconstruir la rama desde la base y conservar el historial anterior en respaldo; no ocultar el procedimiento previo ni inventar fechas de desarrollo.
2. **Descuento consistente:** aplicarlo a KG y persistirlo por línea, manteniendo la coherencia entre pedido, detalle e ingresos.
3. **Datos históricos:** conservar los importes anteriores mediante migración explícita; no borrar la base para resolver un cambio de entidad.
4. **Repetición segura:** comprobar propietario, estado y catálogo actual; cargar un borrador y exigir confirmación.
5. **Cancelación atómica:** la revisión S08 integra la revalidación del estado y la escritura en una misma transacción. La regla se conserva en el ViewModel, usando el contrato del Repository.
6. **Diseño distinto:** cambiar la composición completa en lugar de limitarse a recolorear componentes de la base.
7. **Contraste y navegación:** la revisión de código corrige el color heredado y completa los destinos; la presentación final debe comprobarse en el dispositivo.
8. **Evidencia real:** separar código compilado, pruebas ejecutadas, revisiones manuales y capturas pendientes.

## Reflexión técnica de cinco líneas

Estas líneas resumen decisiones técnicas observables del trabajo; pueden adaptarse después con la experiencia personal de las pruebas.

1. La IA ayudó a ampliar la aplicación sin reemplazar el contrato del Repository ni duplicar toda la base.
2. La primera propuesta visual se parecía demasiado a main y fue necesario pedir un cambio de composición, no solo de colores.
3. Room requirió una migración explícita para conservar pedidos e importes históricos al agregar el descuento.
4. MVVM permitió mantener consultas, reglas de cálculo y presentación en sus capas mientras se añadían las mejoras.
5. La compilación y las pruebas automáticas aportan evidencia técnica, pero el contraste, el teclado y la experiencia de uso necesitan revisión real en el emulador.

## Registro de verificación

- Comandos de compilación y pruebas en [README.md](README.md).
- Casos funcionales y resultados reales en [docs/PRUEBAS_IA.md](docs/PRUEBAS_IA.md).
- Detalle de implementación en [docs/MEJORAS_IA.md](docs/MEJORAS_IA.md).
- Corrección de procedencia de ramas en [docs/REPARACION_GIT.md](docs/REPARACION_GIT.md).
- No se cambian resultados a APROBADA ni se crean capturas ficticias al actualizar este documento.
