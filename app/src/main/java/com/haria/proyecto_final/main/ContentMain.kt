package com.haria.proyecto_final.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.utils.Loading
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun ContentMain(innerPadding: PaddingValues, context: Context, navController: NavHostController) {
    var perfilesEmitiendo by remember { mutableStateOf<List<Perfil>>(emptyList()) }
    val scrollState = rememberScrollState()
    val frasesMain = listOf(
        "Entra al flow. Comparte la vibra",
        "Las salas te esperan. ¿Cuál es tu mood hoy?",
        "El ritmo se comparte mejor en grupo",
        "Dale play con quien vibra como tú",
        "Tu música, tu gente, tu flow",
        "Conecta con quienes sienten el mismo beat",
        "Siente el flow, vive el momento",
        "Crea tu sala, crea tu ambiente",
        "Una canción, mil conexiones",
        "La música te une, tu mood elige",
        "Súbete al beat del día",
        "Haz del sonido una fiesta compartida",
        "La vibra que buscas, la gente que conecta",
        "Explora salas, descubre vibes",
        "Rompe el silencio con tu Flow Rosa"
    )
    val fraseAleatoria = remember { frasesMain.random() }

    LaunchedEffect(key1 = true) {
        try {
            val perfiles = SupabaseManager.getPerfiles()
            perfilesEmitiendo = perfiles
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener canciones: ${e.message}")
        }
    }

    // Escuchamos cambios en todos los perfiles en tiempo real
    LaunchedEffect(Unit) {
        try {
            SupabaseManager.escucharCambiosPerfiles().collect() { nuevosPerfiles ->
                perfilesEmitiendo = nuevosPerfiles
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al escuchar cambios en perfiles: ${e.message}", e)
        }
    }

    if(perfilesEmitiendo.isEmpty()) {
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(12.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = fraseAleatoria,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary
            )
            if (perfilesEmitiendo.all { it.trackid == null }) {
                Text(
                    text = "Nadie está escuchando. Sé el primero en compartir tu música",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
            } else {
                perfilesEmitiendo.forEach { perfil ->
                    var cancion by remember(perfil.trackid) { mutableStateOf<Cancion?>(null) }

                    LaunchedEffect(perfil.trackid) {
                        try {
                            cancion = perfil.trackid?.let { SupabaseManager.getCancionPorId(it) }
                        } catch (e: Exception) {
                            Log.e("Error", "Error al obtener canción: ${e.message}")
                        }
                    }
                    if(perfil.trackid != null) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                                .clickable { navController.navigate("salaScreen/${perfil.id}") },
                            verticalAlignment = Alignment.CenterVertically // Centra verticalmente el contenido
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = cancion?.imagenUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(0.35f)
                                    .aspectRatio(1f),
                            )
                            Column(
                                modifier = Modifier
                                    .weight(0.65f)
                            ) {
                                Text(
                                    text = "${perfil.nombre}, ${perfil.pais}",
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Escuchando: ${cancion?.nombre ?: "Cargando..."}",
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}