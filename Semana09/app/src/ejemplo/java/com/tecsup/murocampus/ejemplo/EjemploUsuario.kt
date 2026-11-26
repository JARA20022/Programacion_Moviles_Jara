package com.tecsup.murocampus.ejemplo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.model.Usuario
import com.tecsup.murocampus.viewmodel.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import com.tecsup.murocampus.data.remote.EncabezadoJson
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}

interface UsuarioRepository {
    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
    override suspend fun listar(): Result<List<Usuario>> = try {
        Result.success(api.listar())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class UsuariosViewModel(private val repo: UsuarioRepository) : ViewModel() {
    private val _usuarios = MutableStateFlow<UiState<List<Usuario>>>(UiState.Cargando)
    val usuarios = _usuarios.asStateFlow()

    init {
        viewModelScope.launch {
            _usuarios.value = repo.listar().fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(it.localizedMessage ?: "Error al cargar usuarios") }
            )
        }
    }
}

object EjemploUsuario {
    val repository: UsuarioRepository by lazy {
        val api = Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(EncabezadoJson).build())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .build().create(UsuarioApi::class.java)
        UsuarioRepositoryRemote(api)
    }
}
