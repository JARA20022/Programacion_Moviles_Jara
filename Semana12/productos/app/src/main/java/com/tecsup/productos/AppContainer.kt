package com.tecsup.productos

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.data.repository.ItemRepositoryImpl
import com.tecsup.productos.ui.lista.ListaViewModel

class AppContainer {
    private val repositorio: ItemRepository = ItemRepositoryImpl(RetrofitClient.api)

    val listaFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ListaViewModel(repositorio)
        }
    }
}
