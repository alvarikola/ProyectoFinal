package com.haria.proyecto_final.sala

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Cancion

@Composable
fun ContentSala(innerPadding: PaddingValues, context: Context, navController: NavHostController) {

    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        ContenedorMusica("rock", navController)
        ContenedorMusica("electronica", navController)
        ContenedorMusica("hiphop", navController)
        ContenedorMusica("pop", navController)
        ContenedorMusica("generica", navController)
    }
}

@Composable
fun ContenedorMusica(estilo: String, navController: NavHostController) {

    val iconName:String


    var icon:Painter = painterResource(id = R.drawable.portada_generica)
    when (estilo) {
        "rock" -> {
            icon = painterResource(id = R.drawable.portada_rock)
            iconName = "portada_rock"
        }
        "electronica" -> {
            icon = painterResource(id = R.drawable.portada_electronic)
            iconName = "portada_electronic"
        }
        "hiphop" -> {
            icon = painterResource(id = R.drawable.portada_hiphop)
            iconName = "portada_hiphop"
        }
        "pop" -> {
            icon = painterResource(id = R.drawable.portada_pop)
            iconName = "portada_pop"
        }
        else -> {
            icon
            iconName = "portada_generica"
        }
    }
    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
            .clickable {
                navController.navigate("estiloCancionScreen/$estilo/$iconName")
            },
    ) {
        Image(
            painter = icon,
            contentDescription = "Icono de aplicación",
            Modifier.fillMaxSize(),
        )
    }
}