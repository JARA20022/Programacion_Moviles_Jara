package com.tecsup.jara

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

data class Curso(val id: String, val nombre: String)

@Composable
fun CursosScreen(uid: String, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val coleccion = remember(uid) {
        FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid).collection("cursos")
    }

    var cursos by remember(uid) { mutableStateOf(emptyList<Curso>()) }
    var nombre by remember(uid) { mutableStateOf("") }
    var cursoEnEdicion by remember(uid) { mutableStateOf<Curso?>(null) }
    var cargando by remember(uid) { mutableStateOf(true) }
    var errorLectura by remember(uid) { mutableStateOf<String?>(null) }

    DisposableEffect(coleccion) {
        val escucha = coleccion.addSnapshotListener { resultado, error ->
            cargando = false
            errorLectura = error?.let { "Error al leer los cursos: ${it.message}" }

            if (resultado != null) {
                cursos = resultado.documents.mapNotNull { documento ->
                    documento.getString("nombre")?.let { Curso(documento.id, it) }
                }
            }
        }
        onDispose { escucha.remove() }
    }

    Box(
        Modifier.fillMaxSize()
            .background(AzulJara)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        LazyColumn(Modifier.fillMaxSize().background(FondoJara)) {
            item {
                CabeceraAcademica(AzulJara, "Mis Cursos")
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(onClick = onNavigateBack) { Text("← Volver") }

                    CampoAcademico(
                        nombre, { nombre = it }, "Nombre del curso", acento = TurquesaJara
                    )

                    BotonAcademico(
                        texto = if (cursoEnEdicion == null) "Registrar curso" else "Guardar cambios",
                        acento = TurquesaJara,
                        habilitado = nombre.isNotBlank(),
                        alPulsar = {
                            val nombreCurso = nombre.trim()
                            if (nombreCurso.isEmpty()) return@BotonAcademico

                            val curso = cursoEnEdicion
                            nombre = ""
                            cursoEnEdicion = null

                            val operacion = if (curso == null) {
                                coleccion.document().set(mapOf("nombre" to nombreCurso))
                            } else {
                                coleccion.document(curso.id).update("nombre", nombreCurso)
                            }

                            operacion.addOnFailureListener { error ->
                                Toast.makeText(
                                    context,
                                    "No se pudo guardar $nombreCurso: ${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )

                    if (cursoEnEdicion != null) {
                        BotonAcademico(
                            texto = "Cancelar edición",
                            secundario = true,
                            alPulsar = {
                                cursoEnEdicion = null
                                nombre = ""
                            }
                        )
                    }

                    when {
                        cargando -> Text("Cargando cursos…")
                        errorLectura != null -> Text(errorLectura.orEmpty())
                        cursos.isEmpty() -> Text("Aún no tienes cursos registrados.")
                        else -> Text(
                            "Cursos registrados",
                            color = TintaJara,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(cursos, key = { it.id }) { curso ->
                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    shape = FormaJara,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFD2DEEF))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(46.dp)
                                .clip(FormaJara)
                                .background(Color(0xFFE7F0FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            LibrosJara(Modifier.size(34.dp))
                        }

                        Column(Modifier.weight(1f)) {
                            Text(
                                curso.nombre,
                                color = TintaJara,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                TextButton(
                                    onClick = {
                                        cursoEnEdicion = curso
                                        nombre = curso.nombre
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = Color(0xFFE7F0FF),
                                        contentColor = AzulJara
                                    ),
                                    shape = FormaJara,
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {
                                        if (cursoEnEdicion?.id == curso.id) {
                                            cursoEnEdicion = null
                                            nombre = ""
                                        }

                                        coleccion.document(curso.id).delete()
                                            .addOnFailureListener { error ->
                                                Toast.makeText(
                                                    context,
                                                    "No se pudo eliminar ${curso.nombre}: ${error.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = Color(0xFFFFE4E1),
                                        contentColor = Color(0xFFA52626)
                                    ),
                                    shape = FormaJara,
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}