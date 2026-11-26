package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostsViewModelFactory<T : ViewModel>(
    private val tipo: Class<T>,
    private val construir: () -> T
) : ViewModelProvider.Factory {
    override fun <V : ViewModel> create(modelClass: Class<V>): V {
        require(modelClass.isAssignableFrom(tipo))
        return modelClass.cast(construir())!!
    }
}
