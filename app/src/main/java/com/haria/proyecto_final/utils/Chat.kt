package com.haria.proyecto_final.utils

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Mensaje
import com.haria.proyecto_final.data.Perfil
import io.github.jan.supabase.realtime.broadcastFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

@Composable
fun Chat(userId: String) {
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