package com.tecsup.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.tecsup.avisoscampus.ui.*
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  val vm=ViewModelProvider(this,object: ViewModelProvider.Factory {
   override fun <T: androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
    require(modelClass.isAssignableFrom(AvisosViewModel::class.java))
    @Suppress("UNCHECKED_CAST")
    return AvisosViewModel((application as AvisosApp).repository) as T
   }
  })[AvisosViewModel::class.java]
  setContent { CampusTheme { PantallaAvisos(vm) } }
 }
}
