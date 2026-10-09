package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

object Repositorio {

    val especialidades = listOf(
        Especialidad(1, "Medicina general", "Atención y evaluación de salud"),
        Especialidad(2, "Pediatría", "Atención médica para niños"),
        Especialidad(3, "Cardiología", "Cuidado del corazón"),
        Especialidad(4, "Dermatología", "Cuidado de la piel"),
        Especialidad(5, "Traumatología", "Atención de huesos y articulaciones")
    )

    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", 1, 8, 4.9, 80.0),
        Medico(2, "Dr. Luis Ramírez", 1, 6, 4.7, 75.0),
        Medico(3, "Dra. María Flores", 2, 10, 4.9, 90.0),
        Medico(4, "Dr. Carlos Mendoza", 3, 12, 4.8, 120.0),
        Medico(5, "Dra. Patricia Rojas", 4, 7, 4.8, 95.0),
        Medico(6, "Dr. Jorge Castillo", 5, 9, 4.7, 100.0)
    )

    private val usuarios = mutableStateListOf<Usuario>()
    private val citas = mutableStateListOf<Cita>()

    private var siguienteUsuarioId = 1
    private var siguienteCitaId = 1

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )

    // Validaciones que antes estaban en ValidacionDatos.kt.

    private val nombrePermitido =
        Regex("^[\\p{L}]+(?:[ '\u2019-][\\p{L}]+)*$")

    private val usuarioCorreo =
        Regex("[A-Za-z0-9._%+\\-]+")

    private val segmentoDominio =
        Regex("[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?")

    private val telefonoPeruano =
        Regex("9[0-9]{8}")

    private fun normalizarNombre(nombre: String): String =
        nombre.trim().replace(Regex("\\s+"), " ")

    private fun normalizarCorreo(correo: String): String =
        correo.trim().lowercase(Locale.ROOT)

    private fun correoValido(correo: String): Boolean {
        if (
            correo.length !in 5..254 ||
            correo.count { it == '@' } != 1
        ) {
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

    fun errorLogin(correo: String, clave: String): String? =
        when {
            correo.length > 254 ->
                "El correo supera la longitud permitida."

            !correoValido(normalizarCorreo(correo)) ->
                "Escribe un correo válido."

            clave.isEmpty() || clave.length > 64 ->
                "Escribe tu contraseña (máximo 64 caracteres)."

            else -> null
        }

    @Synchronized
    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        telefono: String
    ): Usuario? {
        if (errorRegistro(nombre, correo, telefono, clave) != null) {
            return null
        }

        val correoLimpio = normalizarCorreo(correo)

        if (
            usuarios.any {
                it.correo == correoLimpio || it.telefono == telefono
            }
        ) {
            return null
        }

        val usuario = Usuario(
            id = siguienteUsuarioId++,
            nombre = normalizarNombre(nombre),
            correo = correoLimpio,
            clave = clave,
            telefono = telefono
        )

        usuarios.add(usuario)
        usuarioActual = usuario
        return usuario
    }

    fun iniciarSesion(correo: String, clave: String): Usuario? {
        if (errorLogin(correo, clave) != null) {
            usuarioActual = null
            return null
        }

        val correoLimpio = normalizarCorreo(correo)

        val usuario = usuarios.find {
            it.correo == correoLimpio && it.clave == clave
        }

        usuarioActual = usuario
        return usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> =
        especialidades.filter {
            it.nombre.contains(texto.trim(), ignoreCase = true)
        }

    fun especialidadesDestacadas(): List<Especialidad> =
        especialidades.take(4)

    fun obtenerEspecialidad(id: Int): Especialidad? =
        especialidades.find { it.id == id }

    fun obtenerMedico(id: Int): Medico? =
        medicos.find { it.id == id }

    fun obtenerCita(id: Int): Cita? =
        citas.find {
            it.id == id && it.usuarioId == usuarioActual?.id
        }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> =
        medicos.filter {
            it.especialidadId == especialidadId
        }.sortedByDescending {
            it.calificacion
        }

    fun buscarMedicos(texto: String): List<Medico> =
        medicos.filter {
            it.nombre.contains(texto.trim(), ignoreCase = true)
        }.sortedByDescending {
            it.calificacion
        }

    fun horariosDisponibles(
        medicoId: Int,
        fecha: String
    ): List<String> {
        if (obtenerMedico(medicoId) == null || fecha.length != 10) {
            return emptyList()
        }

        val dia = runCatching {
            LocalDate.parse(fecha)
        }.getOrNull() ?: return emptyList()

        val ahora = LocalDateTime.now()
        val hoy = ahora.toLocalDate()

        if (
            dia.isBefore(hoy) ||
            dia.isAfter(hoy.plusDays(90)) ||
            dia.dayOfWeek == DayOfWeek.SATURDAY ||
            dia.dayOfWeek == DayOfWeek.SUNDAY
        ) {
            return emptyList()
        }

        val ocupados = citas.filter {
            it.medicoId == medicoId &&
                    it.fecha == fecha &&
                    it.estado == "Confirmada"
        }.map {
            it.hora
        }

        return horariosBase.filter { hora ->
            val horarioFuturo =
                dia != hoy || LocalTime.parse(hora).isAfter(ahora.toLocalTime())

            hora !in ocupados && horarioFuturo
        }
    }

    @Synchronized
    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        fecha: String,
        hora: String
    ): Cita? {
        if (
            usuarioActual?.id != usuarioId ||
            usuarios.none { it.id == usuarioId } ||
            obtenerMedico(medicoId) == null
        ) {
            return null
        }

        if (hora !in horariosDisponibles(medicoId, fecha)) {
            return null
        }

        val horarioOcupado = citas.any {
            it.medicoId == medicoId &&
                    it.fecha == fecha &&
                    it.hora == hora &&
                    it.estado == "Confirmada"
        }

        if (horarioOcupado) {
            return null
        }

        val cita = Cita(
            id = siguienteCitaId++,
            usuarioId = usuarioId,
            medicoId = medicoId,
            fecha = fecha,
            hora = hora
        )

        citas.add(cita)
        return cita
    }

    fun citasDelUsuario(usuarioId: Int): List<Cita> =
        citas.filter {
            it.usuarioId == usuarioId &&
                    usuarioActual?.id == usuarioId
        }.sortedWith(
            compareBy<Cita> { it.fecha }.thenBy { it.hora }
        )

    @Synchronized
    fun cancelarCita(citaId: Int): Boolean {
        val cita = citas.find {
            it.id == citaId && it.usuarioId == usuarioActual?.id
        } ?: return false

        return citas.remove(cita)
    }
}