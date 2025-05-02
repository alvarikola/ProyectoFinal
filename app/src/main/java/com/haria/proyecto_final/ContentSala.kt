package com.haria.proyecto_final

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.utils.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@Composable
fun ContentSala(innerPadding: PaddingValues, context: Context, perfilId: String, musicViewModel: MusicViewModel, onAction: (PlayerAction, Int, Long?) -> Unit) {
    var perfil by remember { mutableStateOf<Perfil?>(null) }
    var cancion by remember { mutableStateOf<Cancion?>(null) }
    var startTimeMillis by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()
    val isPlaying by musicViewModel.isPlaying.collectAsState()

    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfilPorId(perfilId)
            cancion = perfil?.trackid?.let { SupabaseManager.getCancionPorId(it) }
            startTimeMillis = OffsetDateTime.parse(perfil?.fecha_inicio_cancion)
                .toInstant()
                .toEpochMilli()
            onAction(PlayerAction.Play, perfil?.trackid ?: 0, startTimeMillis)
            Log.i("Perfil",startTimeMillis.toString())
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener el perfil: ${e.message}")
        }
    }

    // Escucha cambios en tiempo real
    LaunchedEffect(perfilId) {
        try {
            SupabaseManager.escucharCambiosPerfil(perfilId) { nuevoPerfil ->
                perfil = nuevoPerfil
                scope.launch(Dispatchers.IO) {
                    nuevoPerfil.trackid?.let { trackId ->
                        val nuevaCancion = SupabaseManager.getCancionPorId(trackId)
                        cancion = nuevaCancion
                    }
                }
                onAction(PlayerAction.Play, perfil?.trackid ?: 0, startTimeMillis)
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al escuchar cambios en el perfil: ${e.message}", e)
        }
    }

    if (perfil == null || cancion == null) {
        Loading()
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (cancion != null) {
                AsyncImage(
                    model = cancion!!.imagenUrl,
                    contentDescription = cancion!!.nombre ?: "Portada de la canción",
                    modifier = Modifier.size(250.dp),
                    contentScale = ContentScale.Crop
                )
            }
            cancion?.nombre?.let {
                Text(
                    text = it,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            cancion?.cantante?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            // TODO interfaz de chatgpt: chat para hablar y quitar musica al salir
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    text = "DJ ${perfil?.nombre}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "10 personas",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (isPlaying) {
                Button(
                    onClick = { onAction(PlayerAction.Pause, perfil?.trackid ?: 0, null) },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(60.dp) // Tamaño del botón redondo
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "pausa",
                        modifier = Modifier.size(35.dp)
                    )
                }
            } else {
                Button(
                    onClick = { onAction(PlayerAction.Play, perfil?.trackid ?: 0, null) },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(60.dp) // Tamaño del botón redondo
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}