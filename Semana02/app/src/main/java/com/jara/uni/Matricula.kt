package com.jara.uni

fun main() {
    // ---------- AFORO DE LA INSTITUCIÓN (un solo dato, aplicado a cada turno por separado) ----------
    print("Ingrese el aforo máximo de la institución (aplica por igual a cada turno): ")
    val aforoMaximoInstitucion = readLine()!!.toInt()

    var matriculadosManana = 0
    var matriculadosTarde = 0
    var matriculadosNoche = 0
    var continuarMatriculando = true

    while (continuarMatriculando) {

        // ---------- 1. INGRESO DE DATOS ----------
        print("\nIngrese el nombre del estudiante: ")
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
        val aforoTurnoActual: Int
        val matriculadosTurnoActual: Int
        if (turnoIngresado == "mañana" || turnoIngresado == "manana") {
            recargoTurno = 0.10
            nombreTurno = "Mañana"
            aforoTurnoActual = aforoMaximoInstitucion
            matriculadosTurnoActual = matriculadosManana
        } else if (turnoIngresado == "tarde") {
            recargoTurno = 0.15
            nombreTurno = "Tarde"
            aforoTurnoActual = aforoMaximoInstitucion
            matriculadosTurnoActual = matriculadosTarde
        } else if (turnoIngresado == "noche") {
            recargoTurno = 0.20
            nombreTurno = "Noche"
            aforoTurnoActual = aforoMaximoInstitucion
            matriculadosTurnoActual = matriculadosNoche
        } else {
            recargoTurno = 0.0
            nombreTurno = "No especificado"
            aforoTurnoActual = 0
            matriculadosTurnoActual = 0
        }

        val cuposDisponiblesTurno = aforoTurnoActual - matriculadosTurnoActual
        if (cuposDisponiblesTurno > 0) {
            println("Aforo turno $nombreTurno: disponible $cuposDisponiblesTurno de $aforoTurnoActual")
        } else {
            println("Aforo turno $nombreTurno: sin cupos disponibles (0 de $aforoTurnoActual)")
        }

        // ---------- CATEGORÍA (Ordinario / Becado) ----------
        println("\nSeleccione la categoría del estudiante:")
        println("1. Ordinario")
        println("2. Becado")
        print("Opción (puede escribir el número o la palabra): ")
        val categoriaIngresada = readLine()!!.trim().lowercase()
        val esBecado = categoriaIngresada == "2" || categoriaIngresada == "becado"
        val nombreCategoria = if (esBecado) "Becado" else "Ordinario"

        // El monto de matrícula solo se pide si es Ordinario; el Becado no paga nada
        var montoMatricula = 0.0
        if (!esBecado) {
            print("Ingrese el monto de matrícula (S/): ")
            montoMatricula = readLine()!!.toDouble()
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

        // Solo el Ordinario paga (matrícula + recargo de turno + IGV); el Becado no paga nada.
        var totalFinal = 0.0
        var montoRecargoTurno = 0.0
        var igv = 0.0

        if (!esBecado) {
            val baseConMatricula = totalPagar + montoMatricula
            montoRecargoTurno = baseConMatricula * recargoTurno
            val subtotalConTurno = baseConMatricula + montoRecargoTurno
            igv = subtotalConTurno * 0.18
            totalFinal = subtotalConTurno + igv
        }

        val formaPago: String
        if (totalFinal > 2500) {
            formaPago = "3 cuotas"
        } else {
            formaPago = "2 cuotas"
        }

        // Actualizar el contador del turno correspondiente
        if (nombreTurno == "Mañana") {
            matriculadosManana++
        } else if (nombreTurno == "Tarde") {
            matriculadosTarde++
        } else if (nombreTurno == "Noche") {
            matriculadosNoche++
        }
        val cuposDisponiblesTurnoDespues = aforoTurnoActual - matriculadosTurnoActual - 1

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
            println("Monto de matrícula: S/ %.2f".format(montoMatricula))
            println("Recargo por turno (${(recargoTurno * 100).toInt()}%%): S/ %.2f".format(montoRecargoTurno))
            println("IGV (18%%): S/ %.2f".format(igv))
        } else {
            println("Becado: no paga matrícula ni ningún otro monto")
        }
        println("Total a pagar: S/ %.2f".format(totalFinal))
        println("Carga académica: $cargaAcademica")
        println("Forma de pago: $formaPago")
        println("-----------------------------------")
        println("Aforo turno $nombreTurno: disponible $cuposDisponiblesTurnoDespues de $aforoTurnoActual")
        println("===================================")

        print("\n¿Desea matricular a otro estudiante? (s/n): ")
        val respuesta = readLine()!!.trim().lowercase()
        continuarMatriculando = respuesta == "s"
    }
}