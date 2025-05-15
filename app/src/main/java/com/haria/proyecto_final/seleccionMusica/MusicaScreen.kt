package com.haria.proyecto_final.seleccionMusica

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.menu.TopAppBar


/**
 * Composable que representa la pantalla de selección de estilo de música.
 *
 * @param context Contexto de la actividad principal.
 * @param navController Controlador de navegación para gestionar las rutas.
 * @param imageLoader Cargador de imágenes para mostrar íconos o recursos relacionados.
 */
@Composable
fun MusicaScreen(context: ComponentActivity, navController: NavHostController, imageLoader: ImageLoader) {
    Scaffold(
        // Barra superior de la pantalla con opciones de navegación.
        topBar = { TopAppBar(navController, imageLoader = imageLoader) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentMusica(innerPadding, context, navController)
        }
    )
}