# Pruebas de aceptación · mejora-ia

**Pendientes de realizar en el dispositivo.** Registrar resultado real y fecha; no marcar aprobado sin comprobarlo.

| Caso | Esperado | Resultado |
|---|---|---|
| Actualizar desde main | Conserva usuarios, pedidos y detalles | Pendiente |
| Pedido activo anterior a hoy | Marca roja, filtro y contador de atrasados | Pendiente |
| Fecha de hoy/futura, ENTREGADO o CANCELADO | Excluido de atrasados | Pendiente |
| Finalizar atrasado | Desaparece y contador baja sin reiniciar | Pendiente |
| 5 kg a S/8 | Sin descuento, S/40 | Pendiente |
| 5.5 kg a S/8 | Ahorro S/4.40, total S/39.60 | Pendiente |
| 6 kg + 5 prendas a S/2.50 | Subtotal S/60.50, ahorro S/4.80, total S/55.70 | Pendiente |
| Cerrar y abrir pedido con descuento | Mismos importes guardados | Pendiente |
| Repetir desde historial | Borrador; no se guarda sin confirmar | Pendiente |
| Cambiar tarifa antes de repetir | Usa tarifa nueva | Pendiente |
| Servicio anterior inactivo | Lo omite y avisa | Pendiente |
| Todos los servicios inactivos | Borrador vacío; Confirmar apagado | Pendiente |
| Repetir teniendo tres pedidos activos | Rechaza el cuarto al confirmar | Pendiente |
| Letras en precio / números en nombre | Rechazo con mensaje | Pendiente |
| Texto largo pegado / NaN / 1e3 | Rechazado, app operativa | Pendiente |
| Modo oscuro, texto grande y teclado | Contenido legible y accesible al desplazar | Pendiente |
| Atrás y pulsaciones rápidas | No duplica pedidos ni pierde datos | Pendiente |

Repetir también los doce casos de docs/PRUEBAS.md. La evidencia de main corresponde a la versión base.

Para generar atrasos, identificar un pedido activo de prueba y cambiar únicamente su fecha de entrega a ayer mediante Database Inspector. No alterar estados finales para simular atraso.

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest
# Con emulador:
.\gradlew.bat :app:connectedDebugAndroidTest
```

## Capturas

Después de aplicar el script, ejecutar el mismo archivo con `-Capturas`. Navegar manualmente por las pantallas indicadas. Guarda seis capturas reales en docs/capturas-ia y sus enlaces en docs/EVIDENCIAS_IA.md. No se certifica su contenido automáticamente; revisar antes del informe.
