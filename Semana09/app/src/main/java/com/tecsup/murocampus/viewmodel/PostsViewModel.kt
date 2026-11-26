package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private var solicitudLista: Job? = null

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    init { cargar() }

    fun cargar() {
        solicitudLista?.cancel()
        solicitudLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = _usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar()
            resultado.onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private var solicitudDetalle: Job? = null

    private var detalleId: Int? = null

    fun asegurarDetalle(id: Int) {
        if (id != detalleId) abrir(id)
    }

    fun abrir(id: Int) {
        detalleId = id
        solicitudDetalle?.cancel()
        _detalle.value = UiState.Cargando
        solicitudDetalle = viewModelScope.launch {
            repo.detalle(id).onSuccess { post ->
                repo.comentarios(id).onSuccess { comentarios ->
                    _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
                }.onFailure { _detalle.value = UiState.Error(mensajeDe(it)) }
            }.onFailure { _detalle.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun cerrarDetalle() {
        detalleId = null
        solicitudDetalle?.cancel()
    }

    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion = _creacion.asStateFlow()
    private var envio: Job? = null

    fun iniciarCreacion() {
        envio?.cancel()
        _creacion.value = null
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank() || envio?.isActive == true) return
        _creacion.value = UiState.Cargando
        envio = viewModelScope.launch {
            repo.crear(NuevoPost(1, titulo.trim(), texto.trim()))
                .onSuccess { _creacion.value = UiState.Exito(it) }
                .onFailure { _creacion.value = UiState.Error(mensajeDe(it)) }
        }
    }
}
