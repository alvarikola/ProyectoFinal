package com.haria.proyecto_final.seleccionMusica

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.R

@Composable
fun ContentMusica(innerPadding: PaddingValues, context: Context, navController: NavHostController) {

    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        ContenedorMusica("rock", navController)
        ContenedorMusica("electronica", navController)
        ContenedorMusica("hiphop", navController)
        ContenedorMusica("pop", navController)
        ContenedorMusica("jazz", navController)
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
        "jazz" -> {
            icon = painterResource(id = R.drawable.portada_jazz)
            iconName = "portada_jazz"
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