package com.tecsup.avisoscampus

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tecsup.avisoscampus.data.*
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import com.tecsup.avisoscampus.ui.PantallaAvisos
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  val vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
   override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(AvisosViewModel::class.java))
    @Suppress("UNCHECKED_CAST")
    return AvisosViewModel(AvisosRepositoryMemoria()) as T
   }
  })[AvisosViewModel::class.java]
  setContent { MaterialTheme(colorScheme=lightColorScheme(primary=Color(0xFF1565C0))) { PantallaAvisos(vm) } }
 }
}
