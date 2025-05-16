package com.haria.proyecto_final.sala

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.menu.TopAppBar
import com.haria.proyecto_final.musicaService.MusicViewModel


/**
 * Composable que representa la pantalla de la sala de música.
 *
 * @param context Contexto de la actividad principal.
 * @param navController Controlador de navegación para gestionar las rutas.
 * @param perfilId ID del perfil del usuario al que pertenece la sala.
 * @param musicViewModel ViewModel para gestionar el estado de la música.
 * @param imagenLoader Cargador de imágenes para mostrar emotes y portadas.
 */
@Composable
fun SalaScreen(context: ComponentActivity, navController: NavHostController, perfilId: String, musicViewModel: MusicViewModel, imagenLoader: ImageLoader) {
    Scaffold(
        // Barra superior de la pantalla con opciones de navegación.
        topBar = { TopAppBar(navController, salaPropia = true, imageLoader = imagenLoader) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentSala(
                innerPadding,
                context,
                perfilId,
                musicViewModel,
                imagenLoader,
                onAction = { action, cancion, startTimeMilis ->
                    when (action) {
                        PlayerAction.Play -> {
                            // Construir la URL completa usando el ID de la canción
                            val musicUrl =
                                "https://prod-1.storage.jamendo.com/?trackid=${cancion}&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D"
                            Log.d(
                                "EstiloCancionScreen",
                                "Enviando broadcast para reproducir: ${cancion}, URL: $musicUrl"
                            )

                            // Enviar un broadcast para iniciar la reproducción de música.
                            val intent = Intent("PLAY_MUSIC").apply {
                                putExtra("music_url", musicUrl)
                                startTimeMilis?.let { putExtra("start_time_millis", it) }
                            }
                            context.sendBroadcast(intent)
                        }

                        PlayerAction.Pause -> {
                            // Enviar un broadcast para pausar la música.
                            Log.d("EstiloCancionScreen", "Enviando broadcast para pausar")
                            val intent = Intent("PAUSE_MUSIC")
                            context.sendBroadcast(intent)
                        }

                        PlayerAction.Stop -> {
                            // Enviar un broadcast para detener la música.
                            Log.d("EstiloCancionScreen", "Enviando broadcast para detener")
                            val intent = Intent("STOP_MUSIC")
                            context.sendBroadcast(intent)
                        }
                    }
                },
                onExit = { reason ->
                    // Mostrar un mensaje al usuario, navegar a la pantalla principal y quitar la musica.
                    val intent = Intent("STOP_MUSIC")
                    context.sendBroadcast(intent)
                    Toast.makeText(context, reason, Toast.LENGTH_SHORT).show()
                    navController.navigate("mainScreen")
                }
            )
        }
    )

}