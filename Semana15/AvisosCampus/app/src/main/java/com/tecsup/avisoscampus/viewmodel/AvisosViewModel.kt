package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
 val avisos = repo.avisos
}
