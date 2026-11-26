package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.*
import com.tecsup.murocampus.viewmodel.*
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(28, 29, 68)
        window.navigationBarColor = android.graphics.Color.rgb(11, 16, 37)
        val fabrica = PostsViewModelFactory(PostsViewModel::class.java) {
            PostsViewModel(PostRepositoryRemote(RetrofitClient.api))
        }
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = fabrica)
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }
                val volver: () -> Unit = {
                    vm.cerrarDetalle()
                    idSeleccionado = null
                    creando = false
                }
                val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                BackHandler(enabled = idSeleccionado != null || creando, onBack = volver)
                Scaffold(
                    topBar = {
                        BarraCampus(idSeleccionado != null || creando) { dispatcher?.onBackPressed() }
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            BotonCampus("＋  Publicar", onClick = {
                                vm.iniciarCreacion()
                                creando = true
                            })
                        }
                    }
                ) { margen ->
                    Box(Modifier.fillMaxSize().padding(margen)) {
                        val id = idSeleccionado
                        when {
                            creando -> Entrada { PantallaCrear(vm) }
                            id != null -> {
                                // El ViewModel conserva el detalle al rotar y lo repone al recrearse el proceso.
                                LaunchedEffect(id) { vm.asegurarDetalle(id) }
                                Entrada { PantallaDetalle(vm, id) }
                            }
                            else -> Entrada { PantallaLista(vm) { elegido ->
                                vm.abrir(elegido)
                                idSeleccionado = elegido
                            } }
                        }
                    }
                }
            }
        }
    }
}
