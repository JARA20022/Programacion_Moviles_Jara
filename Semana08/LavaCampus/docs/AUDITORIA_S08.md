# Revisión de LavaCampus contra la guía S08

Revisión del 28 de octubre de 2026. Base examinada: main 63806c27dcfb5b028348747cb54723261da89447 y mejora-ia 1ff266f864a2c4c056fbdc7b52b309ce4a419517. Las comprobaciones de código no sustituyen las pruebas manuales del emulador.

## Resultado y correcciones

La implementación principal cubre los contratos funcionales de los pasos 1-13. Se detectó que faltaba NavigationBar en main (página 8); Aurora solo tenía cuatro destinos administrativos; AppTopBar.kt y SplashScreen.kt eran archivos vacíos, aunque el splash estaba implementado dentro de NavLava; y las cancelaciones del estudiante no envolvían lectura/validación/escritura en una transacción. La revisión completa esos puntos.

La captura aportada mostraba títulos negros sobre fondo oscuro en Aurora. Los Scaffold transparentes heredaban un color de contenido que no correspondía al fondo. Se define explícitamente onBackground en el contenedor y en navegación. Se conserva la elección claro/oscuro. «Tu operación» es un título personalizado: el PDF exige el panel, no esa frase concreta.

## Matriz de requisitos de main

| Referencia | Contrato | Evidencia de código | Resultado |
|---|---|---|---|
| pp. 4, 11-12 | Cuatro entidades, PK automáticas, FK e índices | data/local/entity/*.kt; índice usuarioId/estado y cascade pedido-detalles | Presente |
| pp. 12-13 | DAO suspend para acciones y Flow para lecturas | dao/UsuarioDao, PedidoDao, DetalleDao, ServicioDao | Presente |
| p. 13 | Relaciones anidadas con Embedded y Relation | relation/PedidoConDetalle, DetalleConServicio | Presente |
| pp. 13-14 | Singleton Room v1 y lavacampus.db | LavaDatabase.obtener, cuatro entidades y cuatro DAO | Presente |
| p. 5 y p. 13 | Sembrar una vez, cuatro servicios, tres clientes, un administrador y diez pedidos | DatosIniciales.sembrar con withTransaction y contar usuarios | Presente |
| p. 14 | Application registrada y carga IO | AndroidManifest.xml y LavaApp.esperarDatosIniciales | Presente |
| p. 14 | Guardar pedido y todos sus detalles atómicamente | LavaRepositoryRoom.crearPedido, db.withTransaction | Presente |
| p. 4 | Congelar precio y subtotal del detalle | DetallePedido.precioUnitario/subtotal; detalle usa importes persistidos | Presente |
| pp. 14-15 | Contrato Repository y observaciones Flow | LavaRepository y delegaciones de LavaRepositoryRoom | Presente |
| p. 15 | DataStore ajustes, sesión, tema, avisos, recibir pedidos | PreferenciasStore, una extensión preferencesDataStore | Presente |
| pp. 15-16 | Login con rol, errores, restauración y rutas por perfil | SesionViewModel y LoginScreen | Presente |
| p. 17 | Pedido activo más reciente y dos recientes propios | InicioViewModel: filtra activos y take(2) sobre id DESC | Presente |
| p. 18 | KG 1..10, paso 0.5; otras unidades 1..30 enteros | NuevoPedidoViewModel y Validaciones.cantidadValida | Presente |
| p. 18 | Total, resumen y fecha máxima; domingo al lunes | NuevoPedidoViewModel.recalcular y Validaciones.fechaEntrega | Presente |
| pp. 5, 18 | Límite de tres pedidos activos | ViewModel y revalidación dentro de la transacción Room | Presente |
| pp. 19-20 | Detalle, seguimiento vertical, pago y casillero | DetallePedidoScreen y StepperEstado | Presente |
| pp. 19-20 | Cancelación de estudiante solo RECIBIDO y con diálogo | DetallePedidoViewModel/Screen y MisPedidos; ahora atómica | Corregido |
| p. 21 | Activos/historial, FAB, cancelar y casillero | MisPedidosViewModel/Screen y PedidoCard | Presente |
| p. 21 | Tarifas activas, Canvas e información de atención | TarifasScreen, IconoServicio y Servicio.textoPrecio | Presente |
| p. 22 | Contadores, ingresos desde lunes, dona/barras y alerta de listos | PanelViewModel, PanelScreen y componentes/graficos | Presente |
| pp. 23-24 | Bandeja con búsqueda, chips y acciones por estado | BandejaViewModel combina lista/filtro/búsqueda | Presente |
| p. 24 | Un paso de estado, casillero libre C-01..30 y pago antes de entregar | GestionPedidoViewModel con enTransaccion | Presente |
| p. 24 | Cancelación administrativa solo RECIBIDO o EN_PROCESO | GestionPedidoViewModel.cancelar | Presente |
| p. 25 | CRUD de servicios y prohibir eliminar los usados | ServiciosViewModel y comprobación transaccional del Repository | Presente |
| p. 25 | Precio positivo, unidad, 24/48/72 h, descripción, icono y activo | FormularioServicioScreen y Validaciones | Presente |
| p. 25 | Clientes con búsqueda, iniciales, pedidos no cancelados y gasto pagado | ClientesScreen y JOIN/agregados de UsuarioDao | Presente |
| p. 25 | Perfil, activos/totales/kg entregados, avisos y tema | PerfilScreen e InicioViewModel.resumenPerfil | Presente |
| p. 25 | Ajustes, recibir pedidos, cerrar sesión y cambiar perfil | AjustesScreen, SesionViewModel y preferencias | Presente |
| p. 25 | Limpiar pila al cambiar rol y recordar preferencias | NavLava.popUpTo y restauración de sesión | Presente |
| p. 8 | NavigationBar con 4 destinos estudiante y 5 administrador | NavLava y rutas de cada rol | Corregido |
| pp. 6-7 | Archivos y paquetes solicitados | Se conserva estructura bajo com.jara.lavacampus | Presente; namespace personalizado |
| pp. 6-7 | AppTopBar y SplashScreen reutilizables | Antes vacíos; ahora implementados y usados | Corregido |
| pp. 7-8 | Tema legible y modo oscuro persistente | MaterialTheme y DataStore; onBackground explícito | Corregido el contraste de Aurora |
| p. 27 | README con nombre, descripción, árbol y MVVM | README.md | Presente |
| p. 27 | Seis capturas, tres de cada rol | docs/capturas y enlaces del README | Existen; actualizar tras la barra nueva |
| p. 27 | Doce pruebas de aceptación | docs/aceptacion.json y PRUEBAS.md | Registradas aprobadas para f805043; repetir en la versión actualizada |
| p. 27 | Respuestas Para pensar | README responde preguntas explícitas de pp. 4 y 15 | Presente; no se inventaron preguntas |
| pp. 11-28 | Commits por hitos | Historial de main cubre datos, preferencias, pantallas y documentación | Mensajes equivalentes, no todos literales |

## Decisiones expresas del usuario

El diseño es distinto del mockup por solicitud del usuario. Por ello no se declara cumplimiento literal de colores y composición de pp. 7-8. Sí se mantienen funciones, pantallas, controles y cantidad de destinos. El paquete com.jara.lavacampus y las versiones actuales de herramientas se conservan por las indicaciones dadas durante la práctica; no se rebajan versiones para imitar ejemplos del PDF.

## Comprobaciones de esta actualización

La actualización debe compilar main y mejora-ia con testDebugUnitTest, assembleDebug y assembleDebugAndroidTest antes de publicar. El historial se amplía con commits normales: una corrección base en main y su integración en mejora-ia. main sigue siendo ancestro de mejora-ia; no se copian las tres mejoras ni Aurora hacia main y no se reescriben los commits ya hechos.

La inspección SQL contrasta las consultas reales de los DAO para contar activos, excluir cancelados, sumar ingresos, agrupar por servicio y contar clientes. La interfaz no llama a DAO/Repository; los ViewModels usan el contrato. Las cancelaciones del estudiante ahora validan el pedido dentro de repo.enTransaccion.

## Pendiente en el dispositivo

Repetir los doce casos de p. 27 sobre la actualización, comprobar barras 4/5, texto con tema claro y oscuro, cancelación desde ambas pantallas, teclado y cambio de perfil. Volver a capturar las seis imágenes de main con su interfaz actual. Para probar la APK main (Room v1) después de mejora-ia (Room v2), utilizar otro emulador sin datos v2: no borrar la base existente para simular una migración inversa que no se implementó.

Ni compilar ni registrar un estado en JSON certifica por sí solo las pruebas manuales. Esta revisión no cambia resultados a APROBADA automáticamente.
