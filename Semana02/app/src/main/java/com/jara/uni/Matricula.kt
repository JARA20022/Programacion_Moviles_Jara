package com.jara.uni

fun main() {
    // ---------- 1. INGRESO DE DATOS ----------
    print("Ingrese el nombre del estudiante: ")
    val nombreEstudiante = readLine()!!

    print("Ingrese la cantidad de cursos a matricular: ")
    val cantidadCursos = readLine()!!.toInt()

    print("Ingrese el valor de cada crédito (S/): ")
    val valorCredito = readLine()!!.toDouble()

    val nombresCursos = mutableListOf<String>()
    val creditosCursos = mutableListOf<Int>()

    for (i in 1..cantidadCursos) {
        println("\n--- Curso $i ---")
        print("Nombre del curso: ")
        nombresCursos.add(readLine()!!)

        print("Créditos del curso: ")
        creditosCursos.add(readLine()!!.toInt())
    }

    // ---------- TURNO (mañana / tarde / noche) ----------
    print("\nIngrese el turno (mañana / tarde / noche): ")
    val turnoIngresado = readLine()!!.trim().lowercase()

    val recargoTurno: Double
    val nombreTurno: String
    if (turnoIngresado == "mañana" || turnoIngresado == "manana") {
        recargoTurno = 0.10
        nombreTurno = "Mañana"
    } else if (turnoIngresado == "tarde") {
        recargoTurno = 0.15
        nombreTurno = "Tarde"
    } else if (turnoIngresado == "noche") {
        recargoTurno = 0.20
        nombreTurno = "Noche"
    } else {
        recargoTurno = 0.0
        nombreTurno = "No especificado"
    }

    // ---------- CATEGORÍA (Ordinario / Becado) ----------
    println("\nSeleccione la categoría del estudiante:")
    println("1. Ordinario")
    println("2. Becado")
    print("Opción: ")
    val opcionCategoria = readLine()!!.toInt()
    val esBecado = opcionCategoria == 2
    val nombreCategoria = if (esBecado) "Becado" else "Ordinario"

    // ---------- 2. CÁLCULOS ----------
    val costosCursos = mutableListOf<Double>()
    var totalCreditos = 0
    var totalPagar = 0.0

    for (i in 0 until cantidadCursos) {
        val costo = creditosCursos[i] * valorCredito
        costosCursos.add(costo)
        totalCreditos += creditosCursos[i]
        totalPagar += costo
    }

    val cargaAcademica: String
    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular"
    } else if (totalCreditos <= 18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Permiso Autorizado"
    }

    // Solo el Ordinario paga matrícula; el Becado no paga
    var totalFinal = 0.0
    var montoRecargoTurno = 0.0
    var igv = 0.0

    if (!esBecado) {
        montoRecargoTurno = totalPagar * recargoTurno
        val subtotalConTurno = totalPagar + montoRecargoTurno
        igv = subtotalConTurno * 0.18
        totalFinal = subtotalConTurno + igv
    }

    val formaPago: String
    if (totalFinal > 2500) {
        formaPago = "3 cuotas"
    } else {
        formaPago = "2 cuotas"
    }

    // ---------- 3. MOSTRAR RESULTADOS ----------
    println("\n===================================")
    println("        BOLETA DE MATRÍCULA")
    println("===================================")
    println("Estudiante: $nombreEstudiante")
    println("Categoría: $nombreCategoria")
    println("Turno: $nombreTurno")
    println("-----------------------------------")
    println(String.format("%-20s%-10s%-10s", "Curso", "Créditos", "Costo (S/)"))
    for (i in 0 until cantidadCursos) {
        println(
            String.format(
                "%-20s%-10d%-10.2f",
                nombresCursos[i],
                creditosCursos[i],
                costosCursos[i]
            )
        )
    }
    println("-----------------------------------")
    println("Cursos matriculados: $cantidadCursos")
    println("Total de créditos: $totalCreditos")
    println("Subtotal cursos: S/ %.2f".format(totalPagar))
    if (!esBecado) {
        println("Recargo por turno (${(recargoTurno * 100).toInt()}%): S/ %.2f".format(montoRecargoTurno))
        println("IGV (18%%): S/ %.2f".format(igv))
    } else {
        println("Becado: no paga matrícula")
    }
    println("Total a pagar: S/ %.2f".format(totalFinal))
    println("Carga académica: $cargaAcademica")
    println("Forma de pago: $formaPago")
    println("===================================")
}