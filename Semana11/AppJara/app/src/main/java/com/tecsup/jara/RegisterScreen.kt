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
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateBack: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    BackHandler(enabled = isLoading) {}

    PantallaAcademica(acento = VioletaJara, mostrarNombre = true) {
        Text(
            "Crear cuenta",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TintaJara
        )
        Text("Registra tu correo y elige una contraseña.", color = TintaJara)

        CampoAcademico(
            email, { email = it }, "Correo electrónico", acento = VioletaJara
        )
        CampoAcademico(
            password, { password = it }, "Contraseña",
            secreto = true, acento = VioletaJara
        )
        CampoAcademico(
            confirmPassword, { confirmPassword = it }, "Confirmar contraseña",
            secreto = true, acento = VioletaJara
        )

        BotonAcademico(
            texto = if (isLoading) "Registrando…" else "Registrarme",
            acento = VioletaJara,
            habilitado = !isLoading,
            alPulsar = {
                val error = when {
                    email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                        "Completa todos los campos."
                    password != confirmPassword -> "Las contraseñas no coinciden."
                    password.length < 6 -> "Usa al menos 6 caracteres."
                    else -> null
                }

                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    return@BotonAcademico
                }

                isLoading = true
                auth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnCompleteListener { tarea ->
                        isLoading = false
                        if (tarea.isSuccessful) {
                            onRegisterSuccess()
                        } else {
                            Toast.makeText(
                                context,
                                tarea.exception?.message ?: "No se pudo crear la cuenta.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        )

        BotonAcademico(
            texto = "Volver",
            alPulsar = onNavigateBack,
            acento = VioletaJara,
            habilitado = !isLoading,
            secundario = true
        )
    }
}