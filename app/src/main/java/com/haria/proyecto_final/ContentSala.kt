package com.haria.proyecto_final

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Perfil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ContentSala(innerPadding: PaddingValues, context: Context, perfilId: String) {
    var perfil by remember { mutableStateOf<Perfil?>(null) }
    var cancion by remember { mutableStateOf<Cancion?>(null) }
    val scope = rememberCoroutineScope()
    // Cambiar la forma de obtener el trackid porque aqui estas obteniedno el de tu propio perfil
    // y tiene que ser el que seleccionas

    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfilPorId(perfilId)
            cancion = perfil?.trackid?.let { SupabaseManager.getCancionPorId(it) }
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener el perfil: ${e.message}")
        }
    }

    // Escucha cambios en tiempo real
    LaunchedEffect(perfilId) {
        try {
            if (perfilId != null) {
                // Assume escucharCambiosPerfil returns a Cancellable or a Job
                SupabaseManager.escucharCambiosPerfil(perfilId) { nuevoPerfil ->
                    perfil = nuevoPerfil
                    // Fetch cancion asynchronously to avoid blocking the main thread
                    scope.launch(Dispatchers.IO) {
                        nuevoPerfil.trackid?.let { trackId ->
                            val nuevaCancion = SupabaseManager.getCancionPorId(trackId)
                            cancion = nuevaCancion
                        } ?: run {
                            cancion = null // Handle case when trackid is null
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al escuchar cambios en el perfil: ${e.message}", e)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(cancion != null) {
            AsyncImage(
                model = cancion!!.imagenUrl,
                contentDescription = cancion!!.nombre ?: "Portada de la canción",
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}