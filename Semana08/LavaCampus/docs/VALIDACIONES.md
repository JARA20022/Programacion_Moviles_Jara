# Validaciones de LavaCampus

## Referencias reutilizadas del repositorio

- `Semana06/saludplus/.../data/repository/Repositorio.kt`, rama `mejora-ia`, commit `c28383e3`: normalización de nombres y correos, validación del correo por segmentos, longitudes 60/254/64 y rechazo de formatos inválidos.
- `Semana06/saludplus/.../ui/screens/auth/RegistroScreen.kt`, mismo commit: controles de caracteres y límites al escribir.
- `Semana03/lab03-2/.../MainActivity.kt`: lectura numérica mediante conversiones seguras; se reforzó para rechazar notación científica, no finitos y exceso de decimales.

Las reglas de registro de personas no se copiaron literalmente: LavaCampus no tiene registro ni celular editable. Un servicio puede llamarse «Planchado» y se conserva «Edredón / frazada». Las claves de práctica `1234` y `admin` siguen siendo válidas para el login.

## Reglas aplicadas

| Campo o dato | Regla |
| --- | --- |
| Nombre del servicio | 3 letras como mínimo, hasta 60 caracteres; letras Unicode, espacios y separadores `/ - ' ’ ( )`; no números ni etiquetas HTML. Se normalizan espacios y Unicode. |
| Nombre duplicado | Se rechaza otro servicio con el mismo nombre normalizado, ignorando mayúsculas. |
| Precio | De S/0.01 a S/9999.99; máximo 4 cifras enteras y 2 decimales; acepta coma o punto. Rechaza signos, letras, exponentes, NaN e infinito. |
| Correo | Hasta 254 caracteres, un `@`, usuario de 1 a 64 caracteres, dominio por segmentos y sufijo alfabético. |
| Clave de login | No vacía, hasta 64 caracteres; sin controles; se compara exactamente, sin recortar la contraseña. |
| Descripción y nota | Hasta 300 caracteres; se admiten números y puntuación, pero no controles invisibles. |
| Búsquedas | Hasta 80 caracteres. |
| Cantidad | KG: 1–10 en pasos de 0.5; PRENDA/UNIDAD: enteros 1–30. Debe ser finita. |
| Pedido | De 1 a 50 servicios, sin identificadores repetidos ni inválidos; usuario estudiante existente y menos de 3 pedidos activos. |
| Disponibilidad | Servicios existentes y activos, con precios válidos; la fecha corresponde al mayor plazo y omite domingo. |
| Importes | Los subtotales nuevos se redondean a céntimos con HALF_UP; el total suma esos subtotales. Los pedidos anteriores no se recalculan. |

## Capas

`model/Validaciones.kt` concentra funciones puras. Los ViewModels aplican las reglas y exponen los errores; la UI muestra mensajes junto a los campos. `LavaRepositoryRoom` vuelve a comprobar los datos antes de escribir, dentro de una transacción. Las consultas siguen usando parámetros; no se concatena entrada del usuario en SQL.

## Comprobación

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug
```

`ValidacionesTest.kt` cubre nombres, texto, correo, claves de práctica, precios, límites, cantidades, duplicados en líneas, fechas e importes. En el emulador, prueba también pegar letras en Precio, números en Nombre y notas largas. Una validación de entrada no convierte esta práctica local en un sistema de autenticación de servidor.

## Alcance de la rama

Este bloque aplica las validaciones de la app base en `main`, por indicación del usuario, y las reúne en un solo commit. No inicia la fase de mejoras ni modifica la rama `mejora-ia`. SaludPlus se utilizó únicamente como referencia de código.
