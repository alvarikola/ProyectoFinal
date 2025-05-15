package com.haria.proyecto_final.navigation

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.haria.proyecto_final.chatPropio.ChatPropioScreen
import com.haria.proyecto_final.estiloCancion.EstiloCancionScreen
import com.haria.proyecto_final.login.LoginScreen
import com.haria.proyecto_final.R
import com.haria.proyecto_final.sala.SalaScreen
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.perfil.PerfilScreen
import com.haria.proyecto_final.seleccionMusica.MusicaScreen
import com.haria.proyecto_final.main.MainScreen
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.utils.UserSessionManager


/**
 * Configura y gestiona la navegación de la aplicación utilizando Jetpack Navigation.
 *
 * @param context Contexto de la actividad principal.
 * @param navController Controlador de navegación para gestionar las rutas.
 * @param checkAuthentication Indica si se debe verificar la autenticación del usuario.
 * @param musicViewModel ViewModel para gestionar el estado de la música.
 */
@Composable
fun NavigationGraph(
    context: ComponentActivity,
    navController: NavHostController,
    checkAuthentication: Boolean,
    musicViewModel: MusicViewModel
) {
    // Configuración explícita del ImageLoader (opcional pero recomendado para control avanzado)
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                // Prioriza ImageDecoder para AVIF en Android 9+
                add(ImageDecoderDecoder.Factory())
            } else {
                // Fallback a GIF decoder en dispositivos antiguos (si necesitas compatibilidad con GIF)
                add(GifDecoder.Factory())
            }
        }
        .build()
    // Define las rutas de navegación y sus pantallas asociadas.
    NavHost(
        navController = navController,
        startDestination = if (checkAuthentication) "mainScreen" else "loginScreen",
    ) {
        // Pantalla principal.
        composable("mainScreen") {
            MainScreen(context, navController, imageLoader)
        }
        // Pantalla de selección de música.
        composable("musicaScreen") {
            MusicaScreen(context, navController, imageLoader)
        }
        // Pantalla de perfil del usuario.
        composable("perfilScreen") {
            PerfilScreen(context, navController, imageLoader)
        }
        // Pantalla de inicio de sesión.
        composable("loginScreen") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("mainScreen")
                val userId = SupabaseManager.getCurrentUserId()
                if (userId != null) {
                    UserSessionManager.guardarUserId(context, userId)
                }
            })
        }
        // Pantalla de estilo de canción con parámetros.
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
                musicViewModel = musicViewModel,
                imageLoader = imageLoader
            )
        }
        // Pantalla de sala con parámetros.
        composable("salaScreen/{perfilId}") { backStackEntry ->
            val perfilId = backStackEntry.arguments?.getString("perfilId") ?: "default"
            SalaScreen(context, navController, perfilId, musicViewModel, imageLoader)
        }
        // Pantalla de chat propio.
        composable("chatPropioScreen") {
            ChatPropioScreen(context, navController, imageLoader)
        }
    }
}