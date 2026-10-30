package com.jara.lavacampus.model

data class SolicitudSoporteResumen(
    val detalleId: Int,
    val pedidoId: Int,
    val nombreEstudiante: String,
    val descripcionOtros: String,
    val fechaRegistro: String,
    val estadoPedido: String,
    val estadoAtencion: String
)
