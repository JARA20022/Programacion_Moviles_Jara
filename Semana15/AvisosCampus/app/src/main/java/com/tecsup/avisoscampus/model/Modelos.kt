package com.tecsup.avisoscampus.model

enum class Origen(val texto: String) { BOTON("Aviso"), RECORDATORIO("Recordatorio"), MODO_AVION("Modo avión") }
enum class Permiso { SinPedir, Concedido, Denegado }
data class Aviso(val id: Int, val titulo: String, val texto: String, val origen: Origen, val hora: Long = System.currentTimeMillis())
