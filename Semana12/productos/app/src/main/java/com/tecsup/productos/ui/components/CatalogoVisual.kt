package com.tecsup.productos.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecsup.productos.domain.model.Item

@Composable
fun PaginaProductos(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter,
        content = content
    )
}

@Composable
fun ColumnaProductos(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    PaginaProductos(modifier) {
        Column(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth()
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
fun CabeceraProductos(
    titulo: String,
    seccion: String,
    accion: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(seccion, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        accion?.invoke()
    }
}

@Composable
fun PortadaSmartphones(titulo: String, subtitulo: String) {
    Box(
        modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp).clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF202E62), Color(0xFF24396B), Color(0xFF365DB0))))
    ) {
        Canvas(Modifier.align(Alignment.CenterEnd).width(112.dp).height(140.dp)) {
            val centro = Offset(size.width * .67f, size.height * .51f)
            repeat(3) { indice ->
                drawCircle(Color.White.copy(alpha = .15f), (30 + 13 * indice).dp.toPx(), centro, style = Stroke(1.dp.toPx()))
            }
        }
        SiluetaTelefono(
            Modifier.align(Alignment.CenterEnd).padding(end = 10.dp).size(87.dp, 115.dp),
            Color(0xFFDCE8FF)
        )
        Column(
            Modifier.align(Alignment.CenterStart).padding(start = 20.dp, top = 22.dp, bottom = 22.dp)
                .fillMaxWidth(.70f),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(titulo, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(subtitulo, color = Color(0xFFE0E8FF), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FotoProducto(producto: Item, modifier: Modifier = Modifier) {
    Box(
        modifier.clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF28334C), Color(0xFF425784)))),
        contentAlignment = Alignment.Center
    ) {
        val url = producto.imagenUrl?.takeIf { it.startsWith("https://", ignoreCase = true) }
        val context = LocalContext.current
        val request = remember(context, url) {
            ImageRequest.Builder(context).data(url).crossfade(240).build()
        }
        var disponible by remember(request) { mutableStateOf(false) }
        if (!disponible) {
            SiluetaTelefono(Modifier.size(48.dp, 60.dp), Color(0xFFAEC2EF))
        }
        if (url != null) {
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Fit,
                onSuccess = { disponible = true },
                onError = { disponible = false }
            )
        }
    }
}

@Composable
private fun SiluetaTelefono(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        val ancho = size.width * .57f
        val alto = size.height * .84f
        val x = (size.width - ancho) / 2
        val y = (size.height - alto) / 2
        val esquina = CornerRadius(ancho * .19f)
        rotate(-17f) {
            drawRoundRect(Color(0xFF182746), Offset(x, y), Size(ancho, alto), esquina)
            drawRoundRect(color, Offset(x, y), Size(ancho, alto), esquina, style = Stroke(1.6.dp.toPx()))
            drawCircle(color, ancho * .065f, Offset(size.width * .5f, y + alto * .13f))
            drawLine(color, Offset(x + ancho * .34f, y + alto * .89f), Offset(x + ancho * .66f, y + alto * .89f), strokeWidth = 1.4.dp.toPx())
        }
    }
}

@Composable
fun TarjetaProducto(
    producto: Item,
    onAbrir: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            TextButton(
                onClick = onAbrir,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FotoProducto(producto, Modifier.size(76.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            producto.subtitulo, color = colors.secondary,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            producto.titulo, color = colors.onSurface,
                            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                            maxLines = 2, overflow = TextOverflow.Ellipsis
                        )
                        Text("Ver detalle", color = colors.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            val etiqueta = if (producto.esFavorito) "Quitar de favoritos" else "Guardar en favoritos"
            FilledTonalIconButton(
                onClick = onFavorito,
                modifier = Modifier.semantics { contentDescription = etiqueta }
            ) {
                AnimatedContent(
                    targetState = producto.esFavorito,
                    transitionSpec = { (fadeIn() + scaleIn(initialScale = .65f)) togetherWith fadeOut() },
                    label = "favorito"
                ) { guardado ->
                    Text(
                        if (guardado) "♥" else "♡",
                        color = if (guardado) colors.primary else colors.onSecondaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clearAndSetSemantics { }
                    )
                }
            }
        }
    }
}

@Composable
fun AvisoCatalogo(mensaje: String, onReintentar: (() -> Unit)? = null) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
            onReintentar?.let { TextButton(onClick = it) { Text("Reintentar") } }
        }
    }
}

@Composable
fun BloqueVacio(titulo: String, mensaje: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(mensaje, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
