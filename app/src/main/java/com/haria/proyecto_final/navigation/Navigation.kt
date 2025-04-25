package com.haria.proyecto_final.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haria.proyecto_final.estiloCancion.EstiloCancionScreen
import com.haria.proyecto_final.LoginScreen
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SalaScreen
import com.haria.proyecto_final.perfil.PerfilScreen
import com.haria.proyecto_final.seleccionMusica.MusicaScreen
import com.haria.proyecto_final.main.MainScreen

@Composable
fun NavigationGraph(
    context: ComponentActivity,
    navController: NavHostController,
    checkAuthentication: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (checkAuthentication) "mainScreen" else "loginScreen",
    ) {
        composable("mainScreen") {
            MainScreen(context, navController)
        }
        composable("salaScreen") {
            MusicaScreen(context, navController)
        }
        composable("perfilScreen") {
            PerfilScreen(context, navController)
        }
        composable("loginScreen") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("mainScreen")
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
                navController = navController
            )
        }
        composable("salaScreen") {
            SalaScreen(context, navController)
        }
    }
}