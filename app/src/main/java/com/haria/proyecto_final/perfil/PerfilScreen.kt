package com.haria.proyecto_final.perfil

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.menu.TopAppBar


/**
 * Composable que representa la pantalla de perfil del usuario.
 *
 * @param context Contexto de la actividad principal.
 * @param navController Controlador de navegación para gestionar las rutas.
 * @param imageLoader Cargador de imágenes para mostrar emotes personalizados.
 */
@Composable
fun PerfilScreen(context: ComponentActivity, navController: NavHostController, imageLoader: ImageLoader) {

    // Estado que almacena la lista de emotes disponibles.
    var emotes by remember { mutableStateOf<List<Emote>>(emptyList()) }

    // Efecto lanzado al inicializar el Composable para obtener los emotes estáticos.
    LaunchedEffect(true) {
        emotes = SupabaseManager.getEmotesEstaticos()
        Log.i("Perfil", emotes.toString())
    }

    // Estructura principal de la pantalla con una barra superior y contenido.
    Scaffold(
        topBar = { TopAppBar(navController, imageLoader = imageLoader) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentPerfil(innerPadding, context, emotes, imageLoader)
        }
    )

}