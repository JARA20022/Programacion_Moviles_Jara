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
}
