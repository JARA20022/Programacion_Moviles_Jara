package com.saludplus.citas.data

import java.util.Locale

object ValidacionDatos {
    private val nombrePermitido =
        Regex("^[\\p{L}]+(?:[ '\u2019-][\\p{L}]+)*$")
    private val usuarioCorreo =
        Regex("[A-Za-z0-9._%+\\-]+")
    private val segmentoDominio =
        Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?")
    private val telefonoPeruano =
        Regex("9[0-9]{8}")

    fun normalizarNombre(nombre: String): String =
        nombre.trim().replace(Regex("\\s+"), " ")

    fun normalizarCorreo(correo: String): String =
        correo.trim().lowercase(Locale.ROOT)

    private fun correoValido(correo: String): Boolean {
        if (correo.length !in 5..254 || correo.count { it == '@' } != 1) {
            return false
        }

        val (usuario, dominio) = correo.split('@')
        val segmentos = dominio.split('.')

        return usuario.length in 1..64 &&
                usuarioCorreo.matches(usuario) &&
                !usuario.startsWith('.') &&
                !usuario.endsWith('.') &&
                ".." !in usuario &&
                segmentos.size >= 2 &&
                segmentos.all { segmentoDominio.matches(it) } &&
                segmentos.last().length in 2..63 &&
                segmentos.last().all { it in 'a'..'z' }
    }

    fun errorRegistro(
        nombre: String,
        correo: String,
        telefono: String,
        clave: String
    ): String? {
        if (
            nombre.length > 100 ||
            correo.length > 254 ||
            telefono.length > 9 ||
            clave.length > 64
        ) {
            return "Uno de los campos supera la longitud permitida."
        }

        val nombreLimpio = normalizarNombre(nombre)
        val correoLimpio = normalizarCorreo(correo)

        return when {
            nombreLimpio.length !in 3..60 ||
                    nombreLimpio.split(' ').size < 2 ||
                    !nombrePermitido.matches(nombreLimpio) ->
                "Escribe nombre y apellido usando solo letras. Máximo 60 caracteres."

            !correoValido(correoLimpio) ->
                "Escribe un correo válido de hasta 254 caracteres."

            !telefonoPeruano.matches(telefono) ->
                "El celular debe comenzar con 9 y tener exactamente 9 números."

            clave.length !in 6..64 ||
                    clave.any { it.isISOControl() } ||
                    clave.none { it.isLetter() } ||
                    clave.none { it in '0'..'9' } ->
                "La contraseña debe tener de 6 a 64 caracteres, con letras y números."

            else -> null
        }
    }

    fun errorLogin(correo: String, clave: String): String? = when {
        correo.length > 254 ->
            "El correo supera la longitud permitida."

        !correoValido(normalizarCorreo(correo)) ->
            "Escribe un correo válido."

        clave.isEmpty() || clave.length > 64 ->
            "Escribe tu contraseña (máximo 64 caracteres)."

        else -> null
    }
}