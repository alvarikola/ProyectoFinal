package com.haria.proyecto_final

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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