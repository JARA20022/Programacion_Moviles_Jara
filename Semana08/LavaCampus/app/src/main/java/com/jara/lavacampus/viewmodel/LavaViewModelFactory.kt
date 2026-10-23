package com.jara.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jara.lavacampus.LavaApp
import com.jara.lavacampus.data.local.entity.Usuario

class LavaViewModelFactory(
    private val app: LavaApp,
    private val estudiante: Usuario? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(
                app.repository,
                app.preferencias,
                app::esperarDatosIniciales
            ) as T
        }
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            return InicioViewModel(
                requireNotNull(estudiante) { "Inicio requiere un estudiante autenticado." },
                app.repository
            ) as T
        }
        throw IllegalArgumentException("ViewModel no registrado: ${modelClass.name}")
    }
}
