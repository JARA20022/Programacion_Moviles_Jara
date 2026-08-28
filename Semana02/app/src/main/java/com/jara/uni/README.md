# Sistema de Matrícula (Kotlin)

Programa de consola que calcula el costo de matrícula de un estudiante a partir de los cursos que lleva, usando **solo condicionales** (`if` / `else`), sin clases, sin herencia y sin ningún concepto de programación orientada a objetos.

## Requisitos del ejercicio

- Ingreso de datos por **terminal** (`readLine()`), no por interfaz gráfica.
- Datos a ingresar:
  1. Nombre del estudiante
  2. Cantidad de cursos a matricular
  3. Valor de cada crédito (S/)
  4. Por cada curso: nombre del curso y cantidad de créditos
- Cálculos:
  - Costo de cada curso = créditos del curso × valor del crédito
  - Total de créditos matriculados
  - Total a pagar (suma de todos los costos)
- Reglas de negocio (condicionales):
  - **Carga académica**, según el total de créditos:
    - Hasta 12 créditos → *Malla Regular*
    - De 13 a 18 créditos → *Carga Completa*
    - Más de 18 créditos → *Permiso Autorizado*
  - **Forma de pago**, según el total a pagar:
    - Si el total supera S/ 2500 → *3 cuotas*
    - Si no → *2 cuotas*
- Resultado final a mostrar: nombre del estudiante, detalle de curso/créditos/costo por curso, cantidad de cursos matriculados, total de créditos, total a pagar, carga académica y forma de pago.
- El desarrollo debe reflejarse en **3 commits** separados en Git/GitHub:
  1. Ingreso de datos
  2. Cálculos
  3. Visualización de resultados

## Tecnología

- **Kotlin** puro (sin Android, sin frameworks), corrido directo por consola.
- Compilado y ejecutado con `kotlinc` / `java`, sin depender del run configuration de Android Studio.

## Archivo principal

```
app/src/main/java/com/jara/uni/Matricula.kt
```

## Cómo compilar y ejecutar

```bash
kotlinc app/src/main/java/com/jara/uni/Matricula.kt -include-runtime -d matricula.jar
java -jar matricula.jar
```

El programa pedirá los datos por consola en este orden: nombre del estudiante, cantidad de cursos, valor del crédito, y luego nombre + créditos de cada curso.

## Ejemplo de salida

```
===================================
        BOLETA DE MATRÍCULA
===================================
Estudiante: Ivan
-----------------------------------
Curso               Créditos  Costo (S/)
Base de datos       3         300.00
Ing de software     5         500.00
-----------------------------------
Cursos matriculados: 2
Total de créditos: 8
Total a pagar: S/ 800.00
Carga académica: Malla Regular
Forma de pago: 2 cuotas
===================================
```

## Historial de commits

| Commit | Contenido |
|---|---|
| 1 | Ingreso de datos por teclado |
| 2 | Implementación de los cálculos de costos y créditos |
| 3 | Visualización de resultados (boleta final) |

Repositorio: https://github.com/JARA20022/Programacion_Moviles_Jara/tree/main/Semana02
