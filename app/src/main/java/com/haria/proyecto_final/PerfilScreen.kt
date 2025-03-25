package com.haria.proyecto_final

import androidx.activity.ComponentActivity
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.haria.proyecto_final.menu.Menu
import com.haria.proyecto_final.menu.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(context: ComponentActivity, navController: NavHostController) {
    val drawerState = androidx.compose.material3.rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()


    // Función para abrir el drawer
    val openDrawer: () -> Unit = {
        coroutineScope.launch { drawerState.open() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Menu(context = context, navController = navController)
        }
    ) {
        Scaffold(
            topBar = { TopAppBar(onNavigationClick = openDrawer, title = "Perfil") },
            content = { innerPadding ->
                // Contenido principal de la pantalla
                ContentPerfil(innerPadding, context)
            }
        )
    }
}