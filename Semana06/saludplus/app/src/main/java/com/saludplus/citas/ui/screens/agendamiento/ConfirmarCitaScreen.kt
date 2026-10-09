package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.BluePrimary
import com.saludplus.citas.ui.theme.BorderLight
import com.saludplus.citas.ui.theme.ErrorRed
import com.saludplus.citas.ui.theme.NavyBlue
import com.saludplus.citas.ui.theme.PastelBlue
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onCitaAgendada: (Int) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val sedeActual = Repositorio.sedeSeleccionada ?: Repositorio.sedes.find { it.id == medico?.sedeId }

    var error by rememberSaveable { mutableStateOf("") }
    var procesando by remember { mutableStateOf(false) }

    val horarioDisponible = hora in Repositorio.horariosDisponibles(medicoId, fecha)

    val fechaEnEspanol = remember(fecha) {
        runCatching {
            LocalDate.parse(fecha)
                .format(
                    DateTimeFormatter.ofPattern(
                        "EEEE d 'de' MMMM yyyy",
                        Locale.forLanguageTag("es-PE")
                    )
                )
                .replace("septiembre", "setiembre", ignoreCase = true)
                .replaceFirstChar { it.titlecase(Locale.forLanguageTag("es-PE")) }
        }.getOrElse { fecha }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = NavyBlue
                )
            }

            Text(
                text = "Confirmar cita",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
        }

        Spacer(Modifier.height(16.dp))

        if (medico == null) {
            Text("No se encontró el médico seleccionado.", color = ErrorRed)
            return@Column
        }

        // Medico Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FotoMedico(
                    medicoId = medico.id,
                    nombre = medico.nombre,
                    tamaño = 64.dp
                )

                Column {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = especialidad?.nombre.orEmpty(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )

                    Text(
                        text = "CMP: ${medico.codigoProfesional}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Cita Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                DatoCita(
                    Icons.Filled.CalendarMonth,
                    "Fecha",
                    fechaEnEspanol
                )

                HorizontalDivider(color = BorderLight)

                DatoCita(
                    Icons.Filled.AccessTime,
                    "Hora de atención",
                    hora
                )

                HorizontalDivider(color = BorderLight)

                DatoCita(
                    Icons.Filled.Place,
                    "Tipo de atención",
                    "Consulta presencial"
                )

                HorizontalDivider(color = BorderLight)

                DatoCita(
                    Icons.Filled.LocationOn,
                    "Sede médica",
                    "${sedeActual?.nombre ?: "Sede principal"} (${sedeActual?.direccion ?: ""})"
                )

                HorizontalDivider(color = BorderLight)

                DatoCita(
                    Icons.Filled.Payments,
                    "Precio de consulta",
                    "S/ ${medico.precioConsulta.toInt()}"
                )
            }
        }

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = error,
                color = ErrorRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        if (!horarioDisponible) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Este horario ya no está disponible. Selecciona otro.",
                color = ErrorRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(28.dp))

        BotonPrincipal(
            texto = "Agendar cita",
            containerColor = BluePrimary,
            enabled = horarioDisponible && !procesando,
            onClick = {
                if (procesando) return@BotonPrincipal

                procesando = true
                val usuarioId = Repositorio.usuarioActual?.id

                if (usuarioId == null) {
                    error = "Inicia sesión para reservar una cita."
                    procesando = false
                } else {
                    val cita = Repositorio.agendarCita(
                        usuarioId = usuarioId,
                        medicoId = medicoId,
                        fecha = fecha,
                        hora = hora
                    )

                    if (cita == null) {
                        error = "Este horario ya no está disponible. Selecciona otro."
                        procesando = false
                    } else {
                        onCitaAgendada(cita.id)
                    }
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DatoCita(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    PastelBlue,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Column {
            Text(
                text = etiqueta,
                fontSize = 11.sp,
                color = TextSecondary
            )

            Text(
                text = valor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
