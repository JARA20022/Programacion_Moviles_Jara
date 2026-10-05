package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal

private val azulRegistro = Color(0xFF2864E8)

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
    var error by rememberSaveable { mutableStateOf("") }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = azulRegistro,
        focusedLeadingIconColor = azulRegistro
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(22.dp))
        Text("Crear cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Regístrate para agendar tus citas",
            fontSize = 14.sp,
            color = Color(0xFF65728A),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(27.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Person, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it.filter(Char::isDigit).take(9) },
            label = { Text("Teléfono") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Phone, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Email, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Filled.Lock, null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Color(0xFFB00020))
        }

        Spacer(Modifier.height(22.dp))

        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                error = when {
                    nombre.trim().length < 3 -> "Escribe tu nombre completo"
                    telefono.length != 9 -> "Escribe un teléfono de 9 dígitos"
                    !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() ->
                        "Escribe un correo válido"
                    clave.length < 6 ->
                        "La contraseña debe tener al menos 6 caracteres"
                    else -> ""
                }

                if (error.isEmpty()) {
                    val usuario = Repositorio.registrarUsuario(
                        nombre = nombre.trim(),
                        correo = correo.trim(),
                        clave = clave,
                        telefono = telefono
                    )
                    if (usuario != null) {
                        onRegistroExitoso()
                    } else {
                        error = "Ese correo ya está registrado"
                    }
                }
            }
        )

        Spacer(Modifier.height(4.dp))
        Text("Al registrarte aceptas nuestros", fontSize = 12.sp,
            color = Color(0xFF65728A))
        TextButton(onClick = onVerTerminos) {
            Text("Términos y Condiciones", color = azulRegistro)
        }

        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text("¿Ya tienes cuenta?", fontSize = 13.sp)
            TextButton(onClick = onIniciarSesion) {
                Text("Iniciar sesión", color = azulRegistro)
            }
        }
    }
}
