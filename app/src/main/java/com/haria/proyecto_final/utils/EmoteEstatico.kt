package com.haria.proyecto_final.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.haria.proyecto_final.data.Emote

//Imagenes estaticas
@Composable
fun AVIFEmoteExample(emoteid: String, medida: Int) {
    val imageUrl = "https://cdn.7tv.app/emote/${emoteid.trim()}/2x.avif "
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                // Opcional: Imagen de respaldo para dispositivos sin soporte AVIF
                error(android.R.drawable.ic_menu_report_image)
            }
        ),
        contentDescription = "Emote AVIF",
        modifier = Modifier
            .size(medida.dp)
            .padding(8.dp)
    )
}