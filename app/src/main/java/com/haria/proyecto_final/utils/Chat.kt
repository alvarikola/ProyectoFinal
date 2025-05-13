package com.haria.proyecto_final.utils

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.data.Mensaje
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.data.PresenceState
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.presenceDataFlow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Chat(userId: String, emotes: List<Emote>, imageLoader: ImageLoader) {
    val mensajes = remember { mutableStateListOf<Mensaje>() }
    var input by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var user by remember { mutableStateOf<Perfil?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val userColors = remember { mutableStateMapOf<String, Color>() }
    var subscribersCount by remember { mutableStateOf(0) }
    var showEmojiMenu by remember { mutableStateOf(false) }
    var scrollState = rememberScrollState()

    // Crear canal
    val channel = SupabaseManager.obtenerCanal(userId)

    LaunchedEffect(key1 = true) {
        user = SupabaseManager.getPerfil()
    }

    // Manejar la suscripción y desuscripción del canal
    DisposableEffect(userId) {
        // Suscribirse al canal al inicio
        coroutineScope.launch {
            channel.subscribe(blockUntilSubscribed = true)

            // Configurar la recolección de mensajes después de suscribirse
            val flow = channel.broadcastFlow<Mensaje>(event = "message")
            coroutineScope.launch {
                flow.collect { message ->
                    mensajes.add(message)
                }
            }

            val id = PresenceState(userId)
            val jsonMessageUserId = Json.encodeToJsonElement(id) as JsonObject
            channel.track(jsonMessageUserId)

            // 3. Coleccionar el flujo de presencia para obtener usuarios conectados
            channel.presenceDataFlow<PresenceState>()
                .collect { presenceList ->
                    subscribersCount = presenceList.size // Actualizar contador
                    Log.i("Chat", "Usuarios conectados: $presenceList")
                }
        }

        // Cleanup: desuscribirse del canal cuando el composable sea removido
        onDispose {
            MainScope().launch {
                try {
                    channel.unsubscribe()
                    Log.d("Chat", "Desuscrito del canal: $userId")
                } catch (e: Exception) {
                    Log.e("Chat", "Error al desuscribirse del canal: ${e.message}", e)
                }
            }
        }
    }

    // Modal Bottom Sheet para emojis
    if (showEmojiMenu) {
        ModalBottomSheet(
            onDismissRequest = { showEmojiMenu = false },
            sheetState = rememberModalBottomSheetState(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.background,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        maxItemsInEachRow = 6
                    ) {
                        emotes.forEach{ emote ->
                            Box(
                                Modifier.clickable{
                                    val currentText = input.text
                                    input = TextFieldValue(currentText + "#emoji:" + emote.id.trim() + "#")
                                    showEmojiMenu = false
                                }
                            ) {
                                AVIFEmoteWithLoader(emote.id, imageLoader)
                            }
                        }
                    }
                }
            }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Usuarios en línea: $subscribersCount"
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(mensajes.reversed()) { message ->
                MessageBubble(message, userColors, imageLoader)
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
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = "emoji",
                        modifier = Modifier.clickable(onClick = { showEmojiMenu = true })
                    )
                }
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
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = Color.White
                )
            }
        }
    }
}


