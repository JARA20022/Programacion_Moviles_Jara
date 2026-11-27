package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
 val avisos = repo.avisos
 private val _permiso = MutableStateFlow(Permiso.SinPedir)
 val permiso = _permiso.asStateFlow()
 fun permisoRespondido(concedido: Boolean) { _permiso.value=if(concedido) Permiso.Concedido else Permiso.Denegado
 }
}
