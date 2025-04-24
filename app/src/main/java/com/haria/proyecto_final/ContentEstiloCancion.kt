package com.haria.proyecto_final

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.utils.Loading

enum class PlayerAction {
    Play, Pause, Stop
}

@Composable
fun ContentEstiloCancion(innerPadding: PaddingValues, estilo: String, icon: Painter, viewModel: MusicViewModel, onAction: (PlayerAction, Cancion) -> Unit) {

    var listaCanciones by remember { mutableStateOf<List<Cancion>>(emptyList()) }
    val scrollState = rememberScrollState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LaunchedEffect(key1 = true) {
        try {
            listaCanciones = SupabaseManager.getCancionesPorEstilo(estilo)
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener las canciones: ${e.message}")
        }
    }

    if(listaCanciones.isEmpty()) {
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(scrollState),
            ) {
                listaCanciones.forEach { cancion ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (cancion.imagenUrl == null) {
                            Log.d("CancionImagen", "URL de la imagen: ${cancion.imagenUrl}")
                        } else {
                            Image(
                                painter = rememberImagePainter(data = cancion.imagenUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = "${cancion.nombre}",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 20.sp
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onAction(PlayerAction.Play, cancion) },
                            enabled = !isPlaying
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reproducir")
                        }

                        Button(
                            onClick = { onAction(PlayerAction.Pause, cancion) },
                            enabled = isPlaying
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = "Pause")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pausar")
                        }

                        Button(
                            onClick = { onAction(PlayerAction.Stop, cancion) }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Stop")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Detener")
                        }
                    }
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