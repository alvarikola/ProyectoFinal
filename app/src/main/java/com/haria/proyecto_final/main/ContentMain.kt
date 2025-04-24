package com.haria.proyecto_final.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.haria.proyecto_final.MusicService
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.SupabaseManager


@Composable
fun ContentMain(innerPadding: PaddingValues, context: Context) {
    var listaCanciones by remember { mutableStateOf<List<Cancion>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        try {
            val canciones = SupabaseManager.getCancionesPorEstilo("electronica")
            Log.d("CancionesDebug", canciones.toString())
            listaCanciones = canciones
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener canciones: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listaCanciones.forEach { cancion ->
            Text(
                text = "Nombre de la canción: ${cancion.nombre}, Estilo: ${cancion.estilo}",
                modifier = Modifier.padding(8.dp),
                fontSize = 20.sp
            )
            if (cancion.imagenUrl == null) {
                Log.d("CancionImagen", "URL de la imagen: ${cancion.imagenUrl}")
            } else {
                Image(
                    painter = rememberImagePainter(data = cancion.imagenUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        MusicServicePlayer(context)
    }
}


@Composable
fun MusicServicePlayer(context: Context) {
    Button(onClick = {
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra("url", "https://prod-1.storage.jamendo.com/?trackid=1879171&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D")
        context.startForegroundService(intent)
    }) {
        Text("Reproducir en segundo plano")
    }
}