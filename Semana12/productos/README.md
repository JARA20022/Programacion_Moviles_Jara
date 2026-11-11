# Productos

Aplicación Android del Laboratorio 11–12 de Programación en Móviles. Permite consultar un catálogo de smartphones, abrir el detalle de cada producto y guardar favoritos en el dispositivo. El ingreso usa Firebase Authentication y el rol se lee en Firestore.

- **Tema:** Productos.
- **Variante:** smartphones.
- **Paquete Android:** `com.tecsup.productos`.
- **API:** DummyJSON, con `GET /products/category/smartphones` para el catálogo y `GET /products/{id}` para el detalle.

## Funciones y requisitos

| Requisito | Comportamiento |
|---|---|
| R01 · Ingreso | Inicia sesión con correo y contraseña. Valida campos vacíos y muestra un mensaje si las credenciales fallan. |
| R02 · Roles | Lee el campo `rol` del documento `usuarios/{uid}` en Firestore. Los roles son `usuario` y `admin`. |
| R03 · Catálogo | Descarga los smartphones con Retrofit. La lista muestra carga, error con **Reintentar** o un mensaje cuando está vacía. |
| R04 · Detalle | Al tocar un producto, consulta el endpoint por ID y muestra su nombre, marca, precio y descripción. |
| R05 · MVVM | Las pantallas Compose observan un `StateFlow<UiState<T>>`. Los ViewModel reciben interfaces de repositorio mediante las fábricas de `AppContainer`. |
| R06 · Room | Guarda el catálogo en SQLite mediante `ItemEntity`, `ItemDao` y `AppDatabase`. La lista observa los datos de Room. |
| R07 · Favoritos | Permite marcar y quitar productos. La pantalla **Mis favoritos** lee Room y las marcas se conservan al reabrir la app y al sincronizar. |
| R08 · Sin conexión | Si falla la descarga y Room tiene datos, muestra el catálogo guardado con un aviso. Con la caché vacía muestra el error y **Reintentar**. |
| R09 · Notificación local | Al completar una sincronización, avisa cuántos elementos se guardaron. Pide permiso en Android 13 o superior y abre la app al tocar el aviso. |
| R10 · Panel Admin | Disponible para el rol `admin`. Muestra el total guardado y el total de favoritos. **Sincronizar** actualiza Room y **Limpiar caché** vacía ambos totales. |
| R11 · Sesión | Recupera la sesión al reabrir la app. **Cerrar sesión** vuelve al formulario de ingreso. |

Ambos roles pueden ingresar, ver el catálogo y el detalle, administrar favoritos y cerrar sesión. El rol `admin` tiene acceso adicional al Panel Admin.

## Mejoras de la fase 2

- Tema oscuro en el inicio de sesión, el catálogo, el detalle, los favoritos y el panel Admin.
- Fotos de productos de DummyJSON cargadas con Coil 2.7.0. Si una foto no está disponible, se muestra una ilustración.
- Búsqueda por modelo o marca, con un mensaje cuando no hay coincidencias.
- Transiciones entre pantallas y animaciones al marcar favoritos o al cambiar los totales del panel Admin.
- Opción **Reintentar** cuando falla una carga. Si falla la conexión y hay datos en Room, el catálogo muestra los productos guardados.

El requisito R12 se comprueba con las pruebas unitarias. El registro de consultas a IA para el requisito R13 corresponde a `PROMPTS.md`.

## Requisitos técnicos

| Componente | Configuración |
|---|---|
| Entorno | Android Studio con Android SDK 37 instalado. |
| Android | Mínimo API 24. Compilación API 37 y objetivo API 36. |
| Compilación | Gradle Wrapper 9.4.1, Android Gradle Plugin 9.2.1 y JDK 21 para el daemon de Gradle. |
| Kotlin y UI | Kotlin 2.2.10, Jetpack Compose, Material 3, Navigation Compose 2.8.4 y Lifecycle 2.11.0. |
| Datos | Retrofit 2.11.0, Room 2.8.5 y KSP 2.3.10. |
| Firebase | Google Services 4.5.0, Firebase BoM 33.5.1, Authentication y Cloud Firestore. |
| Prueba | Emulador con Google Play y Android 13 o superior para probar el permiso de notificaciones. Conexión a internet para el ingreso inicial y la sincronización. |

## Configurar Firebase

1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/) y registra una aplicación Android con el paquete `com.tecsup.productos`.
2. Descarga `google-services.json` y colócalo en `Semana12/productos/app/`. El archivo queda excluido de Git.
3. En **Authentication → Método de acceso**, habilita **Correo electrónico/contraseña**.
4. En **Authentication → Usuarios**, crea una cuenta de prueba por rol y asigna una contraseña a cada una. Conserva las credenciales fuera del repositorio.
5. Crea la base de Cloud Firestore `(default)` y la colección `usuarios`. Usa el UID de cada cuenta de Authentication como ID de su documento. Los dos campos son de tipo texto.

   | Cuenta de ejemplo | ID del documento | `rol` | `nombre` |
   |---|---|---|---|
   | `usuario@productos.com` | UID de la cuenta de usuario | `usuario` | `Usuario` |
   | `admin@productos.com` | UID de la cuenta de administrador | `admin` | `Administrador` |

6. En **Firestore → Reglas**, publica estas reglas para que cada cuenta autenticada pueda leer su propio perfil.

```text
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Compilar y ejecutar

1. Abre `Semana12/productos` como proyecto en Android Studio.
2. Verifica que `google-services.json` esté dentro de `app/` y sincroniza el proyecto con Gradle.
3. Desde PowerShell, situado en `Semana12/productos`, ejecuta `.\gradlew.bat :app:assembleDebug`.
4. Selecciona la configuración **app**, elige un emulador y pulsa **Run**. Entra con una de las cuentas de prueba.
5. El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Organización del código

- `data/remote` define los endpoints, el cliente Retrofit y el modelo recibido de la API.
- `data/local` contiene la entidad, el DAO y la base de datos Room.
- `data/repository` sincroniza la API con Room y conserva las marcas de favoritos en una transacción.
- `data/auth` autentica con Firebase y recupera el rol desde Firestore.
- `data/mapper` transforma los datos entre los modelos remoto, local y de la interfaz.
- `domain/model` contiene `Item`, `Sesion` y `Rol`.
- `ui` organiza las pantallas, los ViewModel, los estados y la navegación.
- `notification` contiene la interfaz `Notificador` y la implementación con el canal de Android.

## Pruebas unitarias

`ListaViewModelTest` usa un repositorio falso para comprobar tres casos:

1. La carga exitosa muestra el catálogo y solicita la notificación de sincronización.
2. Si falla la conexión y hay datos en caché, se muestran los productos guardados y un aviso.
3. Si falla la conexión y la caché está vacía, se muestra un error. Al recuperar la conexión y reintentar, se carga el catálogo.

Desde `Semana12/productos`, ejecuta en PowerShell:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

El reporte HTML se genera en `app/build/reports/tests/testDebugUnitTest/index.html`.

## Pruebas manuales

| Caso | Cómo probarlo | Resultado esperado |
|---|---|---|
| PA-01 y PA-02 | Entra con cada cuenta y revisa el catálogo. | El acceso al Panel Admin aparece con el rol `admin`. |
| PA-03 y PA-04 | Envía el formulario vacío y prueba una contraseña incorrecta. | El formulario muestra el mensaje de validación o ingreso fallido. |
| PA-05 y PA-06 | Acepta el permiso, abre el catálogo con internet y toca un producto. | Aparecen los productos, la notificación de sincronización y el detalle del producto seleccionado. |
| PA-07 y PA-10 | Con la cuenta admin, marca un favorito, reabre la app y sincroniza desde el Panel Admin. | El producto sigue marcado. |
| PA-08 | Carga el catálogo, activa modo avión con Wi-Fi apagado y reabre la app con la sesión iniciada. | Se muestra el catálogo guardado y el aviso de sin conexión. |
| PA-09 | Entra como administrador, limpia la caché, activa modo avión con Wi-Fi apagado y reabre la app con la sesión iniciada. | El catálogo muestra el error y ofrece **Reintentar**. |
| PA-11 | En el Panel Admin, pulsa **Limpiar caché** y luego **Sincronizar** con internet. | Los dos totales pasan a cero al limpiar. Al sincronizar, se recupera el catálogo. |
| PA-12 y PA-13 | Gira el emulador en el catálogo. Después cierra sesión y vuelve a abrir la app. | El catálogo conserva su estado al girar. Al cerrar sesión, reaparece el formulario de ingreso. |
| PA-14 | Rechaza el permiso al abrir la app o desactiva sus notificaciones en Ajustes. Luego sincroniza. | El catálogo se actualiza y las notificaciones quedan desactivadas. |

Las capturas de login, catálogo, detalle, favoritos, panel Admin y notificación se presentan en el informe del laboratorio.

## Autor

**Iván Jara Ayala.** Desarrolló la aplicación base en `main` y la segunda fase en `mejora-ia`, que incluye el rediseño, el manejo de errores y las pruebas del ViewModel.
