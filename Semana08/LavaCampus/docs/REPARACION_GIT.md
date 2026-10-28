# Corrección del origen de mejora-ia

## Base y respaldo

- Fase 1: main en `63806c27dcfb5b028348747cb54723261da89447`.
- Rama anterior: `6aebcbe46dc78f02b0c9873393556e94c034a73a`.
- Respaldo íntegro: `respaldo/mejora-ia-antes-6aebcbe4`, con todos los commits y proyectos de la rama anterior.
- Rama de entrega: mejora-ia creada desde el commit indicado de main.

La primera automatización había incorporado LavaCampus a una rama de mejoras utilizada antes por otros trabajos. La reparación corrige ese procedimiento: el respaldo conserva aquella rama completa; la nueva mejora-ia parte de main y añade únicamente los cambios de Semana08/LavaCampus.

## Orden de los cambios

1. `ia: agrega pedidos atrasados en la bandeja` (commit A).
2. `ia: agrega descuento por volumen` (commit B).
3. `ia: agrega repetir un pedido` (commit C).
4. `ia: rediseña interfaz con navegación inferior y estilo Aurora` (diseño adicional solicitado).
5. `docs: agrega PROMPTS.md` (commit D).

Los primeros tres se reaplican desde los commits de desarrollo existentes con cherry-pick -x. El diseño se conserva con el contenido final de Aurora. La documentación se actualiza para describir el resultado real. No se fabrican avances ni se alteran fechas para aparentar un desarrollo diferente.

## Comprobaciones de la reparación

El script debe verificar que main es ancestro de la nueva mejora-ia, que solo cambia Semana08/LavaCampus respecto a main y que el árbol app completo es idéntico al de la versión Aurora anterior. Antes de publicar, compila el APK principal y el APK de pruebas y ejecuta las pruebas unitarias existentes.

Primero se publica y verifica el respaldo. La sustitución remota utiliza un lease con el SHA exacto de la rama anterior: si alguien actualiza mejora-ia entretanto, Git rechaza la sustitución. main no se publica ni se reescribe.

Las comprobaciones funcionales en emulador, capturas y reflexión personal siguen siendo las que se documenten realmente; reorganizar Git no las aprueba automáticamente.

## Verificación local

```powershell
git merge-base --is-ancestor main mejora-ia
# Debe devolver código 0.
git log --reverse --format="%h %s" main..mejora-ia
git diff --name-only main..mejora-ia
# Todas las rutas deben estar dentro de Semana08/LavaCampus.
```

Referencias: guía del laboratorio, fase 2, páginas 29-30; https://git-scm.com/docs/git-cherry-pick ; https://git-scm.com/docs/git-push .
