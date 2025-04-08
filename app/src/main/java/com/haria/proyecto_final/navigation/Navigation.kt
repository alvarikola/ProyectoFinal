package com.haria.proyecto_final.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haria.proyecto_final.LoginScreen
import com.haria.proyecto_final.perfil.PerfilScreen
import com.haria.proyecto_final.SalaScreen
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
            SalaScreen(context, navController)
        }
        composable("perfilScreen") {
            PerfilScreen(context, navController)
        }
        composable("loginScreen") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("mainScreen")
            })
        }
    }
}