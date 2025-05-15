package com.haria.proyecto_final.main

import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.menu.TopAppBar


/**
 * Composable que representa la pantalla principal de la aplicación.
 *
 * @param context Contexto de la actividad que contiene esta pantalla.
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param imageLoader Cargador de imágenes para las carátulas de las canciones.
 */
@Composable
fun MainScreen(context: ComponentActivity, navController: NavHostController, imageLoader: ImageLoader) {
    Scaffold(
        // Barra superior de la pantalla principal.
        topBar = { TopAppBar(navController, main = true, imageLoader = imageLoader) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentMain(innerPadding, context, navController, imageLoader)
        }
    )
}