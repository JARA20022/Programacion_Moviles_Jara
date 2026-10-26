package com.jara.lavacampus.model
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.distinctUntilChanged
object RelojCampus {
    fun hoy() = flow { while (true) { emit(LocalDate.now().toString()); delay(60_000) } }.distinctUntilChanged()
}
