package com.jara.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PANEL = "panel"
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val DETALLE_PEDIDO = "detalle_pedido/{id}"

    fun detallePedido(id: Int): String = "detalle_pedido/$id"
}
