package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jara.lavacampus.LavaApp

class LavaViewModelFactory(private val app: LavaApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(
                app.repository,
                app.preferencias,
                app::esperarDatosIniciales
            ) as T
        }
        throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
    }
}
