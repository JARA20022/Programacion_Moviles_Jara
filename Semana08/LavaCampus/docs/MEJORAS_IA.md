# LavaCampus · fase 2

Base: main `63806c2`. Se conservan las validaciones y las funciones de ambos perfiles.

## Tres mejoras de la guía

1. **Pedidos atrasados:** consulta en PedidoDao por fecha anterior a hoy, excluyendo ENTREGADO y CANCELADO. La interfaz LavaRepository expone la consulta. BandejaViewModel muestra marca roja y filtro; PanelViewModel cuenta resultados. El reloj comprueba la fecha cada minuto mientras se observa la pantalla.
2. **Descuento por volumen:** si la suma de KG supera 5, se descuenta 10 % solo en las líneas por KG. Las prendas y unidades mantienen su tarifa. Cada descuento se redondea a céntimos HALF_UP. El Repository recalcula todo dentro de la transacción. Pedido guarda subtotal bruto, descuento y total neto; cada detalle guarda subtotal neto y descuento. Los ingresos por servicio suman importes netos.
3. **Repetir pedido:** desde Historial abre un borrador editable. Verifica propietario y estado ENTREGADO/CANCELADO. Usa precios actuales; omite con aviso servicios inactivos, inexistentes o cantidades incompatibles. Exige confirmación, no inserta automáticamente ni modifica el original.

| Pedido | Subtotal | Descuento | Total |
|---|---:|---:|---:|
| 3.5 kg a S/8 + 5 prendas a S/2.50 | S/40.50 | S/0.00 | S/40.50 |
| 5 kg a S/8 | S/40.00 | S/0.00 | S/40.00 |
| 5.5 kg a S/8 | S/44.00 | S/4.40 | S/39.60 |
| 6 kg a S/8 + 5 prendas a S/2.50 | S/60.50 | S/4.80 | S/55.70 |

## Diseño Estudio Textil

Trama de puntos en Canvas, verde petróleo con acentos lima, títulos serif y etiquetas monoespaciadas. Acceso, inicio y panel tienen composiciones nuevas. Tarjetas con esquinas recortadas, transición breve entre pantallas, motivo decorativo que se dibuja una vez y respuesta al pulsar el botón de acceso. Las APIs estándar de Compose respetan la escala de animación del sistema. Se mantiene modo oscuro y desplazamiento para acceder a los formularios con teclado.

## Persistencia y validación

Room pasa de versión 1 a 2 con MIGRACION_1_2, sin borrar tablas. Agrega descuento/subtotal a pedidos y descuento a detalles. Los pedidos anteriores mantienen total y descuento cero; su subtotal se rellena con total. No se aplica retroactivamente el descuento a pedidos antiguos. La prueba instrumentada utiliza una base temporal, conserva un pedido de ejemplo y exige que Room valide el esquema migrado.

Después de instalar esta versión, la base del dispositivo será v2. Para probar de nuevo la APK antigua de main, usa otro emulador: el código v1 no abre una base v2 sin migración de retorno.

Se mantienen nombres sin números, Unicode/tildes, límites de longitud, formato de correo, precios positivos con dos decimales, bloqueo de NaN/exponentes, cantidades por unidad, servicios activos, líneas sin duplicados y límite de tres pedidos activos. Repetir añade comprobación de propietario/estado y el descuento no se recibe como dato de la pantalla.

## Ramas

La rama mejora-ia ya contiene SaludPlus. El script preserva ese historial y sus archivos; incorpora solo Semana08/LavaCampus desde main en un commit de base. Después crea commits A, B, C, diseño y documentación. main no se modifica y no se fuerza el push.

## Verificación y fuentes

El script ejecuta testDebugUnitTest, assembleDebug y assembleDebugAndroidTest. Con un único emulador conectado instala ambas APK con ADB -r y ejecuta las pruebas instrumentadas antes de enviar, sin desinstalar ni borrar datos de la app. Las comprobaciones manuales y capturas deben realizarse en el dispositivo: docs/PRUEBAS_IA.md comienza como pendiente.

- Guía GGGGLAB-S08: fase 2, páginas 29–30.
- https://developer.android.com/training/data-storage/room/migrating-db-versions
- https://developer.android.com/develop/ui/compose/animation/quick-guide
