package com.haria.proyecto_final

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.menu.TopAppBar
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.seleccionMusica.ContentMusica
import kotlinx.coroutines.launch

@Composable
fun SalaScreen(context: ComponentActivity, navController: NavHostController, perfilId: String, musicViewModel: MusicViewModel) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentSala(innerPadding, context, perfilId, musicViewModel) { action, cancion ->
                when (action) {
                    PlayerAction.Play -> {
                        // Construir la URL completa usando el ID de la canción
                        val musicUrl = "https://prod-1.storage.jamendo.com/?trackid=${cancion}&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D"
                        Log.d("EstiloCancionScreen", "Enviando broadcast para reproducir: ${cancion}, URL: $musicUrl")

                        val intent = Intent("PLAY_MUSIC")
                        intent.putExtra("music_url", musicUrl)
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
            }
        }
    )
}