package com.tecsup.centrotareas

import android.os.Bundle
import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.PantallaTareas
import com.tecsup.centrotareas.ui.theme.CentroTareasTheme
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        val factory = TareasViewModelFactory(TareasRepositoryLocal())
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(factory = factory)
                PantallaTareas(vm)
            }
        }
    }
}
