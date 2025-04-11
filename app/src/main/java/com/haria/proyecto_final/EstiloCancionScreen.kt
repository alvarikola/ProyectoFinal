package com.haria.proyecto_final

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import com.haria.proyecto_final.menu.TopAppBar


@Composable
fun EstiloCancionScreen(context: ComponentActivity, estilo: String, icon: Painter, navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentEstiloCancion(innerPadding, estilo, icon)
        }
    )
}