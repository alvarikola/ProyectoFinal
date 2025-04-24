package com.haria.proyecto_final

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.haria.proyecto_final.menu.TopAppBar


@Composable
fun EstiloCancionScreen(context: ComponentActivity, estilo: String, icon: Painter, navController: NavHostController) {
    val viewModel: MusicViewModel = viewModel()
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentEstiloCancion(innerPadding, estilo, icon, viewModel) { action, cancion ->
                when (action) {
                    PlayerAction.Play -> {
                        val intent = Intent("PLAY_MUSIC")
                        intent.putExtra("music_url", "https://prod-1.storage.jamendo.com/?trackid=${cancion.id}&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D")
                        context.sendBroadcast(intent)
                    }
                    PlayerAction.Pause -> {
                        // Lógica para pausar la canción
                    }
                    PlayerAction.Stop -> {
                        // Lógica para detener la canción
                    }
                }
            }
        }
    )
}