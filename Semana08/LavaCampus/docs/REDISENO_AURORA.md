# Rediseño Aurora · mejora-ia

## Cambios visibles

- Login sin la tarjeta de bienvenida anterior: marca, motivo orbital y formulario independiente.
- Inicio del estudiante con bienvenida, acción Nuevo pedido, seguimiento y accesos en dos columnas.
- Panel con balance destacado, cifras integradas, aviso de atrasos, accesos en cuadrícula y selector de gráficos.
- Navegación inferior: Inicio/Pedidos/Servicios/Perfil para estudiantes; Panel/Pedidos/Servicios/Ajustes para administración. Se oculta en formularios y detalles.
- Pedido nuevo con tarjetas seleccionables por cantidad y total/confirmación en una barra inferior.
- Tipografía sans serif, violeta/coral, tarjetas redondeadas y grano de fondo estático. El motivo orbital y la entrada se animan una vez, sin bucles de consumo permanente.
- En la primera apertura se activa modo oscuro; luego Ajustes o Perfil permiten elegir claro/oscuro y se guarda la elección.

## Alcance

Solo presentación, navegación, inicialización de apariencia y documentación. No cambian modelos, DAO, repositorios, ViewModels, validaciones, Room, DataStore ni versiones de dependencias. Se conservan atrasados, descuento por volumen, repetir pedido, permisos de rol y todas las operaciones anteriores. No se ejecuta borrado de datos ni reinstalación destructiva.

## Verificación que ejecuta el script

`./gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest`

Si hay exactamente un dispositivo conectado, instala con `adb install -r` y abre LavaCampus. Si falla la compilación o la instalación, se detiene antes del commit. Se puede volver a ejecutar para continuar: admite archivos originales o exactamente iguales a los generados, y rechaza otros cambios.

## Revisión manual pendiente

- [ ] Login de ambos roles y errores de validación.
- [ ] Navegación inferior y botón Atrás, sin duplicar pantallas.
- [ ] Nuevo pedido, teclado, nota, cantidad y confirmación.
- [ ] Descuento: 6 kg a S/8 + 5 prendas a S/2.50 = S/55.70.
- [ ] Repetir desde historial y comprobar tarifas actuales.
- [ ] Pedidos atrasados, filtros y acciones administrativas.
- [ ] Modo claro/oscuro y persistencia tras reabrir.
- [ ] Tamaño de letra grande y desplazamiento en pantalla pequeña.
- [ ] Capturas nuevas de ambos roles para el informe.

No sustituir capturas de main: conservarlas y crear evidencias específicas de mejora-ia.

## Referencias técnicas

- https://developer.android.com/develop/ui/compose/animation/composables-modifiers
- https://developer.android.com/develop/ui/compose/components/navigation-bar
