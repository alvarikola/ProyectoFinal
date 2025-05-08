package com.haria.proyecto_final

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.menu.Menu
import com.haria.proyecto_final.menu.TopAppBar
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.seleccionMusica.ContentMusica
import kotlinx.coroutines.launch

@Composable
fun SalaScreen(context: ComponentActivity, navController: NavHostController, perfilId: String, musicViewModel: MusicViewModel) {
    val scope = rememberCoroutineScope()
    val drawerState = androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)

    // Función para abrir el drawer
    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Menu(context = context, navController = navController)
        }
    ) {
        Scaffold(
            topBar = { TopAppBar(onNavigationClick = openDrawer, navController, salaPropia = true) },
            content = { innerPadding ->
                // Contenido principal de la pantalla
                ContentSala(
                    innerPadding,
                    context,
                    perfilId,
                    musicViewModel,
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
}