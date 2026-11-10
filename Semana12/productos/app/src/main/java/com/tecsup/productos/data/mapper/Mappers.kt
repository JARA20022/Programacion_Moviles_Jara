package com.tecsup.productos.data.mapper

import com.tecsup.productos.data.remote.dto.ItemDto
import com.tecsup.productos.domain.model.Item

internal fun ItemDto.aItem(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    subtitulo = brand?.takeIf { it.isNotBlank() } ?: "Smartphone",
    imagenUrl = thumbnail,
    esFavorito = esFavorito,
    descripcion = description,
    precio = price
)
