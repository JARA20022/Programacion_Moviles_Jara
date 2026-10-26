# Prompts utilizados · LavaCampus

## Prompt 1

**Texto enviado a ChatGPT:** «dame todo de una nomas para hacerlo de mejora ia de un solo script con todas las vaciones, y un diseño diferente, animaciones ,entradas, diseños raros, texturizados pero diferentes profesional ahora si damelo».

**Qué generó la IA:** script PowerShell con atrasados, descuento por volumen y repetir pedido; rediseño Compose, migración 1→2, pruebas automatizadas, documentación y commits por bloque.

**Descomposición técnica realizada por la IA (no son mensajes adicionales del estudiante):** consultar atrasados desde DAO y exponerlos mediante Repository/ViewModels; calcular el descuento en transacción; repetir como borrador con precios actuales; crear trama en Canvas, nueva composición visual y transiciones.

**Qué se corrigió o adaptó:** conservar la rama preexistente de SaludPlus; importar únicamente LavaCampus; limitar el descuento a líneas KG; guardar descuento por detalle para mantener ingresos netos coherentes; comprobar propiedad del pedido; dejar las pruebas manuales pendientes hasta realizarlas.

**¿Respeta MVVM?:** Sí. Las pantallas muestran estado y envían acciones a ViewModels. Los ViewModels dependen de LavaRepository, sin DAOs. La implementación Room valida y persiste; PreciosCampus y RepetirPedido concentran reglas puras. El cambio de entidades eleva la base a v2 con migración explícita.

## Reflexión técnica propuesta — revisar con la experiencia de la práctica

- La IA ayuda cuando las reglas y los límites se definen con precisión.
- Compilar no demuestra por sí solo que una pantalla o migración funcione en el dispositivo.
- El precio y descuento se recalculan al persistir para no confiar en la interfaz.
- MVVM permite cambiar el diseño sin mezclar SQL con componentes visuales.
- Los casos límite y evidencias reales deben comprobarse antes de dar por finalizada una mejora.

Consultar docs/MEJORAS_IA.md y docs/PRUEBAS_IA.md. No se atribuyen al estudiante comprobaciones manuales o aprendizajes que aún no haya confirmado.
