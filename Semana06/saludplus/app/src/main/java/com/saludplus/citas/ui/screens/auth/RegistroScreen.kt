package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.ValidacionDatos
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIniciarSesion: () -> Unit,
    onVerTerminos: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmarClave by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nuevo ->
                if (
                    nuevo.length <= 60 &&
                    nuevo.all {
                        it.isLetter() ||
                                it == ' ' ||
                                it == '-' ||
                                it == '\'' ||
                                it == '’'
                    }
                ) {
                    nombre = nuevo
                    error = ""
                }
            },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it.take(254)
                error = ""
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevo ->
                telefono = nuevo
                    .filter { caracter -> caracter in '0'..'9' }
                    .take(9)
                error = ""
            },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it.take(64)
                error = ""
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        OutlinedTextField(
            value = confirmarClave,
            onValueChange = {
                confirmarClave = it.take(64)
                error = ""
            },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Color(0xFFB00020))
        }

        Spacer(Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                error = ValidacionDatos.errorRegistro(
                    nombre,
                    correo,
                    telefono,
                    clave
                ) ?: if (clave != confirmarClave) {
                    "Las contraseñas no coinciden"
                } else {
                    ""
                }

                if (error.isEmpty()) {
                    val usuario = Repositorio.registrarUsuario(
                        nombre = nombre,
                        correo = correo,
                        clave = clave,
                        telefono = telefono
                    )

                    if (usuario != null) {
                        onRegistroExitoso()
                    } else {
                        error = "Ese correo o teléfono ya está registrado"
                    }
                }
            }
        )

        TextButton(onClick = onVerTerminos) {
            Text("Ver términos y condiciones")
        }

        TextButton(onClick = onIniciarSesion) {
            Text("Ya tengo una cuenta")
        }
    }
}