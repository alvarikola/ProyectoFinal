package com.haria.proyecto_final

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.menu.TopAppBar
import com.haria.proyecto_final.seleccionMusica.ContentMusica

@Composable
fun SalaScreen(context: ComponentActivity, navController: NavHostController, perfilId: String) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentSala(innerPadding, context, perfilId)
        }
    )
}