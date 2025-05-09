package com.haria.proyecto_final.seleccionMusica

import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.haria.proyecto_final.menu.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun MusicaScreen(context: ComponentActivity, navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(navController) },
        content = { innerPadding ->
            // Contenido principal de la pantalla
            ContentMusica(innerPadding, context, navController)
        }
    )
}