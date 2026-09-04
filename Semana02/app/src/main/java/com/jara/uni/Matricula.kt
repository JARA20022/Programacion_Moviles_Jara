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

    val montoRecargoTurno = totalPagar * recargoTurno
    val totalConTurno = totalPagar + montoRecargoTurno

    val formaPago: String
    if (totalConTurno > 2500) {
        formaPago = "3 cuotas"
    } else {
        formaPago = "2 cuotas"
    }

    // ---------- 3. MOSTRAR RESULTADOS ----------
    println("\n===================================")
    println("        BOLETA DE MATRÍCULA")
    println("===================================")
    println("Estudiante: $nombreEstudiante")
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
    println("Recargo por turno (${(recargoTurno * 100).toInt()}%): S/ %.2f".format(montoRecargoTurno))
    println("Total a pagar: S/ %.2f".format(totalConTurno))
    println("Carga académica: $cargaAcademica")
    println("Forma de pago: $formaPago")
    println("===================================")
}