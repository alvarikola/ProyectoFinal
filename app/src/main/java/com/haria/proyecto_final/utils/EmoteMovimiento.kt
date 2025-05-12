package com.haria.proyecto_final.utils

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.haria.proyecto_final.data.Emote

@Composable
fun AVIFEmoteWithLoader(emoteid: String, imageLoader: ImageLoader) {
    val context = LocalContext.current
    val imageUrl = "https://cdn.7tv.app/emote/${emoteid.trim()}/2x.gif"
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .placeholder(android.R.drawable.stat_sys_download) // Mientras carga
                .error(android.R.drawable.stat_notify_error)    // Si falla
                .build(),
            imageLoader = imageLoader,

            ),
        contentDescription = "Emote AVIF con Coil",
        modifier = Modifier
            .size(80.dp)
            .padding(10.dp)
    )

}


