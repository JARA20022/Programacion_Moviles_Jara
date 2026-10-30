package com.jara.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PANEL = "panel"
    const val SERVICIOS = "servicios"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"
    const val SOPORTES = "soportes"
    const val PERFIL = "perfil"
    const val INICIO_SOPORTE = "inicio_soporte"
    const val SOPORTE_PERFIL = "soporte_perfil"
    const val FORMULARIO_SERVICIO = "servicio/{id}"
    fun formularioServicio(id: Int = 0): String = "servicio/$id"

    const val BANDEJA = "bandeja?estado={estado}"
    const val GESTION_PEDIDO = "gestion_pedido/{id}"
    fun bandeja(estado: String = "TODOS"): String = "bandeja?estado=$estado"
    fun gestionPedido(id: Int): String = "gestion_pedido/$id"
    const val NUEVO_PEDIDO = "nuevo_pedido?origen={origen}"
    fun nuevoPedido(origen: Int = 0): String = "nuevo_pedido?origen=$origen"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val SOLICITUDES = "solicitudes"
    const val RESPUESTA_SOLICITUD = "solicitud/{detalleId}"
    fun respuestaSolicitud(detalleId: Int): String = "solicitud/$detalleId"
    const val DETALLE_PEDIDO = "detalle_pedido/{id}"

    fun detallePedido(id: Int): String = "detalle_pedido/$id"
}
