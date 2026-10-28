# Prompts utilizados · LavaCampus

## Prompt 1

**Texto enviado a ChatGPT:** «dame todo de una nomas para hacerlo de mejora ia de un solo script con todas las vaciones, y un diseño diferente, animaciones ,entradas, diseños raros, texturizados pero diferentes profesional ahora si damelo».

**Qué generó la IA:** script PowerShell con atrasados, descuento por volumen y repetir pedido; rediseño Compose, migración 1→2, pruebas automatizadas, documentación y commits por bloque.

**Descomposición técnica realizada por la IA (no son mensajes adicionales del estudiante):** consultar atrasados desde DAO y exponerlos mediante Repository/ViewModels; calcular el descuento en transacción; repetir como borrador con precios actuales; crear trama en Canvas, nueva composición visual y transiciones.

**Qué se corrigió o adaptó:** limitar el descuento a líneas KG; guardar descuento por detalle para mantener ingresos netos coherentes; comprobar propiedad del pedido; dejar las pruebas manuales pendientes hasta realizarlas. La primera automatización reutilizó una rama existente; después se corrigió el origen de mejora-ia para partir de main, conservando el historial anterior en una rama de respaldo.

**¿Respeta MVVM?:** Sí. Las pantallas muestran estado y envían acciones a ViewModels. Los ViewModels dependen de LavaRepository, sin DAOs. La implementación Room valida y persiste; PreciosCampus y RepetirPedido concentran reglas puras. El cambio de entidades eleva la base a v2 con migración explícita.

## Reflexión técnica propuesta — revisar con la experiencia de la práctica

- La IA ayuda cuando las reglas y los límites se definen con precisión.
- Compilar no demuestra por sí solo que una pantalla o migración funcione en el dispositivo.
- El precio y descuento se recalculan al persistir para no confiar en la interfaz.
- MVVM permite cambiar el diseño sin mezclar SQL con componentes visuales.
- Los casos límite y evidencias reales deben comprobarse antes de dar por finalizada una mejora.

Consultar docs/MEJORAS_IA.md y docs/PRUEBAS_IA.md. No se atribuyen al estudiante comprobaciones manuales o aprendizajes que aún no haya confirmado.


## Revisión visual solicitada

**Extracto del mensaje real del usuario:** «no le veo diferentecia al de main tedije algo moderno».

**Respuesta implementada:** reemplazar la propuesta visual anterior por Aurora: login, inicio, panel, formulario de pedido y componentes comunes; añadir navegación inferior según rol, filtros redondeados, superficies violeta/coral, textura discreta en Canvas y entradas breves. Mantener las operaciones, consultas, validaciones y migración existentes.

**Revisión técnica:** comprobar rutas y acciones de ambos roles, compilar APK principal y APK de pruebas y ejecutar la suite unitaria existente. Los resultados de interacción y las capturas de esta edición deben registrarse después de probarla en un dispositivo real o emulador; no se asumen aprobados por compilar.


## Corrección del flujo Git

**Extracto del mensaje real del usuario:** «mi script reutilizó la rama mejora-ia existente e incorporó LavaCampus [...] mientras que el PDF indica crearla desde main [...] debe ser exacto al pdf».

**Qué generó la IA:** un script de reparación que prepara una rama nueva desde main, reaplica los tres commits funcionales, conserva el código Aurora, verifica el resultado y respalda la rama anterior antes de sustituir mejora-ia.

**Qué se corrigió:** el origen y el orden del historial de la fase 2. Se conservan los commits originales en la rama de respaldo y la procedencia de los tres commits funcionales mediante cherry-pick -x. No se cambian fechas para simular otro proceso de desarrollo.

**¿Respeta MVVM?:** Sí. Esta reparación reorganiza Git y actualiza documentación. El árbol app de la rama corregida debe ser idéntico al de la versión Aurora que ya estaba implementada; no modifica reglas, entidades, pantallas ni preferencias.
