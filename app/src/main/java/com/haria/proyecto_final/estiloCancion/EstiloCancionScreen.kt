package com.haria.proyecto_final.estiloCancion

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.menu.TopAppBar
import kotlinx.coroutines.launch


/**
 * Composable que representa la pantalla de canciones de un estilo específico.
 *
 * @param context Contexto de la actividad que contiene esta pantalla.
 * @param estilo Estilo o género musical de las canciones a mostrar.
 * @param icon Icono que representa el estilo musical.
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param musicViewModel ViewModel que gestiona el estado de la música.
 * @param imageLoader Cargador de imágenes para las carátulas de las canciones.
 */
@Composable
fun EstiloCancionScreen(context: ComponentActivity, estilo: String, icon: Painter, navController: NavHostController, musicViewModel: MusicViewModel, imageLoader: ImageLoader) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(navController, imageLoader = imageLoader) },
        // Contenido principal de la pantalla
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentEstiloCancion(
                innerPadding,
                estilo,
                icon,
                musicViewModel,
                imageLoader
            ) { action, cancion ->
                when (action) {
                    PlayerAction.Play -> {
                        // Construir la URL completa usando el ID de la canción
                        val musicUrl =
                            "https://prod-1.storage.jamendo.com/?trackid=${cancion.id}&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D"
                        // Lanza una corrutina para establecer la canción actual del usuario.
                        scope.launch {
                            SupabaseManager.establecerCancion(cancion.id)
                        }
                        Log.d(
                            "EstiloCancionScreen",
                            "Enviando broadcast para reproducir: ${cancion.nombre}, URL: $musicUrl"
                        )

                        // Crea un intent para enviar un broadcast que indica que se debe reproducir música.
                        // Se incluye la URL de la canción como extra en el intent.
                        val intent = Intent("PLAY_MUSIC")
                        intent.putExtra("music_url", musicUrl)
                        context.sendBroadcast(intent)
                    }

                    // Maneja la acción de pausar la reproducción de música.
                    // Registra un mensaje en el log y envía un broadcast para pausar la música.
                    PlayerAction.Pause -> {
                        Log.d("EstiloCancionScreen", "Enviando broadcast para pausar")
                        val intent = Intent("PAUSE_MUSIC")
                        context.sendBroadcast(intent)
                    }

                    // Maneja la acción de detener la reproducción de música.
                    // Registra un mensaje en el log, envía un broadcast para detener la música y
                    // lanza una corrutina para restablecer la canción actual del usuario.
                    PlayerAction.Stop -> {
                        Log.d("EstiloCancionScreen", "Enviando broadcast para detener")
                        val intent = Intent("STOP_MUSIC")
                        context.sendBroadcast(intent)
                        scope.launch {
                            SupabaseManager.establecerCancion(null)
                        }
                    }
                }
            }
        }
    )
}