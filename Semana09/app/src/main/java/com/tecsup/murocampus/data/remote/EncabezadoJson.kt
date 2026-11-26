package com.tecsup.murocampus.data.remote

import okhttp3.Interceptor
import okhttp3.Response

object EncabezadoJson : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val solicitud = chain.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        return chain.proceed(solicitud)
    }
}
