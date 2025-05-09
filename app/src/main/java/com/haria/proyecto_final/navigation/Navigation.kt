package com.haria.proyecto_final.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haria.proyecto_final.ChatPropioScreen
import com.haria.proyecto_final.estiloCancion.EstiloCancionScreen
import com.haria.proyecto_final.LoginScreen
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SalaScreen
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.perfil.PerfilScreen
import com.haria.proyecto_final.seleccionMusica.MusicaScreen
import com.haria.proyecto_final.main.MainScreen
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.utils.UserSessionManager
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    context: ComponentActivity,
    navController: NavHostController,
    checkAuthentication: Boolean,
    musicViewModel: MusicViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (checkAuthentication) "mainScreen" else "loginScreen",
    ) {
        composable("mainScreen") {
            MainScreen(context, navController)
        }
        composable("musicaScreen") {
            MusicaScreen(context, navController)
        }
        composable("perfilScreen") {
            PerfilScreen(context, navController)
        }
        composable("loginScreen") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("mainScreen")
                val userId = SupabaseManager.getCurrentUserId()
                if (userId != null) {
                    UserSessionManager.guardarUserId(context, userId)
                }
            })
        }
        composable("estiloCancionScreen/{estilo}/{iconName}") { backStackEntry ->
            val iconName = backStackEntry.arguments?.getString("iconName")
            val iconResId = if (!iconName.isNullOrEmpty()) {
                val resId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
                if (resId != 0) resId else R.drawable.portada_generica
            } else {
                R.drawable.portada_generica
            }
            EstiloCancionScreen(
                context,
                estilo = backStackEntry.arguments?.getString("estilo") ?: "default",
                icon = painterResource(id = iconResId),
                navController = navController,
                musicViewModel = musicViewModel
            )
        }
        composable("salaScreen/{perfilId}") { backStackEntry ->
            val perfilId = backStackEntry.arguments?.getString("perfilId") ?: "default"
            SalaScreen(context, navController, perfilId, musicViewModel)
        }
        composable("chatPropioScreen") {
            ChatPropioScreen(context, navController)
        }
    }
}