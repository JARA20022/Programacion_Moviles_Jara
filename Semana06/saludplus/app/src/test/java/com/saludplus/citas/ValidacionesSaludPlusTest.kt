package com.saludplus.citas

import com.saludplus.citas.data.ValidacionDatos
import com.saludplus.citas.data.repository.Repositorio
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionesSaludPlusTest {

    @Test
    fun registroRechazaDatosInvalidos() {
        fun error(
            nombre: String,
            correo: String,
            telefono: String,
            clave: String = "Clave123"
        ) = ValidacionDatos.errorRegistro(
            nombre,
            correo,
            telefono,
            clave
        )

        assertNull(
            error(
                "Iván Jara Ayala",
                "ivan@ejemplo.com",
                "912345678"
            )
        )

        assertNotNull(
            error(
                "Iván123 Jara",
                "ivan@ejemplo.com",
                "912345678"
            )
        )

        assertNotNull(
            error(
                "Ana 😊",
                "ivan@ejemplo.com",
                "912345678"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@ejemplo.com",
                "91234-678"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@ejemplo.com",
                "91234567899"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@ejemplo.com",
                "91234567A"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@ejemplo.com",
                "812345678"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@@ejemplo.com",
                "912345678"
            )
        )

        assertNotNull(
            error(
                "Iván Jara",
                "ivan@ejemplo.com",
                "912345678",
                "123456"
            )
        )
    }

    @Test
    fun reservaRechazaFechaYHorarioInvalidos() {
        val ahora = LocalDateTime.of(
            2026,
            10,
            8,
            7,
            30
        )

        val usuario = Repositorio.registrarUsuario(
            "Iván Jara",
            "ivan@ejemplo.com",
            "Clave123",
            "912345678"
        )!!

        assertNull(
            Repositorio.agendarCita(
                usuario.id,
                1,
                "2026-10-07",
                "08:00",
                ahora
            )
        )

        assertNull(
            Repositorio.agendarCita(
                usuario.id,
                1,
                "2026-10-10",
                "08:00",
                ahora
            )
        )

        assertNull(
            Repositorio.agendarCita(
                usuario.id,
                1,
                "cualquier cosa",
                "08:00",
                ahora
            )
        )

        assertNull(
            Repositorio.agendarCita(
                usuario.id,
                1,
                "2026-10-08",
                "25:00",
                ahora
            )
        )

        val cita = Repositorio.agendarCita(
            usuario.id,
            1,
            "2026-10-08",
            "08:00",
            ahora
        )

        assertNotNull(cita)

        assertNull(
            Repositorio.agendarCita(
                usuario.id,
                1,
                "2026-10-08",
                "08:00",
                ahora
            )
        )

        assertFalse(
            "08:00" in Repositorio.horariosDisponibles(
                1,
                "2026-10-08",
                ahora
            )
        )

        assertEquals(
            cita?.id,
            Repositorio.obtenerCita(cita!!.id)?.id
        )
    }

    @Test
    fun unaCuentaNoPuedeConsultarCitasDeOtra() {
        val ahora = LocalDateTime.of(
            2026,
            10,
            8,
            7,
            30
        )

        val primero = Repositorio.registrarUsuario(
            "Primera Persona",
            "primera@ejemplo.com",
            "Clave123",
            "923456789"
        )!!

        val cita = Repositorio.agendarCita(
            primero.id,
            1,
            "2026-10-08",
            "09:00",
            ahora
        )!!

        val segundo = Repositorio.registrarUsuario(
            "Segunda Persona",
            "segunda@ejemplo.com",
            "Clave123",
            "934567890"
        )!!

        assertTrue(
            Repositorio.citasDelUsuario(segundo.id).isEmpty()
        )

        assertTrue(
            Repositorio.citasDelUsuario(primero.id).isEmpty()
        )

        assertNull(
            Repositorio.obtenerCita(cita.id)
        )

        assertFalse(
            Repositorio.cancelarCita(cita.id)
        )
    }
}