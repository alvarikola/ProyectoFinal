package com.haria.proyecto_final.seleccionMusica

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.haria.proyecto_final.menu.TopAppBar

@Composable
fun MusicaScreen(context: ComponentActivity, navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentMusica(innerPadding, context, navController)
        }
    )
}