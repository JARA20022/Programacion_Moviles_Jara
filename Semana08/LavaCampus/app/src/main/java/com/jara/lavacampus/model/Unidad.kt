package com.jara.lavacampus.model

import com.jara.lavacampus.data.local.entity.Servicio
import java.util.Locale

enum class Unidad { KG, PRENDA, UNIDAD }

fun Servicio.textoPrecio(): String {
    val etiqueta = when (unidad) {
        Unidad.KG.name -> "por kg"
        Unidad.PRENDA.name -> "por prenda"
        else -> "por unidad"
    }
    return String.format(Locale("es", "PE"), "S/ %.2f %s", precio, etiqueta)
}
