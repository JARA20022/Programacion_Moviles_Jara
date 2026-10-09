package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.ValidacionDatos
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

    @Synchronized
    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        telefono: String
    ): Usuario? {
        if (
            ValidacionDatos.errorRegistro(
                nombre,
                correo,
                telefono,
                clave
            ) != null
        ) {
            return null
        }

        val correoLimpio = ValidacionDatos.normalizarCorreo(correo)

        if (
            usuarios.any {
                it.correo == correoLimpio ||
                        it.telefono == telefono
            }
        ) {
            return null
        }

        val usuario = Usuario(
            id = siguienteUsuarioId++,
            nombre = ValidacionDatos.normalizarNombre(nombre),
            correo = correoLimpio,
            clave = clave,
            telefono = telefono
        )

        usuarios.add(usuario)
        usuarioActual = usuario
        return usuario
    }

    fun iniciarSesion(correo: String, clave: String): Usuario? {
        if (ValidacionDatos.errorLogin(correo, clave) != null) {
            usuarioActual = null
            return null
        }

        val correoLimpio = ValidacionDatos.normalizarCorreo(correo)

        val usuario = usuarios.find {
            it.correo == correoLimpio &&
                    it.clave == clave
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
            it.id == id &&
                    it.usuarioId == usuarioActual?.id
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
        fecha: String,
        ahora: LocalDateTime = LocalDateTime.now()
    ): List<String> {
        if (fecha.length != 10) {
            return emptyList()
        }

        val dia = runCatching {
            LocalDate.parse(fecha)
        }.getOrNull() ?: return emptyList()

        if (
            obtenerMedico(medicoId) == null ||
            dia.isBefore(ahora.toLocalDate()) ||
            dia.isAfter(ahora.toLocalDate().plusDays(90)) ||
            dia.dayOfWeek == DayOfWeek.SATURDAY ||
            dia.dayOfWeek == DayOfWeek.SUNDAY
        ) {
            return emptyList()
        }

        val ocupados = citas
            .filter {
                it.medicoId == medicoId &&
                        it.fecha == fecha &&
                        it.estado == "Confirmada"
            }
            .map { it.hora }

        return horariosBase.filter { hora ->
            hora !in ocupados &&
                    (
                            dia != ahora.toLocalDate() ||
                                    LocalTime.parse(hora)
                                        .isAfter(ahora.toLocalTime())
                            )
        }
    }

    @Synchronized
    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        fecha: String,
        hora: String,
        ahora: LocalDateTime = LocalDateTime.now()
    ): Cita? {
        if (
            usuarioActual?.id != usuarioId ||
            medicos.none { it.id == medicoId } ||
            hora !in horariosDisponibles(
                medicoId,
                fecha,
                ahora
            )
        ) {
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
            it.id == citaId &&
                    it.usuarioId == usuarioActual?.id
        } ?: return false

        return citas.remove(cita)
    }
}