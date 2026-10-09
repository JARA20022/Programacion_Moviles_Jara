package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.BackgroundMain
import com.saludplus.citas.ui.theme.CardSurface
import com.saludplus.citas.ui.theme.ErrorRed
import com.saludplus.citas.ui.theme.PetroleumPrimary
import com.saludplus.citas.ui.theme.PetroleumVariant
import com.saludplus.citas.ui.theme.SurfaceWhite
import com.saludplus.citas.ui.theme.TextPrimary
import com.saludplus.citas.ui.theme.TextSecondary

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
        focusedBorderColor = PetroleumPrimary,
        focusedLeadingIconColor = PetroleumPrimary,
        focusedLabelColor = PetroleumPrimary,
        unfocusedContainerColor = SurfaceWhite,
        focusedContainerColor = SurfaceWhite
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Regístrate para agendar tus citas médicas",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
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
                        } else {
                            error = "En el nombre usa solo letras y un máximo de 60 caracteres."
                        }
                    },
                    label = { Text("Nombre completo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                    label = { Text("Teléfono celular") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                    label = { Text("Correo electrónico") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
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
                    label = { Text("Confirmar contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = coloresCampo,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = error,
                        color = ErrorRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(20.dp))

                BotonPrincipal(
                    texto = "Registrarme",
                    containerColor = PetroleumPrimary,
                    onClick = {
                        val errorValidacion = Repositorio.errorRegistro(
                            nombre,
                            correo,
                            telefono,
                            clave
                        ) ?: if (clave != confirmarClave) {
                            "Las contraseñas no coinciden."
                        } else {
                            null
                        }

                        if (errorValidacion != null) {
                            error = errorValidacion
                        } else {
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

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Al registrarte aceptas nuestros ",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    TextButton(
                        onClick = onVerTerminos,
                        modifier = Modifier.padding(start = 0.dp)
                    ) {
                        Text(
                            text = "Términos",
                            color = PetroleumVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta?",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )

                    TextButton(onClick = onIniciarSesion) {
                        Text(
                            text = "Iniciar sesión",
                            color = PetroleumVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
