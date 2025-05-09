package com.haria.proyecto_final

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.haria.proyecto_final.main.ContentMain
import com.haria.proyecto_final.menu.TopAppBar

@Composable
fun ChatPropioScreen(context: ComponentActivity, navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(navController, salaPropia = true) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ChatPropioContent(innerPadding, context)
        }
    )
}