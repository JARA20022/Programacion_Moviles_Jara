package com.tecsup.avisoscampus.data

import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.flow.*
interface AvisosRepository {
 val avisos: StateFlow<List<Aviso>>
 suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}
class AvisosRepositoryMemoria : AvisosRepository {
 private val lista = MutableStateFlow<List<Aviso>>(emptyList())
 override val avisos = lista.asStateFlow()
 private var siguiente = 1
 override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
  val aviso = Aviso(siguiente++, titulo, texto, origen)
  lista.value = listOf(aviso) + lista.value
  return aviso
 }
}
