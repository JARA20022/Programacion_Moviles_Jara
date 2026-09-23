package com.jara.tecsupfit.model

data class ReservaFit(
    val id: Int,
    val clase: ClaseFit,
    val cupos: Int = 1,
    val estado: String = "Confirmada"
)
