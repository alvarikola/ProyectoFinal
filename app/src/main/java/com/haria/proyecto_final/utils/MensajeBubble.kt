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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.data.Mensaje


/**
 * Composable que representa un mensaje en forma de burbuja dentro de un chat.
 *
 * Este componente muestra el nombre del usuario, el texto del mensaje y, si corresponde,
 * emojis representados por identificadores específicos.
 *
 * @param mensaje Objeto `Mensaje` que contiene la información del mensaje a mostrar.
 * @param userColors Mapa mutable que asocia nombres de usuario con colores únicos.
 * @param imageLoader Cargador de imágenes utilizado para cargar emojis animados.
 */
@Composable
fun MessageBubble(mensaje: Mensaje, userColors: MutableMap<String, Color>, imageLoader: ImageLoader) {
    // Nombre de usuario por defecto.
    val userDefecto by remember { mutableStateOf("User${(1..100).random()}") }
    // Nombre del usuario o valor por defecto.
    val userName = mensaje.userNombre ?: userDefecto
    // Color único para el usuario.
    val userColor = userColors.getOrPut(userName) { generateRandomColor() }
    // Divide el texto en partes (texto y emojis).
    val text = splitTextWithEmojis(mensaje.text)

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Margen vertical de 4dp.
        crossAxisAlignment = FlowCrossAxisAlignment.End //Alineado abajo
    ) {
        Text(
            text = (mensaje.userNombre ?: userDefecto) + ": ",
            color = userColor,
            fontWeight = FontWeight.Bold
        )
        text.forEach {
            if (it.startsWith("#emoji:") && it.endsWith("#")) { // Verifica si es un emoji.
                val emoteId = it.substring(7, it.length - 1) // Extrae el ID del emoji.
                AVIFEmoteMovement(emoteId, imageLoader = imageLoader) // Muestra el emoji animado.
            } else {
                Text(
                    text = it,
                )
            }
        }
    }
}


/**
 * Función que divide un texto en partes, separando texto normal y códigos de emojis.
 *
 * @param input Cadena de texto que contiene texto y/o códigos de emojis.
 * @return Una lista de cadenas, donde cada elemento es texto o un código de emoji.
 */
fun splitTextWithEmojis(input: String): List<String> {
    // Expresión regular para coincidir con texto o códigos de emojis.
    val pattern = """([^#]+|#emoji:[^#]+#)""".toRegex()

    // Encuentra todas las coincidencias en la cadena de entrada.
    return pattern.findAll(input)
        .map { it.value.trim() } // Elimina espacios en blanco.
        .filter { it.isNotEmpty() } // Filtra coincidencias vacías.
        .toList()
}