package com.tecsup.productos.data.mapper

import com.tecsup.productos.data.local.ItemEntity
import com.tecsup.productos.data.remote.dto.ItemDto
import com.tecsup.productos.domain.model.Item

internal fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = title,
    subtitulo = brand?.takeIf { it.isNotBlank() } ?: "Smartphone",
    imagenUrl = thumbnail,
    esFavorito = esFavorito,
    descripcion = description,
    precio = price
)

internal fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    descripcion = descripcion,
    precio = precio
)

internal fun ItemDto.aItem(esFavorito: Boolean = false): Item =
    aEntity(esFavorito).aModelo()
