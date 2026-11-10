package com.tecsup.productos.data.remote

import com.tecsup.productos.data.remote.dto.ItemDto
import com.tecsup.productos.data.remote.dto.ProductosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products/category/{slug}")
    suspend fun listar(@Path("slug") slug: String = "smartphones"): ProductosResponseDto

    @GET("products/{id}")
    suspend fun detalle(@Path("id") id: Int): ItemDto
}
