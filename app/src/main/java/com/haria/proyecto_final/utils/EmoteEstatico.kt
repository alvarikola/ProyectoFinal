package com.haria.proyecto_final.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter


/**
 * Composable que muestra una imagen estática en formato AVIF.
 *
 * @param emoteid ID del emote a mostrar.
 * @param medida Tamaño en dp de la imagen.
 * @param imageLoader Cargador de imágenes utilizado para cargar la imagen.
 */
@Composable
fun AVIFEmoteStatic(emoteid: String, medida: Int, imageLoader: ImageLoader) {
    val imageUrl = "https://cdn.7tv.app/emote/${emoteid.trim()}/2x.avif "
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                // Opcional: Imagen de respaldo para dispositivos sin soporte AVIF
                error(android.R.drawable.ic_menu_report_image)
            },
            imageLoader = imageLoader,
        ),
        contentDescription = "Emote AVIF",
        modifier = Modifier
            .size(medida.dp)
            .padding(8.dp)
    )
}