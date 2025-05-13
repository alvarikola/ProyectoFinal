package com.haria.proyecto_final.utils

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.data.Mensaje


@Composable
fun MessageBubble(mensaje: Mensaje, userColors: MutableMap<String, Color>, imageLoader: ImageLoader) {
    val userDefecto by remember { mutableStateOf("User${(1..100).random()}") }
    val userName = mensaje.userNombre ?: userDefecto
    val userColor = userColors.getOrPut(userName) { generateRandomColor() }
    val text = splitTextWithEmojis(mensaje.text)

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        crossAxisAlignment = FlowCrossAxisAlignment.End //Alineado abajo
    ) {
        Text(
            text = (mensaje.userNombre ?: userDefecto) + ": ",
            color = userColor,
            fontWeight = FontWeight.Bold
        )
        text.forEach {
            if (it.startsWith("#emoji:") && it.endsWith("#")) {
                val emoteId = it.substring(7, it.length - 1)
                AVIFEmoteWithLoader(emoteId, imageLoader = imageLoader)
            } else {
                Text(
                    text = it,
                )
            }
        }
    }
}

fun splitTextWithEmojis(input: String): List<String> {
    // Regular expression to match text or emoji codes
    val pattern = """([^#]+|#emoji:[^#]+#)""".toRegex()

    // Find all matches in the input string
    return pattern.findAll(input)
        .map { it.value.trim() } // Trim any whitespace
        .filter { it.isNotEmpty() } // Remove any empty matches
        .toList()
}