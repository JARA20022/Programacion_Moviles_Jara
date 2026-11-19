package com.tecsup.centrotareas.model

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data class Calculando(val porcentaje: Int) : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data class Cancelado(val porcentaje: Int) : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}

enum class ModoCarga(val etiqueta: String) {
    SECUENCIAL("Una por una"),
    PARALELO("A la vez")
}

data class TiemposCarga(
    val secuenciales: List<Long> = emptyList(),
    val paralelos: List<Long> = emptyList()
) {
    fun registrar(modo: ModoCarga, ms: Long): TiemposCarga = when (modo) {
        ModoCarga.SECUENCIAL -> copy(secuenciales = secuenciales + ms)
        ModoCarga.PARALELO -> copy(paralelos = paralelos + ms)
    }
}
