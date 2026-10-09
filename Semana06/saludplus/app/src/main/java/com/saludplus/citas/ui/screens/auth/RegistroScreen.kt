package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Icon
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
    var confirmarClave by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = azulRegistro,
        focusedLeadingIconColor = azulRegistro
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 22.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(22.dp))

        Text(
            text = "Crear cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

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
                } else {
                    error = "En el nombre usa solo letras y un máximo de 60 caracteres."
                }
            },
            label = {
                Text("Nombre completo")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevo ->
                telefono = nuevo
                    .filter { it in '0'..'9' }
                    .take(9)
                error = ""
            },
            label = {
                Text("Teléfono")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it.take(254)
                error = ""
            },
            label = {
                Text("Correo electrónico")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
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
            onValueChange = {
                clave = it.take(64)
                error = ""
            },
            label = {
                Text("Contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = coloresCampo,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmarClave,
            onValueChange = {
                confirmarClave = it.take(64)
                error = ""
            },
            label = {
                Text("Confirmar contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
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
            Text(
                text = error,
                color = Color(0xFFB00020)
            )
        }

        Spacer(Modifier.height(22.dp))

        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                error = Repositorio.errorRegistro(
                    nombre,
                    correo,
                    telefono,
                    clave
                ) ?: if (clave != confirmarClave) {
                    "Las contraseñas no coinciden."
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
                        error = "El correo o celular ya está registrado."
                    }
                }
            }
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Al registrarte aceptas nuestros",
            fontSize = 12.sp,
            color = Color(0xFF65728A)
        )

        TextButton(onClick = onVerTerminos) {
            Text(
                text = "Términos y Condiciones",
                color = azulRegistro
            )
        }

        Spacer(Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Ya tienes cuenta?",
                fontSize = 13.sp
            )

            TextButton(onClick = onIniciarSesion) {
                Text(
                    text = "Iniciar sesión",
                    color = azulRegistro
                )
            }
        }
    }
}