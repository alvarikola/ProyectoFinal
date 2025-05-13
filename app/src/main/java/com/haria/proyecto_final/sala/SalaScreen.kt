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

@Composable
fun SalaScreen(context: ComponentActivity, navController: NavHostController, perfilId: String, musicViewModel: MusicViewModel, imagenLoader: ImageLoader) {
    Scaffold(
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

                            val intent = Intent("PLAY_MUSIC").apply {
                                putExtra("music_url", musicUrl)
                                startTimeMilis?.let { putExtra("start_time_millis", it) }
                            }
                            context.sendBroadcast(intent)
                        }

                        PlayerAction.Pause -> {
                            Log.d("EstiloCancionScreen", "Enviando broadcast para pausar")
                            val intent = Intent("PAUSE_MUSIC")
                            context.sendBroadcast(intent)
                        }

                        PlayerAction.Stop -> {
                            Log.d("EstiloCancionScreen", "Enviando broadcast para detener")
                            val intent = Intent("STOP_MUSIC")
                            context.sendBroadcast(intent)
                        }
                    }
                },
                onExit = { reason ->
                    Toast.makeText(context, reason, Toast.LENGTH_SHORT).show()
                    navController.navigate("mainScreen")
                }
            )
        }
    )

}