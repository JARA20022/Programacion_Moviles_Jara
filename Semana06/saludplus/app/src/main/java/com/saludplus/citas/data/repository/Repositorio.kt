package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.HorarioMedico
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.ResultadoLaboratorio
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Repositorio {

    val especialidades = listOf(
        Especialidad(1, "Medicina general", "Atención y evaluación médica integral"),
        Especialidad(2, "Pediatría", "Atención médica especializada para niños y adolescentes"),
        Especialidad(3, "Ginecología", "Cuidado integral de la salud de la mujer"),
        Especialidad(4, "Cardiología", "Prevención, diagnóstico y tratamiento del corazón"),
        Especialidad(5, "Dermatología", "Cuidado integral de la piel, cabello y uñas"),
        Especialidad(6, "Traumatología", "Atención de lesiones en huesos y articulaciones"),
        Especialidad(7, "Odontología", "Salud bucal, prevención y tratamiento dental")
    )

    val sedes = listOf(
        Sede(
            1,
            "Sede San Juan de Lurigancho",
            "Av. Próceres de la Independencia 1500",
            "San Juan de Lurigancho",
            "(01) 555-1001",
            "Lun-Dom 8:00 a.m. - 8:00 p.m."
        ),
        Sede(
            2,
            "Sede Independencia",
            "Av. Túpac Amaru 2200",
            "Independencia",
            "(01) 555-1002",
            "Lun-Dom 8:00 a.m. - 8:00 p.m."
        ),
        Sede(
            3,
            "Sede Miraflores",
            "Av. Arequipa 4500",
            "Miraflores",
            "(01) 555-1003",
            "Lun-Dom 8:00 a.m. - 8:00 p.m."
        ),
        Sede(
            4,
            "Sede Los Olivos",
            "Av. Carlos Izaguirre 900",
            "Los Olivos",
            "(01) 555-1004",
            "Lun-Sáb 8:00 a.m. - 8:00 p.m."
        )
    )

    var sedeSeleccionada by mutableStateOf<Sede?>(null)

    val medicos = listOf(
        Medico(
            id = 1,
            nombre = "Dra. Ana Torres",
            especialidadId = 1,
            experiencia = 8,
            calificacion = 4.9,
            precioConsulta = 80.0,
            codigoProfesional = "CMP-1001",
            sedeId = 1,
            telefono = "987654321",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                HorarioMedico(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0))
            )
        ),
        Medico(
            id = 2,
            nombre = "Dr. Luis Ramírez",
            especialidadId = 1,
            experiencia = 6,
            calificacion = 4.7,
            precioConsulta = 75.0,
            codigoProfesional = "CMP-1002",
            sedeId = 1,
            telefono = "987654322",
            horarios = listOf(
                HorarioMedico(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                HorarioMedico(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(18, 0))
            )
        ),
        Medico(
            id = 3,
            nombre = "Dra. María Flores",
            especialidadId = 2,
            experiencia = 10,
            calificacion = 4.9,
            precioConsulta = 90.0,
            codigoProfesional = "CMP-2001",
            sedeId = 1,
            telefono = "987654323",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                HorarioMedico(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                HorarioMedico(DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(13, 0), emergencia = true)
            )
        ),
        Medico(
            id = 4,
            nombre = "Dra. Carmen Ugarte",
            especialidadId = 3,
            experiencia = 11,
            calificacion = 4.8,
            precioConsulta = 110.0,
            codigoProfesional = "CMP-3005",
            sedeId = 1,
            telefono = "987654331",
            horarios = listOf(
                HorarioMedico(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                HorarioMedico(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(18, 0))
            )
        ),
        Medico(
            id = 5,
            nombre = "Dr. Carlos Mendoza",
            especialidadId = 4,
            experiencia = 12,
            calificacion = 4.8,
            precioConsulta = 120.0,
            codigoProfesional = "CMP-4001",
            sedeId = 2,
            telefono = "987654324",
            horarios = listOf(
                HorarioMedico(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                HorarioMedico(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(12, 0))
            )
        ),
        Medico(
            id = 6,
            nombre = "Dra. Patricia Rojas",
            especialidadId = 5,
            experiencia = 7,
            calificacion = 4.8,
            precioConsulta = 95.0,
            codigoProfesional = "CMP-5001",
            sedeId = 2,
            telefono = "987654325",
            horarios = listOf(
                HorarioMedico(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                HorarioMedico(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                HorarioMedico(DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(14, 0), emergencia = true)
            )
        ),
        Medico(
            id = 7,
            nombre = "Dr. Jorge Castillo",
            especialidadId = 6,
            experiencia = 9,
            calificacion = 4.7,
            precioConsulta = 100.0,
            codigoProfesional = "CMP-6001",
            sedeId = 3,
            telefono = "987654326",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                HorarioMedico(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0))
            )
        ),
        Medico(
            id = 8,
            nombre = "Dra. Elena Morales",
            especialidadId = 1,
            experiencia = 11,
            calificacion = 4.9,
            precioConsulta = 85.0,
            codigoProfesional = "CMP-1003",
            sedeId = 3,
            telefono = "987654327",
            horarios = listOf(
                HorarioMedico(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                HorarioMedico(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                HorarioMedico(DayOfWeek.SUNDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), emergencia = true)
            )
        ),
        Medico(
            id = 9,
            nombre = "Dr. Roberto Gómez",
            especialidadId = 2,
            experiencia = 8,
            calificacion = 4.6,
            precioConsulta = 85.0,
            codigoProfesional = "CMP-2002",
            sedeId = 2,
            telefono = "987654328",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(14, 0)),
                HorarioMedico(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(14, 0))
            )
        ),
        Medico(
            id = 10,
            nombre = "Dra. Sofía Vargas",
            especialidadId = 4,
            experiencia = 14,
            calificacion = 5.0,
            precioConsulta = 130.0,
            codigoProfesional = "CMP-4002",
            sedeId = 4,
            telefono = "987654329",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                HorarioMedico(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(12, 0))
            )
        ),
        Medico(
            id = 11,
            nombre = "Dr. Miguel Ángel",
            especialidadId = 5,
            experiencia = 9,
            calificacion = 4.8,
            precioConsulta = 100.0,
            codigoProfesional = "CMP-5002",
            sedeId = 4,
            telefono = "987654330",
            horarios = listOf(
                HorarioMedico(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0)),
                HorarioMedico(DayOfWeek.SATURDAY, LocalTime.of(8, 0), LocalTime.of(14, 0))
            )
        ),
        Medico(
            id = 12,
            nombre = "Dra. Lucía Paredes",
            especialidadId = 7,
            experiencia = 10,
            calificacion = 4.9,
            precioConsulta = 95.0,
            codigoProfesional = "COP-7001",
            sedeId = 3,
            telefono = "987654332",
            horarios = listOf(
                HorarioMedico(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                HorarioMedico(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(13, 0))
            )
        )
    )

    private val resultadosLaboratorio = listOf(
        ResultadoLaboratorio(
            id = 1,
            usuarioId = 0,
            examen = "Hemograma completo",
            fecha = "10/05/2025",
            estado = "Disponible",
            resultado = "14.2",
            unidad = "g/dL",
            referencia = "12.0 - 16.0"
        ),
        ResultadoLaboratorio(
            id = 2,
            usuarioId = 0,
            examen = "Glucosa en sangre",
            fecha = "10/05/2025",
            estado = "Disponible",
            resultado = "92",
            unidad = "mg/dL",
            referencia = "70 - 100"
        ),
        ResultadoLaboratorio(
            id = 3,
            usuarioId = 0,
            examen = "Perfil lipídico (Colesterol total)",
            fecha = "12/05/2025",
            estado = "Disponible",
            resultado = "185",
            unidad = "mg/dL",
            referencia = "< 200"
        ),
        ResultadoLaboratorio(
            id = 4,
            usuarioId = 0,
            examen = "Examen completo de orina",
            fecha = "18/05/2025",
            estado = "Pendiente",
            resultado = "-",
            unidad = "-",
            referencia = "Normal"
        )
    )

    private val usuarios = mutableStateListOf<Usuario>()
    private val citas = mutableStateListOf<Cita>()

    private var siguienteUsuarioId = 1
    private var siguienteCitaId = 1

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    // Validaciones

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
        especialidades

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
            it.especialidadId == especialidadId &&
                    (sedeSeleccionada == null || it.sedeId == sedeSeleccionada?.id)
        }.sortedByDescending {
            it.calificacion
        }

    fun buscarMedicos(texto: String): List<Medico> =
        medicos.filter {
            it.nombre.contains(texto.trim(), ignoreCase = true) &&
                    (sedeSeleccionada == null || it.sedeId == sedeSeleccionada?.id)
        }.sortedByDescending {
            it.calificacion
        }

    fun medicosPorSede(sedeId: Int): List<Medico> =
        medicos.filter { it.sedeId == sedeId }

    fun resultadosDelUsuario(usuarioId: Int): List<ResultadoLaboratorio> =
        resultadosLaboratorio.filter {
            it.usuarioId == usuarioId || it.usuarioId == 0
        }

    fun horariosDisponibles(
        medicoId: Int,
        fecha: String,
        ahoraOpcional: LocalDateTime? = null
    ): List<String> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()

        if (fecha.length != 10) return emptyList()

        // Si hay sede seleccionada, verificar pertenencia del médico a la sede
        if (sedeSeleccionada != null && medico.sedeId != sedeSeleccionada?.id) {
            return emptyList()
        }

        val dia = runCatching {
            LocalDate.parse(fecha)
        }.getOrNull() ?: return emptyList()

        val ahora = ahoraOpcional ?: LocalDateTime.now()
        val hoy = ahora.toLocalDate()

        if (dia.isBefore(hoy) || dia.isAfter(hoy.plusDays(90))) {
            return emptyList()
        }

        val horarioMedico = medico.horarios.find { it.dia == dia.dayOfWeek }
            ?: return emptyList()

        val franjas = mutableListOf<String>()
        var actual = horarioMedico.horaInicio
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm")

        while (actual.isBefore(horarioMedico.horaFin)) {
            val horaTexto = actual.format(formatoHora)
            val horarioFuturo = dia != hoy || actual.isAfter(ahora.toLocalTime())

            val ocupado = citas.any {
                it.medicoId == medicoId &&
                        it.fecha == fecha &&
                        it.hora == horaTexto &&
                        it.estado == "Confirmada"
            }

            if (horarioFuturo && !ocupado) {
                franjas.add(horaTexto)
            }
            actual = actual.plusHours(1)
        }

        return franjas
    }

    @Synchronized
    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        fecha: String,
        hora: String
    ): Cita? {
        val usuarioSesion = usuarioActual
        if (
            usuarioSesion == null ||
            usuarioSesion.id != usuarioId ||
            usuarios.none { it.id == usuarioId }
        ) {
            return null
        }

        val medico = obtenerMedico(medicoId) ?: return null

        if (sedeSeleccionada != null && medico.sedeId != sedeSeleccionada?.id) {
            return null
        }

        val disponibles = horariosDisponibles(medicoId, fecha)
        if (hora !in disponibles) {
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
            hora = hora,
            estado = "Confirmada",
            sedeId = sedeSeleccionada?.id ?: medico.sedeId
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
