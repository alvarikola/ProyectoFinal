package com.haria.proyecto_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.haria.proyecto_final.SupabaseManager.init
import com.haria.proyecto_final.navigation.NavigationGraph
import com.haria.proyecto_final.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val logeado = SupabaseManager.isLoggedIn()
            ProyectoFinalTheme {
                NavigationGraph(this, navController, logeado)
            }
        }
    }

}