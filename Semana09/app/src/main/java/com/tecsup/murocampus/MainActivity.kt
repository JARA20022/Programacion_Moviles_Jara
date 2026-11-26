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
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )

                        )
                    }

                ) { margen ->
                    Box(Modifier.fillMaxSize().padding(margen)) {
                        PantallaLista(vm)
                    }
                }
            }
        }
    }
}
