package com.tecsup.productos

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.lista.ListaViewModel

class AppContainer {
    private val productosDePrueba = listOf(
        Item(id = 1, titulo = "Samsung Galaxy S24", subtitulo = "Samsung", imagenUrl = null),
        Item(id = 2, titulo = "iPhone 15", subtitulo = "Apple", imagenUrl = null),
        Item(id = 3, titulo = "Google Pixel 8", subtitulo = "Google", imagenUrl = null),
        Item(id = 4, titulo = "Motorola Edge 50", subtitulo = "Motorola", imagenUrl = null)
    )

    val listaFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ListaViewModel { Result.success(productosDePrueba) }
        }
    }
}
