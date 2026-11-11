package com.tecsup.jara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.FirebaseApp

internal val AzulJara = Color(0xFF1749DB)
internal val VioletaJara = Color(0xFF5A37CD)
internal val TurquesaJara = Color(0xFF007D84)
internal val TintaJara = Color(0xFF10214A)
internal val FondoJara = Color(0xFFF0F6FE)
internal val FormaJara = RoundedCornerShape(14.dp)

private val ColoresJara = lightColorScheme(
    primary = AzulJara,
    onPrimary = Color.White,
    background = FondoJara,
    surface = Color.White,
    onSurface = TintaJara,
    onBackground = TintaJara,
    outline = Color(0xFF879AB6)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContent {
            MaterialTheme(colorScheme = ColoresJara) {
                AuthApp()
            }
        }
    }
}

@Composable
internal fun CabeceraAcademica(
    acento: Color,
    titulo: String,
    mostrarNombre: Boolean = false
) {
    Box(
        Modifier.fillMaxWidth()
            .background(Brush.linearGradient(listOf(acento, TintaJara)))
    ) {
        Canvas(Modifier.matchParentSize()) {
            val paso = 12.dp.toPx()

            for (fila in 0..4) {
                for (columna in 0..5) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.26f),
                        radius = 1.3.dp.toPx(),
                        center = Offset(
                            18.dp.toPx() + columna * paso,
                            18.dp.toPx() + fila * paso
                        )
                    )
                }
            }

            for (i in 0..3) {
                val desplazamiento = i * 16.dp.toPx()
                drawLine(
                    color = Color.White.copy(alpha = 0.18f),
                    start = Offset(size.width - 76.dp.toPx() + desplazamiento, size.height),
                    end = Offset(size.width + desplazamiento, size.height - 88.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (mostrarNombre) {
                Text(
                    "Iván Jara Ayala",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Image(
                painter = painterResource(R.drawable.tecsup),
                contentDescription = "Logo Tecsup",
                modifier = Modifier.width(166.dp).height(57.dp)
            )

            if (titulo.isNotBlank()) {
                Text(
                    titulo,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
internal fun PantallaAcademica(
    acento: Color,
    tituloCabecera: String = "",
    mostrarNombre: Boolean = false,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier.fillMaxSize()
            .background(acento)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraAcademica(acento, tituloCabecera, mostrarNombre)

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(FondoJara)
                .padding(horizontal = 24.dp, vertical = 26.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = contenido
        )
    }
}

@Composable
internal fun CampoAcademico(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    secreto: Boolean = false,
    acento: Color = AzulJara
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        singleLine = true,
        shape = FormaJara,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (secreto && !visible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (secreto) {
            {
                TextButton(onClick = { visible = !visible }) {
                    Text(if (visible) "Ocultar" else "Ver", color = acento)
                }
            }
        } else {
            null
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = acento,
            focusedLabelColor = acento,
            cursorColor = acento,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
internal fun BotonAcademico(
    texto: String,
    alPulsar: () -> Unit,
    acento: Color = AzulJara,
    habilitado: Boolean = true,
    secundario: Boolean = false
) {
    val modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)

    if (secundario) {
        OutlinedButton(
            onClick = alPulsar,
            enabled = habilitado,
            modifier = modifier,
            shape = FormaJara,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = acento)
        ) {
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Button(
            onClick = alPulsar,
            enabled = habilitado,
            modifier = modifier,
            shape = FormaJara,
            colors = ButtonDefaults.buttonColors(
                containerColor = acento,
                contentColor = Color.White
            )
        ) {
            Text(texto, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
internal fun LibrosJara(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val colores = listOf(AzulJara, VioletaJara, TurquesaJara)

        colores.forEachIndexed { i, color ->
            val x = size.width * (0.12f + i * 0.025f)
            val y = size.height * (0.14f + i * 0.24f)
            val ancho = size.width * 0.73f
            val alto = size.height * 0.21f
            val radio = CornerRadius(alto * 0.27f)

            drawRoundRect(color, Offset(x, y), Size(ancho, alto), radio)
            drawRoundRect(
                color = Color(0xFFE9F4FF),
                topLeft = Offset(x + ancho * 0.18f, y + alto * 0.24f),
                size = Size(ancho * 0.80f, alto * 0.52f),
                cornerRadius = CornerRadius(alto * 0.13f)
            )
        }
    }
}