package com.haria.proyecto_final.chatPropio

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.menu.TopAppBar


/**
 * Composable que representa la pantalla principal del chat propio.
 *
 * @param context Contexto de la actividad actual.
 * @param navController Controlador de navegación utilizado para gestionar las rutas de la aplicación.
 * @param imageLoader Cargador de imágenes utilizado para manejar las imágenes en la pantalla.
 */
@Composable
fun ChatPropioScreen(context: ComponentActivity, navController: NavHostController, imageLoader: ImageLoader) {
    Scaffold(
        topBar = { TopAppBar(navController, salaPropia = true, imageLoader = imageLoader) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ChatPropioContent(innerPadding, context, imageLoader)
        }
    )
}