package com.jara.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    Column(Modifier.fillMaxSize().padding(28.dp),verticalArrangement=Arrangement.spacedBy(20.dp,Alignment.CenterVertically),horizontalAlignment=Alignment.CenterHorizontally) {
        Text("LAVACAMPUS",style=MaterialTheme.typography.headlineMedium,color=MaterialTheme.colorScheme.onBackground)
        CircularProgressIndicator()
        Text("Preparando tu espacio",color=MaterialTheme.colorScheme.onBackground)
    }
}
