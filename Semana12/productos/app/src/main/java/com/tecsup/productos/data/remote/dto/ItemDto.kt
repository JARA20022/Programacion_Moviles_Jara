package com.tecsup.productos.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String? = null,
    val brand: String? = null,
    val thumbnail: String? = null
)

@Serializable
data class ProductosResponseDto(
    val products: List<ItemDto>
)
