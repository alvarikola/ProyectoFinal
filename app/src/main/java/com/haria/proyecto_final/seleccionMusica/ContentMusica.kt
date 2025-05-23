package com.haria.proyecto_final.seleccionMusica

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.haria.proyecto_final.R


/**
 * Composable que representa el contenido de la pantalla de selección de estilo de música.
 *
 * @param innerPadding Espaciado interno para ajustar el diseño.
 * @param context Contexto de la aplicación.
 * @param navController Controlador de navegación para gestionar las rutas.
 */
@Composable
fun ContentMusica(innerPadding: PaddingValues, context: Context, navController: NavHostController) {
    val scrollState = rememberScrollState()
    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState),
    ) {
        // Agrega contenedores de música para diferentes estilos.
        ContenedorMusica("rock", navController)
        ContenedorMusica("electronica", navController)
        ContenedorMusica("hiphop", navController)
        ContenedorMusica("pop", navController)
        ContenedorMusica("jazz", navController)
        ContenedorMusica("chillout", navController)
    }
}

/**
 * Composable que representa un contenedor de música para un estilo específico.
 *
 * @param estilo Estilo de música representado por el contenedor.
 * @param navController Controlador de navegación para gestionar las rutas.
 */
@Composable
fun ContenedorMusica(estilo: String, navController: NavHostController) {

    val iconName:String

    // Determina el ícono y el nombre del recurso basado en el estilo.
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
        "chillout" -> {
            icon = painterResource(id = R.drawable.portada_chillout)
            iconName = "portada_chillout"
        }
        else -> {
            icon
            iconName = "portada_generica"
        }
    }

    // Caja que contiene el ícono y permite la navegación al hacer clic.
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