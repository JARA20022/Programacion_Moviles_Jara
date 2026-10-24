# Pruebas de aceptación — paso 14

Ejecuta las pruebas sobre la misma instalación de LavaCampus. Usa datos ficticios. No borres el almacenamiento para restaurar resultados: perderías pedidos y preferencias. Una prueba fallida se registra como fallida y se corrige antes del commit.

1. **Clave incorrecta.** Cierra sesión, selecciona Estudiante y prueba `ana@correo.test` con una clave equivocada. Debe mostrar error y permanecer en el login.
2. **Pedido vacío.** Ingresa con una cuenta estudiante y abre Nuevo pedido. Sin seleccionar servicios, Confirmar debe estar deshabilitado.
3. **Total y entrega.** Con Lavado y secado a S/8.00 y Planchado a S/2.50, selecciona 3.5 kg y 5 prendas. El total debe ser S/40.50. La fecha es hoy más 2 días; si cae domingo, pasa al lunes. Si editaste esas tarifas durante otra prueba, restablécelas antes de comprobar este ejemplo.
4. **Cuarto pedido activo.** Usa una cuenta con tres pedidos en RECIBIDO, EN_PROCESO o LISTO. Intenta confirmar otro: aparece «Ya tienes 3 pedidos activos» y la cantidad de pedidos no aumenta. Puedes crear pedidos de prueba hasta alcanzar tres, siempre que Recibir pedidos esté activado.
5. **Cancelación del estudiante.** Abre el detalle de un pedido EN_PROCESO: no debe permitir cancelarlo.
6. **Listo sin casillero.** Como administrador, abre un pedido EN_PROCESO y pulsa Marcar como Listo sin elegir casillero: debe mostrar el rechazo y conservar el estado.
7. **Casillero ocupado.** Identifica un pedido LISTO y su casillero. Abre otro pedido EN_PROCESO: ese casillero aparece gris y no permite selección.
8. **Entrega sin cobro.** Abre un pedido LISTO sin pagar e intenta entregarlo: debe rechazarse. Activa Cobrado y vuelve a entregar: pasa a ENTREGADO y libera el casillero.
9. **Seguimiento reactivo.** Mantén abierto el detalle de un pedido RECIBIDO del estudiante. Para probar una modificación mientras esa pantalla sigue visible, usa Database Inspector sobre la misma base y cambia SOLO ese pedido a EN_PROCESO. La pantalla debe actualizarse sin cerrarse. Cambiar de perfil para editarlo y regresar comprueba persistencia, pero no sustituye esta prueba de observación en la pantalla abierta.
10. **Ingresos semanales.** Compara el panel con la suma SQL de pedidos pagados desde el lunes. Crear un pedido sin pagar no aumenta los ingresos. Marcar Cobrado un pedido de la semana sí los actualiza.
11. **Eliminar servicio usado.** En Servicios, intenta eliminar un servicio asociado a pedidos. Debe mostrar el motivo y conservarlo. Desactivarlo sí se permite y lo oculta en Tarifas y Nuevo pedido.
12. **Persistencia.** Activa modo oscuro y deja una sesión abierta. Detén completamente la app, sin desinstalarla ni borrar datos, y vuelve a abrirla. Debe mantener pedidos, perfil y tema.

## Consulta para contrastar ingresos

En Database Inspector, ejecuta esta consulta de lectura. Usa el reloj y la zona del dispositivo, como la app:

```sql
SELECT COALESCE(SUM(total), 0) AS ingresos_semana
FROM pedidos
WHERE pagado = 1
  AND fechaRegistro >= date('now', 'localtime', 'weekday 0', '-6 days');
```

## Prueba del seguimiento sin cerrar la pantalla

Selecciona un pedido RECIBIDO propio del estudiante. En esta consulta, sustituye `<ID_DEL_PEDIDO>` por su identificador real. No ejecutes el marcador literalmente. La condición evita modificar pedidos en otra etapa:

```sql
UPDATE pedidos
SET estado = 'EN_PROCESO'
WHERE id = <ID_DEL_PEDIDO> AND estado = 'RECIBIDO';
```

Esta consulta es una intervención controlada para comprobar la observación de Room, no una función de la interfaz. El avance normal se prueba desde la bandeja del administrador.

## Comprobaciones del paso 13

Además de las doce pruebas anteriores, confirma que cambiar de perfil funciona en ambos sentidos; que apagar Recibir pedidos impide crear; y que Clientes y las estadísticas del perfil reflejan los datos. Vuelve a activar Recibir pedidos al terminar.

## Capturas requeridas

Guarda tres capturas del estudiante y tres del administrador mediante el modo `-Capturas` del script. Navega a la pantalla indicada antes de pulsar ENTER. Comprueba que no aparezca el teclado ni un diálogo que tape el contenido. Las imágenes se guardan en `docs/capturas/`.
