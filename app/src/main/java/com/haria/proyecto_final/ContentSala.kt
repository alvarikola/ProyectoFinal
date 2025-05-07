package com.haria.proyecto_final

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Mensaje
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.utils.Loading
import io.github.jan.supabase.realtime.broadcastFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.time.OffsetDateTime


@Composable
fun ContentSala(innerPadding: PaddingValues, context: Context, perfilId: String, musicViewModel: MusicViewModel, onAction: (PlayerAction, Int, Long?) -> Unit, onExit: (reason: String) -> Unit) {
    var perfil by remember { mutableStateOf<Perfil?>(null) }
    var cancion by remember { mutableStateOf<Cancion?>(null) }
    var startTimeMillis by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()
    val isPlaying by musicViewModel.isPlaying.collectAsState()

    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfilPorId(perfilId)
            cancion = perfil?.trackid?.let { SupabaseManager.getCancionPorId(it) }
            if (cancion == null) {
                onExit("La sala se ha cerrado")
                return@LaunchedEffect
            }
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
                if (nuevoPerfil.trackid == null) {
                    onExit("La sala se ha cerrado")
                    return@escucharCambiosPerfil
                }
                scope.launch(Dispatchers.IO) {
                    nuevoPerfil.trackid?.let { trackId ->
                        val nuevaCancion = SupabaseManager.getCancionPorId(trackId)
                        cancion = nuevaCancion
                    }
                }
                startTimeMillis = OffsetDateTime.parse(perfil?.fecha_inicio_cancion)
                    .toInstant()
                    .toEpochMilli()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.40f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (cancion != null) {
                        AsyncImage(
                            model = cancion!!.imagenUrl,
                            contentDescription = cancion!!.nombre ?: "Portada de la canción",
                            modifier = Modifier
                                .weight(0.65f),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.35f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
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
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.075f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "DJ ${perfil?.nombre}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "10 personas",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.075f),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (isPlaying) {
                    Button(
                        onClick = { onAction(PlayerAction.Pause, perfil?.trackid ?: 0, null) },
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(45.dp) // Tamaño del botón redondo
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pause),
                            contentDescription = "pausa",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { onAction(PlayerAction.Play, perfil?.trackid ?: 0, null) },
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(45.dp) // Tamaño del botón redondo
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Play",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .weight(0.45f)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 5.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        ChatScreen(perfilId)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatScreen(userId: String) {
    val mensajes = remember { mutableStateListOf<Mensaje>() }
    var input by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var user by remember { mutableStateOf<Perfil?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val userColors = remember { mutableStateMapOf<String, Color>() }

    // Crear canal
    val channel = SupabaseManager.obtenerCanal(userId)

    LaunchedEffect(key1 = true) {
        user = SupabaseManager.getPerfil()
    }

    // Escuchar mensajes entrantes
    LaunchedEffect(Unit) {
        val flow = channel.broadcastFlow<Mensaje>(event = "message")
        flow.collect { message ->
            mensajes.add(message)
        }
    }

    // Suscribirse al canal (iniciar conexión)
    LaunchedEffect(Unit) {
        channel.subscribe(blockUntilSubscribed = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(mensajes.reversed()) { message ->
                MessageBubble(message, userColors)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = {
                    input = it
                    Log.i("prueba", "Nuevo valor: ${it.text}")
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val currentText = input.text
                    if (currentText.isNotBlank()) {
                        coroutineScope.launch {
                            val msg = Mensaje(user?.nombre, currentText, userId)
                            val jsonMessage = Json.encodeToJsonElement(msg) as JsonObject
                            channel.broadcast(
                                event = "message",
                                message = jsonMessage
                            )
                            mensajes.add(msg) // Mostrar tu mensaje también
                        }
                        input = TextFieldValue("")
                    }
                }
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun MessageBubble(mensaje: Mensaje, userColors: MutableMap<String, Color>) {
    val userDefecto by remember { mutableStateOf("User${(1..100).random()}") }
    val userName = mensaje.userNombre ?: userDefecto
    val userColor = userColors.getOrPut(userName) { generateRandomColor() }

    FlowRow (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = (mensaje.userNombre ?: userDefecto) + ": ",
            color = userColor
        )
        Text(
            text = mensaje.text
        )
    }
}

fun generateRandomColor(): Color {
    val red = (100..255).random()
    val green = (100..255).random()
    val blue = (100..255).random()
    return Color(red, green, blue)
}