package com.jara.lavacampus.model

import com.jara.lavacampus.data.local.entity.Servicio
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.Normalizer
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

// Adaptado de Semana06/saludplus: nombres Unicode, correo por segmentos y longitudes.
object Validaciones {
    const val MAX_NOMBRE = 60
    const val MAX_CORREO = 254
    const val MAX_CLAVE = 64
    const val MAX_TEXTO = 300
    const val MAX_BUSQUEDA = 80
    const val MAX_LINEAS = 50
    const val MAX_PRECIO = 9999.99
    private val nombrePermitido = Regex("[\\p{L}\\p{M} /()'’\\-]*")
    private val precioParcial = Regex("[0-9]{0,4}([.,][0-9]{0,2})?")
    private val precioCompleto = Regex("[0-9]{1,4}([.,][0-9]{1,2})?")
    private val usuarioCorreo = Regex("[A-Za-z0-9._%+\\-]+")
    private val segmentoDominio = Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?")

    fun normalizarNombre(nombre: String): String = Normalizer.normalize(nombre.trim(), Normalizer.Form.NFC)
        .replace(Regex(" +"), " ")
    fun claveNombre(nombre: String): String = normalizarNombre(nombre).lowercase(Locale.ROOT)
    fun normalizarCorreo(correo: String): String = correo.trim().lowercase(Locale.ROOT)
    fun nombreEditable(nombre: String): Boolean = nombre.length <= MAX_NOMBRE && nombrePermitido.matches(nombre)
    fun errorNombre(nombre: String): String? {
        if (!nombreEditable(nombre)) return "Usa solo letras, espacios y separadores / - ' ( ). Máximo 60 caracteres."
        val n = normalizarNombre(nombre)
        if (n.length < 3 || n.count { it.isLetter() } < 3) return "El nombre debe contener al menos 3 letras."
        return null
    }
    fun precioEditable(texto: String): Boolean = texto.length <= 7 && precioParcial.matches(texto)
    fun precio(texto: String): Double? {
        if (!precioCompleto.matches(texto)) return null
        return texto.replace(',', '.').toDoubleOrNull()?.takeIf { precioValido(it) }
    }
    fun precioValido(valor: Double): Boolean = valor.isFinite() && valor > 0.0 && valor <= MAX_PRECIO &&
        BigDecimal.valueOf(valor).stripTrailingZeros().scale() <= 2
    fun errorPrecio(texto: String): String? = if (precio(texto) == null)
        "Usa un precio de S/ 0.01 a S/ 9999.99, con máximo 2 decimales." else null

    fun textoEditable(texto: String, maximo: Int = MAX_TEXTO): Boolean = texto.length <= maximo &&
        texto.none { (it.isISOControl() && it != '\n' && it != '\t') || Character.getType(it) == Character.FORMAT.toInt() }
    fun errorTexto(texto: String, campo: String): String? = if (!textoEditable(texto))
        "$campo admite hasta $MAX_TEXTO caracteres y no permite controles invisibles." else null
    fun correoValido(valor: String): Boolean {
        if (valor.length !in 5..MAX_CORREO || valor.count { it == '@' } != 1) return false
        val correo = normalizarCorreo(valor)
        val (usuario, dominio) = correo.split('@')
        val segmentos = dominio.split('.')
        return usuario.length in 1..64 && usuarioCorreo.matches(usuario) &&
            !usuario.startsWith('.') && !usuario.endsWith('.') && ".." !in usuario &&
            segmentos.size >= 2 && segmentos.all { segmentoDominio.matches(it) } &&
            segmentos.last().length in 2..63 && segmentos.last().all { it in 'a'..'z' }
    }
    fun errorLogin(correo: String, clave: String): String? = when {
        !correoValido(correo) -> "Escribe un correo válido de hasta 254 caracteres."
        clave.isBlank() || clave.length > MAX_CLAVE || clave.any { it.isISOControl() } -> "Escribe tu contraseña (máximo 64 caracteres)."
        else -> null
    }
    fun validarServicio(s: Servicio) {
        require(s.id >= 0) { "Identificador de servicio inválido." }
        require(errorNombre(s.nombre) == null) { errorNombre(s.nombre).orEmpty() }
        require(precioValido(s.precio)) { "El precio debe estar entre S/ 0.01 y S/ 9999.99 y tener hasta 2 decimales." }
        require(errorTexto(s.descripcion, "La descripción") == null) { errorTexto(s.descripcion, "La descripción").orEmpty() }
        require(s.unidad in Unidad.entries.map { it.name }) { "Unidad inválida." }
        require(s.horasEntrega in listOf(24, 48, 72)) { "La entrega debe ser de 24, 48 o 72 horas." }
        require(s.icono in listOf("wash", "iron", "shirt")) { "Ícono inválido." }
    }
    fun cantidadValida(unidad: String, cantidad: Double): Boolean = cantidad.isFinite() && when (unidad) {
        "KG" -> cantidad in 1.0..10.0 && cantidad * 2.0 % 1.0 == 0.0
        "PRENDA", "UNIDAD" -> cantidad in 1.0..30.0 && cantidad % 1.0 == 0.0
        else -> false
    }
    fun validarLineas(lineas: List<LineaPedido>) {
        require(lineas.size in 1..MAX_LINEAS) { "El pedido debe tener entre 1 y $MAX_LINEAS servicios." }
        require(lineas.all { it.servicioId > 0 && it.cantidad.isFinite() && it.cantidad > 0.0 }) { "El pedido contiene una línea inválida." }
        require(lineas.map { it.servicioId }.distinct().size == lineas.size) { "No repitas un servicio en el mismo pedido." }
    }
    fun subtotal(cantidad: Double, precio: Double): Double = BigDecimal.valueOf(cantidad)
        .multiply(BigDecimal.valueOf(precio)).setScale(2, RoundingMode.HALF_UP).toDouble()
    fun total(importes: List<Double>): Double = importes.fold(BigDecimal.ZERO) { suma, v -> suma + BigDecimal.valueOf(v) }.toDouble()
    fun fechaEntrega(horas: Int, hoy: LocalDate = LocalDate.now()): LocalDate {
        require(horas in listOf(24, 48, 72)) { "Tiempo de entrega inválido." }
        val fecha = hoy.plusDays((horas / 24).toLong())
        return if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha.plusDays(1) else fecha
    }
}
