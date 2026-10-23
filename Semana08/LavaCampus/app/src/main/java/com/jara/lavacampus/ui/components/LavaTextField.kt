package com.jara.lavacampus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LavaTextField(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    esClave: Boolean = false,
    habilitado: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onSiguiente: () -> Unit = {},
    onListo: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        enabled = habilitado,
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        visualTransformation = if (esClave && !visible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onSiguiente() },
            onDone = { onListo() }
        ),
        trailingIcon = if (esClave) {
            {
                TextButton(onClick = { visible = !visible }, enabled = habilitado) {
                    Text(if (visible) "Ocultar" else "Ver")
                }
            }
        } else null
    )
}
