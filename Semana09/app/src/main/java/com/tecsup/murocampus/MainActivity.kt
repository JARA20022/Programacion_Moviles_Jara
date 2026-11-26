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
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                BackHandler(enabled = idSeleccionado != null || creando, onBack = volver)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            navigationIcon = {
                                if (idSeleccionado != null || creando) {
                                    IconButton(onClick = volver) {
                                        Text("←", color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(onClick = { vm.iniciarCreacion(); creando = true }) {
                                Text("+")
                            }
                        }
                    }
                ) { margen ->
                    Box(Modifier.fillMaxSize().padding(margen)) {
                        val id = idSeleccionado
                        when {
                            creando -> PantallaCrear(vm)
                            id != null -> {
                                // El ViewModel conserva el detalle al rotar y lo repone al recrearse el proceso.
                                LaunchedEffect(id) { vm.asegurarDetalle(id) }
                                PantallaDetalle(vm, id)
                            }
                            else -> PantallaLista(vm) { elegido ->
                                vm.abrir(elegido)
                                idSeleccionado = elegido
                            }
                        }
                    }
                }
            }
        }
    }
}
