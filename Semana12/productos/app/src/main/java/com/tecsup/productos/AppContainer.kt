package com.tecsup.productos

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.data.auth.FirebaseAuthRepository
import com.tecsup.productos.data.local.AppDatabase
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.data.repository.ItemRepositoryImpl
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.domain.model.Sesion
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.notification.NotificadorAndroid
import com.tecsup.productos.ui.admin.AdminViewModel
import com.tecsup.productos.ui.detalle.DetalleViewModel
import com.tecsup.productos.ui.favoritos.FavoritosViewModel
import com.tecsup.productos.ui.lista.ListaViewModel
import com.tecsup.productos.ui.login.LoginViewModel

class AppContainer(contexto: Context) {
    private val baseDatos = AppDatabase.obtener(contexto)
    private val repositorio: ItemRepository = ItemRepositoryImpl(RetrofitClient.api, baseDatos)

    private val autenticacion: AuthRepository = FirebaseAuthRepository()
    private val notificador: Notificador = NotificadorAndroid(contexto)

    val loginFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            LoginViewModel(autenticacion)
        }
    }

    val listaFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ListaViewModel(repositorio, notificador)
        }
    }

    val favoritosFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            FavoritosViewModel(repositorio)
        }
    }

    fun adminFactory(sesion: Sesion): ViewModelProvider.Factory {
        require(sesion.rol == Rol.ADMIN) { "Se requiere el rol de administrador." }
        return viewModelFactory {
            initializer {
                AdminViewModel(repositorio, notificador)
            }
        }
    }

    fun detalleFactory(id: Int): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            DetalleViewModel(id, repositorio)
        }
    }
}
