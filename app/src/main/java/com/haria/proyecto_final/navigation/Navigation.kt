package com.haria.proyecto_final.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haria.proyecto_final.main.MainScreen

@Composable
fun NavigationGraph(
    context: ComponentActivity,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen",
    ) {
        composable("mainScreen") {
            MainScreen(context, navController)
        }
    }
}