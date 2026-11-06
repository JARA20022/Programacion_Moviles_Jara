package com.tecsup.jara

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    BackHandler(enabled = isLoading) {}

    PantallaAcademica(acento = AzulJara, mostrarNombre = true) {
        Text(
            "Iniciar sesión",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TintaJara
        )
        Text("Ingresa con tu cuenta para acceder a tus cursos.", color = TintaJara)

        CampoAcademico(email, { email = it }, "Correo electrónico")
        CampoAcademico(password, { password = it }, "Contraseña", secreto = true)

        BotonAcademico(
            texto = if (isLoading) "Ingresando…" else "Ingresar",
            habilitado = !isLoading,
            alPulsar = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(
                        context, "Completa el correo y la contraseña.", Toast.LENGTH_SHORT
                    ).show()
                    return@BotonAcademico
                }

                isLoading = true
                auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener { tarea ->
                        isLoading = false
                        if (tarea.isSuccessful) {
                            onLoginSuccess()
                        } else {
                            Toast.makeText(
                                context,
                                tarea.exception?.message ?: "No se pudo iniciar sesión.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        )

        BotonAcademico(
            texto = "Crear cuenta",
            alPulsar = onNavigateToRegister,
            habilitado = !isLoading,
            secundario = true
        )

        Text(
            "Programación Móvil · Laboratorio 11",
            color = TintaJara,
            style = MaterialTheme.typography.bodySmall
        )
    }
}