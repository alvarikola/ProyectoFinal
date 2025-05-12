package com.haria.proyecto_final.perfil

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.menu.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(context: ComponentActivity, navController: NavHostController) {


    // TODO: Poner de fotos de perfil algunos emotes que se vean bien quietos
    var emotes by remember { mutableStateOf<List<Emote>>(emptyList()) }
    LaunchedEffect(true) {
        emotes = SupabaseManager.getEmotes()
        Log.i("Perfil", emotes.toString())
    }

    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentPerfil(innerPadding, context, emotes)
        }
    )

}