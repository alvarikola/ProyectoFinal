package com.haria.proyecto_final.estiloCancion

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.menu.Menu
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.menu.TopAppBar
import kotlinx.coroutines.launch


@Composable
fun EstiloCancionScreen(context: ComponentActivity, estilo: String, icon: Painter, navController: NavHostController, musicViewModel: MusicViewModel) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentEstiloCancion(
                innerPadding,
                estilo,
                icon,
                musicViewModel
            ) { action, cancion ->
                when (action) {
                    PlayerAction.Play -> {
                        // Construir la URL completa usando el ID de la canción
                        val musicUrl =
                            "https://prod-1.storage.jamendo.com/?trackid=${cancion.id}&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D"
                        scope.launch {
                            SupabaseManager.establecerCancion(cancion.id)
                        }
                        Log.d(
                            "EstiloCancionScreen",
                            "Enviando broadcast para reproducir: ${cancion.nombre}, URL: $musicUrl"
                        )

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
                        scope.launch {
                            SupabaseManager.establecerCancion(null)
                        }
                    }
                }
            }
        }
    )
}