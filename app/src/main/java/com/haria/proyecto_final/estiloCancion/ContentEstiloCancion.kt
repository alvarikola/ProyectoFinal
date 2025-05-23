package com.haria.proyecto_final.estiloCancion

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.haria.proyecto_final.R
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.utils.Loading

enum class PlayerAction {
    Play, Pause, Stop
}


/**
 * Composable que muestra una lista de canciones de un estilo específico.
 *
 * @param innerPadding Espaciado interno para el contenido.
 * @param estilo Estilo o género musical de las canciones a mostrar.
 * @param icon Icono que representa el estilo musical.
 * @param viewModel ViewModel que gestiona el estado de la música.
 * @param imageLoader Cargador de imágenes para las carátulas de las canciones.
 * @param onAction Callback que maneja las acciones del reproductor (Play, Pause, Stop).
 */
@Composable
fun ContentEstiloCancion(innerPadding: PaddingValues, estilo: String, icon: Painter, viewModel: MusicViewModel, imageLoader: ImageLoader, onAction: (PlayerAction, Cancion) -> Unit) {

    var listaCanciones by remember { mutableStateOf<List<Cancion>>(emptyList()) }
    val scrollState = rememberScrollState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()

    // Efecto lanzado al inicializar el Composable para cargar las canciones del estilo dado
    LaunchedEffect(key1 = true) {
        try {
            Log.d("ContentEstilo", "Obteniendo canciones para estilo: $estilo")
            listaCanciones = SupabaseManager.getCancionesPorEstilo(estilo)
            Log.d("ContentEstilo", "Canciones obtenidas: ${listaCanciones.size}")
        } catch (e: Exception) {
            Log.e("ContentEstilo", "Error al obtener las canciones: ${e.message}")
        }
    }

    // Muestra un indicador de carga si no hay canciones disponibles
    if(listaCanciones.isEmpty()) {
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Muestra el icono del estilo musical
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                // Itera sobre la lista de canciones y las muestra
                listaCanciones.forEach { cancion ->
                    val isCurrentSong = currentSong?.id == cancion.id
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Muestra la imagen de la canción si está disponible
                        if (cancion.imagenUrl == null) {
                            Log.d("CancionImagen", "URL de la imagen: ${cancion.imagenUrl}")
                        } else {
                            Column(
                                modifier = Modifier
                                    .weight(0.3f)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = cancion.imagenUrl, imageLoader = imageLoader),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(130.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        // Muestra el nombre de la canción y los botones de control
                        Column(
                            modifier = Modifier
                                .weight(0.7f) // hace que el texto y botones ocupen el resto del espacio
                                .background(MaterialTheme.colorScheme.primary)
                                .defaultMinSize(minHeight = 130.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${cancion.nombre}",
                                    modifier = Modifier.padding(2.dp),
                                    fontSize = 20.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.setCurrentSong(cancion)
                                        onAction(PlayerAction.Play, cancion)
                                    },
                                    enabled = !isPlaying || !isCurrentSong
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                                }

                                Button(
                                    onClick = { onAction(PlayerAction.Pause, cancion) },
                                    enabled = isPlaying && isCurrentSong
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_pause),
                                        contentDescription = "pausa",
                                    )
                                }

                                Button(
                                    onClick = {
                                        onAction(PlayerAction.Stop, cancion)
                                        viewModel.setCurrentSong(null)
                                    }
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_stop),
                                        contentDescription = "pausa",
                                    )
                                }
                            }
                        }
                    }
                    // Muestra un divisor entre las canciones
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}