package com.jara.uni

fun main() {
    // ---------- 1. INGRESO DE DATOS ----------
    print("Ingrese el nombre del estudiante: ")
    val nombreEstudiante = readLine()!!

    print("Ingrese el valor de cada crédito (S/): ")
    val valorCredito = readLine()!!.toDouble()

    print("Ingrese la cantidad de cursos a matricular: ")
    val cantidadCursos = readLine()!!.toInt()

    val nombresCursos = mutableListOf<String>()
    val creditosCursos = mutableListOf<Int>()

    for (i in 1..cantidadCursos) {
        println("\n--- Curso $i ---")
        print("Nombre del curso: ")
        nombresCursos.add(readLine()!!)

        print("Créditos del curso: ")
        creditosCursos.add(readLine()!!.toInt())
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
        cargaAcademica = "Carga Máxima"
    } else {
        cargaAcademica = "Requiere autorización RENU"
    }

    val formaPago: String
    if (totalPagar > 2500) {
        formaPago = "3 cuotas"
    } else {
        formaPago = "2 cuotas"
    }
}
