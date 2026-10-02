package com.jara.lab06.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jara.lab06.data.favoritosTienda
import com.jara.lab06.data.reportadosTienda
import com.jara.lab06.model.Producto
import java.util.Locale

@Composable
fun TarjetaProducto(
    producto: Producto,
    onVerDetalle: () -> Unit
) {
    val context = LocalContext.current

    // Cada tarjeta abre y cierra su propio menú.
    var expanded by remember {
        mutableStateOf(false)
    }

    var mostrarReporte by remember {
        mutableStateOf(false)
    }

    val esFavorito = producto.id in favoritosTienda

    // Pedimos confirmación antes de registrar el reporte de práctica.
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
                    Text("Reportar")
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = producto.categoria,
                        color = Color.Gray
                    )

                    if (esFavorito) {
                        Text(
                            text = "En favoritos",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // El menú queda junto al botón que lo abre.
                Box {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription =
                                "Opciones de ${producto.nombre}"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (esFavorito) {
                                        "Quitar de favoritos"
                                    } else {
                                        "Favoritos"
                                    }
                                )
                            },
                            onClick = {
                                if (esFavorito) {
                                    favoritosTienda.remove(producto.id)
                                } else {
                                    favoritosTienda.add(producto.id)
                                }

                                expanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Compartir")
                            },
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

                        DropdownMenuItem(
                            text = {
                                Text("Reportar")
                            },
                            onClick = {
                                expanded = false
                                mostrarReporte = true
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "S/ %.2f",
                        producto.precio
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onVerDetalle) {
                    Text("Ver producto")
                }
            }
        }
    }
}