package com.tecsup.centrotareas.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class TareasRepositoryLocal : TareasRepository {
    override fun progresoTarea(): Flow<Int> = flow {
        Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
        emit(0)
        for (porcentaje in 1..100) {
            delay(40)
            emit(porcentaje)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override suspend fun contarPrimos(hasta: Int, informar: (Int) -> Unit): Int = withContext(Dispatchers.Default) {
        val total = (hasta.toLong() - 1).coerceAtLeast(0L)
        var ultimo = 0
        informar(0)
        var cantidad = 0
        for (n in 2..hasta) {
            ensureActive()
            if (esPrimo(n)) cantidad++
            val porcentaje = ((n - 1L) * 100 / total).toInt()
            if (porcentaje != ultimo) {
                ultimo = porcentaje
                informar(porcentaje)
            }
        }
        cantidad
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var divisor = 2
        while (divisor.toLong() * divisor <= n) {
            if (n % divisor == 0) return false
            divisor++
        }
        return true
    }
}
