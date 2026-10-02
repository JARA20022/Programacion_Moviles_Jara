package com.jara.lab06.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jara.lab06.R
import com.jara.lab06.data.favoritosTienda
import com.jara.lab06.data.reportadosTienda
import com.jara.lab06.model.Producto
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun TarjetaProducto(
    producto: Producto,
    onVerDetalle: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    // Cada producto controla la apertura de su propio menú.
    var expanded by remember {
        mutableStateOf(false)
    }

    var mostrarReporte by remember {
        mutableStateOf(false)
    }

    // Mediciones en píxeles de la tarjeta y del botón para cálculos exactos
    var cardHeightPx by remember { mutableIntStateOf(0) }
    var cardWidthPx by remember { mutableIntStateOf(0) }
    var buttonTopInCardPx by remember { mutableFloatStateOf(0f) }
    var buttonLeftInCardPx by remember { mutableFloatStateOf(0f) }
    var buttonHeightPx by remember { mutableIntStateOf(0) }
    var menuHeightPx by remember { mutableIntStateOf(0) }

    val esFavorito = producto.id in favoritosTienda

    // Si el menú se despliega cerca del borde de pantalla, desplaza el ítem para ser visible
    LaunchedEffect(expanded) {
        if (expanded) {
            coroutineScope.launch {
                bringIntoViewRequester.bringIntoView()
            }
        }
    }

    // Confirmación del reporte de práctica.
    if (mostrarReporte) {
        AlertDialog(
            onDismissRequest = {
                mostrarReporte = false
            },
            title = {
                Text("Reportar producto")
            },
            text = {
                Text(
                    "¿Reportar ${producto.nombre}? " +
                            "Este reporte de práctica se guarda solo en memoria."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (producto.id !in reportadosTienda) {
                            reportadosTienda.add(producto.id)
                        }

                        mostrarReporte = false

                        Toast.makeText(
                            context,
                            "Reporte de práctica registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text("Reportar", color = Color(0xFF5B2C83))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarReporte = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 112.dp)
                .clickable { onVerDetalle() }
                .onSizeChanged { size ->
                    cardHeightPx = size.height
                    cardWidthPx = size.width
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF4EFF8)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            border = if (expanded) {
                BorderStroke(2.dp, Color(0xFF5B2C83))
            } else {
                null
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Recuadro lavanda de 64dp con esquinas de 12dp e icono de bolsa de 32dp
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEDE3F4)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shopping_bag),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF5B2C83)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = producto.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF242128)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = String.format(Locale.US, "S/ %.2f", producto.precio),
                        fontSize = 16.sp,
                        color = Color(0xFF665176)
                    )

                    if (esFavorito) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "En favoritos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5B2C83)
                        )
                    }
                }

                // Botón de tres puntos en recuadro/círculo blanco de 32dp con área táctil de 48dp y Box del DropdownMenu
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            val pos = coordinates.positionInParent()
                            buttonTopInCardPx = pos.y
                            buttonLeftInCardPx = pos.x
                        }
                        .onSizeChanged { size ->
                            buttonHeightPx = size.height
                        }
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Opciones de ${producto.nombre}",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color(0xFF242128)
                                )
                            }
                        }

                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.size(48.dp)
                        ) {
                            // El icono se muestra centrado en el Surface
                        }
                    }

                    // 1. Desplazamiento vertical exacto:
                    // DropdownMenu en offset y=0 se abre en el borde inferior del botón (buttonTopInCardPx + buttonHeightPx).
                    // Para situar el menú 8dp por debajo del borde inferior de la tarjeta (cardHeightPx):
                    val buttonBottomInCardPx = buttonTopInCardPx + buttonHeightPx
                    val distanceToBottomPx = (cardHeightPx - buttonBottomInCardPx).coerceAtLeast(0f)
                    val distanceToBottomDp = with(density) { distanceToBottomPx.toDp() }
                    val yOffsetDp = distanceToBottomDp + 8.dp

                    // 2. Desplazamiento horizontal exacto:
                    // Alineación del borde derecho del menú (208dp) con el borde derecho de la tarjeta.
                    val distanceToRightPx = (cardWidthPx - buttonLeftInCardPx).coerceAtLeast(0f)
                    val distanceToRightDp = with(density) { distanceToRightPx.toDp() }
                    val xOffsetDp = distanceToRightDp - 208.dp

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        offset = DpOffset(xOffsetDp, yOffsetDp),
                        modifier = Modifier
                            .width(208.dp)
                            .onSizeChanged { size ->
                                if (size.height > 0 && size.height != menuHeightPx) {
                                    menuHeightPx = size.height
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, Color(0xFFDEDEE2))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (esFavorito) {
                                        "Quitar de favoritos"
                                    } else {
                                        "Favoritos"
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF242128)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (esFavorito) Color(0xFF5B2C83) else Color(0xFF242128)
                                )
                            },
                            modifier = Modifier.heightIn(min = 48.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            onClick = {
                                if (esFavorito) {
                                    favoritosTienda.remove(producto.id)
                                } else {
                                    favoritosTienda.add(producto.id)
                                }
                                expanded = false
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp,
                            color = Color(0xFFDEDEE2)
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Compartir",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF242128)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF242128)
                                )
                            },
                            modifier = Modifier.heightIn(min = 48.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            onClick = {
                                expanded = false

                                val precio = String.format(
                                    Locale.US,
                                    "S/ %.2f",
                                    producto.precio
                                )

                                val compartir = Intent(
                                    Intent.ACTION_SEND
                                ).apply {
                                    type = "text/plain"

                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "${producto.nombre} — $precio\n" +
                                                "Tienda IVAN JARA AYALA"
                                    )
                                }

                                context.startActivity(
                                    Intent.createChooser(
                                        compartir,
                                        "Compartir producto"
                                    )
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp,
                            color = Color(0xFFDEDEE2)
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Reportar",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF242128)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF242128)
                                )
                            },
                            modifier = Modifier.heightIn(min = 48.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            onClick = {
                                expanded = false
                                mostrarReporte = true
                            }
                        )
                    }
                }
            }
        }

        // Espacio reservado temporal debajo de la tarjeta cuando el menú está abierto:
        // Requerimiento: 8dp (arriba) + altura real del menú + 16dp (abajo).
        // Dado que LazyColumn ya aplica 16dp de separación entre elementos, restamos los 16dp de LazyColumn:
        // Altura del Spacer = (8dp + menuHeightDp + 16dp) - 16dp = menuHeightDp + 8dp.
        if (expanded) {
            val menuHeightDp = with(density) { menuHeightPx.toDp() }
            val spacerHeightDp = if (menuHeightDp > 0.dp) menuHeightDp + 8.dp else 170.dp
            Spacer(modifier = Modifier.height(spacerHeightDp))
        }
    }
}
