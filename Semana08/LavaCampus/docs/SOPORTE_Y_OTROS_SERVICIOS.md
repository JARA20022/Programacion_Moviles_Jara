# Implementación: Soporte y Otros Servicios

Esta documentación describe el recorrido de los cambios realizados para implementar el rol `SOPORTE` y el servicio especial `OTROS SERVICIOS` en el proyecto LavaCampus, manteniendo la arquitectura y el diseño originales.

## A. REGISTRO DE SOPORTE: DESDE EL BOTÓN HASTA LA LISTA

El proceso inicia en la pantalla `SoportesScreen`, cuando el administrador pulsa el botón "Registrar Soporte" para abrir el formulario y, después de llenarlo, pulsa "Guardar".

### 1. Dónde Inició
- **Archivo:** `app/src/main/java/com/jara/lavacampus/ui/administrador/SoportesScreen.kt`
- **Componente:** `FormularioSoporte`
- **Evento:** El usuario llena los datos y pulsa el botón `LavaButton` (texto: "Guardar"). Este evento llama a `foco.clearFocus()` y luego ejecuta la acción en el ViewModel pasándole los datos: `vm.registrarSoporte(...)`.

### 2. Dónde Se Valida
- **Archivo ViewModel:** `app/src/main/java/com/jara/lavacampus/viewmodel/SoportesViewModel.kt`
- **Función:** `registrarSoporte`
- El ViewModel usa `Validaciones` para verificar que el correo tenga formato válido, el código cumpla con ser 4-12 números (`errorCodigoSoporte`), el nombre y apellido sean letras y signos permitidos (`errorNombre`), y la contraseña cumpla los requisitos de letra/número (`errorClaveSoporte`). Además, verifica localmente que `clave == claveConfirmacion`.
- Tras validar, el ViewModel se conecta con el repositorio llamando a `repository.registrarSoporte(...)`.
- En el repositorio `LavaRepositoryRoom`, la misma validación se repite para proteger la integridad dentro de la transacción, además de comprobar duplicidad de código y correo mediante consultas a base de datos.

### 3. Con Quién Se Conecta
- El ViewModel interactúa con la interfaz `LavaRepository` (en `LavaRepository.kt`), específicamente con el método `registrarSoporte`.
- La implementación `LavaRepositoryRoom` inicia una transacción (`db.withTransaction`).
- En la transacción, primero obtiene el actor que realiza la acción y verifica que tenga rol `ADMIN`. Si no, se rechaza.
- Tras re-validar los datos y la ausencia de duplicidad (`usuarios.contarPorCorreo` y `usuarios.contarPorCodigo`), llama a `usuarios.insertar(...)`.

### 4. Dónde Se Guardó
- La entidad es `Usuario`, alojada en `app/src/main/java/com/jara/lavacampus/data/local/entity/Usuario.kt`.
- La tabla de Room asociada se llama `usuarios` dentro de la base de datos `lavacampus.db` local en el dispositivo (SQLite).
- Se insertan: `nombre`, `apellido`, `codigo`, `correo`, `clave` y se asigna obligatoriamente el rol `"SOPORTE"`.
- Ni `claveConfirmacion` ni el rol proporcionado en el formulario se guardan, y `DataStore` no almacena contraseñas ni la lista de usuarios.

### 5. Cómo Llega a la Lista
- En `UsuarioDao.kt`, existe el método `fun observarSoportes(): Flow<List<Usuario>>` que retorna un `Flow` con la consulta: `SELECT * FROM usuarios WHERE rol = 'SOPORTE' ORDER BY nombre`.
- En `SoportesViewModel.kt`, se declara `val soportes = repository.observarSoportes().stateIn(...)`. Esto observa la tabla y se actualiza reactivamente al cambiar.
- Compose detecta el cambio en `SoportesScreen.kt` al observar el estado con `collectAsStateWithLifecycle()` e itera la lista en un `LazyColumn` a través de la función `items(soportes)`.

### 6. Dónde Terminó
- La operación de persistencia termina al concretarse el commit de la transacción en `LavaRepositoryRoom.kt`.
- El flujo visual termina cuando, tras el éxito, el ViewModel notifica a la vista, el formulario desaparece y el `LazyColumn` en `SoportesScreen` se repinta mostrando la lista actualizada.

---

## B. LOGIN DEL SOPORTE

El login se gestiona desde `app/src/main/java/com/jara/lavacampus/ui/estudiante/LoginScreen.kt`.

- **Dónde se escriben las credenciales:** Cuando el usuario cambia el selector de rol (`FilterChip`) a `"SOPORTE"`, un campo adicional de `Confirmar Contraseña` se hace visible en el formulario.
- **Comprobación visual de claves:** Al presionar "Ingresar", el componente evalúa inmediatamente `if (rol == Rol.SOPORTE && clave != confirmacion)`, mostrando el error `"Las contraseñas no coinciden"` si no coinciden.
- **Dónde se autentica:** Se invoca `vm.login(correo, clave, rol)` en `SesionViewModel.kt`, el cual llama a `repository.login(...)`. 
- En el repositorio, la consulta `SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1` obtiene al usuario.
- En el `SesionViewModel`, se comprueba `usuario.rol != rolSeleccionado.name` (asegurando que sólo el rol SOPORTE entra si se escogió ese rol en el login).
- **Datos de sesión:** Se almacena la sesión llamando a `preferencias.guardarSesion(usuario.rol, usuario.id)` (utilizando Preferences DataStore), y no se guarda ninguna contraseña.
- **Pantalla final:** Al restaurarse o generarse el estado de sesión, la navegación descrita en `NavLava.kt` lleva al usuario con el rol `SOPORTE` hacia la ruta `Rutas.INICIO_SOPORTE`, visualizando la pantalla `SoporteInicioScreen.kt`.

---

## C. OTROS SERVICIOS Y LA ATENCIÓN DEL SOPORTE (Ampliación)

Como ampliación propuesta a los requerimientos iniciales (que pedían acceso al catálogo), se le otorgó al rol de SOPORTE la capacidad para atender requerimientos especiales de "OTROS SERVICIOS" sin requerir ser un Administrador general.

### 1. Dónde inicia la solicitud del estudiante
- **Archivo:** `app/src/main/java/com/jara/lavacampus/ui/estudiante/NuevoPedidoScreen.kt`
- **Evento:** Cuando el estudiante selecciona "OTROS SERVICIOS" (y la cantidad es mayor a 0), aparece de manera reactiva un campo de texto adicional "Ej. Lavado de zapatillas". 
- Se invoca `vm.cambiarDescripcionOtros(...)` en `NuevoPedidoViewModel`.

### 2. Dónde se guarda su descripción
- **Entidad / Tabla:** `detalles_pedido` (entidad `DetallePedido`).
- **Nuevas Columnas:** Mediante `MIGRACION_3_4` y `MIGRACION_4_5` en `LavaDatabase.kt`, se añadieron las columnas: `descripcionOtros`, `estadoAtencion`, `respuestaSoporte`, `soporteId` y `fechaRespuesta`.
- **Flujo de grabado:** Al pulsar en confirmar, `NuevoPedidoViewModel` recoge la `descripcionOtros` ingresada y valida el texto mediante `Validaciones.errorDescripcionOtros` (exigiendo de 5 a 200 caracteres, permitiendo números y signos, no solo letras). Si es válido, se empuja como `LineaPedido` hacia `LavaRepositoryRoom`, insertando finalmente mediante `detalles.insertarTodos`.

### 3. Cómo llega a la lista de soporte
- **Consultas DAO:** Se creó en `DetalleDao.kt` la consulta `observarSolicitudesSoporte()` que hace un `INNER JOIN` entre `detalles_pedido`, `pedidos` y `usuarios`, filtrando `WHERE d.descripcionOtros IS NOT NULL`. 
- **Flujo de Pantalla:** La ruta del `NavLava.kt` carga `SolicitudesSoporteScreen.kt`. Este Composable recolecta `vm.solicitudes` de `SolicitudesSoporteViewModel` el cual mantiene en tiempo real los requerimientos del estudiante usando `Flow`.

### 4. Dónde inicia la respuesta del soporte
- **Pantalla:** Dentro de `RespuestaSolicitudScreen.kt` (que se abre al presionar "Ver / Atender" sobre un elemento de la lista).
- **Entrada:** El Soporte cambia el estado (por ej. a `"ATENDIDA"`) e ingresa un mensaje en "Respuesta del soporte".
- **Evento:** Se presiona "Guardar", lo que invoca `vm.responder(detalleId, estadoSeleccionado, respuesta, ...)` del `SolicitudesSoporteViewModel`.

### 5. Qué funciones y clases conecta la respuesta
- El ViewModel envía la petición al repositorio `LavaRepositoryRoom.responderSolicitud`.
- El repositorio efectúa la misma regla de validación llamando a `Validaciones.errorRespuestaSoporte` y asegurando que el actor en sesión sea `SOPORTE` (para evitar accesos no autorizados por otras cuentas).
- El repositorio finalmente llama al método `@Query UPDATE` `responderSolicitud` dentro de `DetalleDao.kt`.

### 6. En qué tabla y columnas se guarda la respuesta
- **Tabla:** `detalles_pedido`.
- **Columnas actualizadas:** `estadoAtencion`, `respuestaSoporte`, `soporteId` (ID del actor SOPORTE actual) y `fechaRespuesta` (marca de tiempo actual). 
- Toda esta data está ligada estrictamente al `id` particular del detalle del pedido (no se sobrescribe ni se pisa con otras solicitudes de otros pedidos).

### 7. Cómo llega al detalle que ve el estudiante y el administrador
- Como las clases previas de `DetallePedidoScreen.kt` (Estudiante) y `GestionPedidoScreen.kt` (Admin) están construidas para escuchar recursivamente los detalles mediante `Flow`, automáticamente recogen los nuevos campos al haberse expandido `DetallePedido`.
- Se introdujo un bloque condicional en ambos archivos:
  ```kotlin
  linea.detalle.descripcionOtros?.let { desc -> ... }
  ```
- Este bloque inmediatamente muestra en pantalla el estado de la atención (ej. PENDIENTE, ATENDIDA), y si fue atendida, despliega la `respuestaSoporte`.

### 8. Dónde termina el recorrido
- La escritura de la atención termina a nivel interno cuando se completa el UPDATE en Room (`DetalleDao`).
- Visualmente, el proceso concluye cuando el propio Soporte regresa a su bandeja de solicitudes, y a su vez, el estudiante observa la respuesta dentro del historial interactivo de "Mis Pedidos".

---

### Tabla Resumen de Cambios

| Archivo | Clase o Composable | Función | Responsabilidad | Se conecta con |
|---------|---------------------|---------|-----------------|-----------------|
| `SoportesScreen.kt` | `FormularioSoporte` | `LavaButton(onClick=...)` | Renderizar campos y lanzar petición de registro | `SoportesViewModel.registrarSoporte` |
| `SoportesViewModel.kt` | `SoportesViewModel` | `registrarSoporte` | Validar datos antes de enviarlos | `LavaRepository.registrarSoporte` |
| `LavaRepositoryRoom.kt` | `LavaRepositoryRoom` | `registrarSoporte` | Verificar permisos de administrador, duplicidad, iniciar la transacción de guardado | `UsuarioDao.insertar`, `Validaciones` |
| `LavaDatabase.kt` | `MIGRACION_2_3` | `migrate` | Modificar la base de datos (agregar apellido, inyectar "OTROS SERVICIOS") | SQLite (`db.execSQL`) |
| `Validaciones.kt` | `Validaciones` | `errorCodigoSoporte`, `errorClaveSoporte` | Centralizar reglas de negocio como formato y longitud | N/A |
| `LoginScreen.kt` | `LoginScreen` | `LavaButton(onClick=...)` | Validar la confirmación y hacer petición de acceso | `SesionViewModel.login` |
| `SoporteInicioScreen.kt`| `SoporteInicioScreen` | `LazyColumn` (items) | Listar el catálogo de servicios de forma informativa para el soporte | `ServiciosViewModel.servicios` |

### Ejemplo Práctico de Registro

Supongamos que creamos el usuario:
- Código: `000123`
- Nombre: `José`
- Apellido: `Pérez`
- Correo: `jose.soporte@example.com`

**Recorrido de este registro:**
1. El administrador entra a la pantalla de Ajustes, pulsa el botón "Usuarios de soporte" e ingresa a `SoportesScreen`.
2. Presiona "Registrar Soporte" e introduce los datos mencionados, añadiendo una contraseña con letras y números que pasa la validación y confirmación local.
3. El ViewModel llama a `repository.registrarSoporte(...)`.
4. El repositorio consulta `usuarios.contarPorCorreo("jose.soporte@example.com")` y `usuarios.contarPorCodigo("000123")` devolviendo 0 (sin duplicados).
5. Se normalizan los espacios y tildes (`"José"`, `"Pérez"`). El repositorio ejecuta `usuarios.insertar(Usuario(..., rol="SOPORTE"))`. 
6. El DAO inserta en la base de datos `lavacampus.db` el nuevo `Usuario`.
7. Room notifica que la tabla `usuarios` ha cambiado, enviando el nuevo estado por Flow a través de `observarSoportes`. Compose redibuja la lista en `SoportesScreen` de manera automática para mostrar el nombre `"José Pérez"`.

> _El proceso de escritura termina en la confirmación de la transacción de Room, y el resultado visual termina en la aparición de una nueva tarjeta en la lista de soportes._
