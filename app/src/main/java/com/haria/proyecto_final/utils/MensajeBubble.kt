package com.haria.proyecto_final.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.data.Mensaje

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