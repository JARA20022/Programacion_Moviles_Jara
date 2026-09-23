package com.jara.clinicasalud.data

import com.jara.clinicasalud.model.Cita
import com.jara.clinicasalud.model.Medico

// Centraliza los datos de ejemplo, como el repositorio del Lab-05.
// No guarda el estado de la interfaz ni utiliza ViewModel o base de datos.
object DatosClinica {
    val medicos = listOf(
        Medico(
            id = 1,
            nombre = "Dra. Ana Torres",
            categoria = "Cardiología",
            especialidad = "Cardióloga",
            calificacion = "4.9",
            experiencia = "12 años exp.",
            resenas = 128,
            descripcion = "Especialista en arritmias e hipertensión, " +
                    "formación en la Clínica Mayo."
        ),
        Medico(
            id = 2,
            nombre = "Dr. Luis Vega",
            categoria = "Pediatría",
            especialidad = "Pediatra",
            calificacion = "4.7"
        ),
        Medico(
            id = 3,
            nombre = "Dra. Rosa Díaz",
            categoria = "Dermatología",
            especialidad = "Dermatóloga",
            calificacion = "4.8"
        )
    )

    // Cita completada que aparece en la referencia del Word.
    // Se usa al inicializar el estado de la sesión; las nuevas citas se
    // añaden en NavegacionClinica con remember y mutableStateOf.
    fun citasIniciales(): List<Cita> = listOf(
        Cita(
            id = 1,
            medico = medicos.first { it.id == 2 },
            fecha = "Miércoles 15",
            hora = "3:00 pm",
            estado = "Completada"
        )
    )
}
