package com.haria.proyecto_final.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = rosa,
    secondary = rosaIcono,
    background = negroFondo,
    primaryContainer = colorTarjetas,
    onPrimary = colorLetra,
)

//private val LightColorScheme = lightColorScheme(
//    primary = rosa400,
//    //secondary = PurpleGrey80,
//    //tertiary = Pink80,
//    background = negroFondo,
//    primaryContainer = colorTarjetas
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

@Composable
fun ProyectoFinalTheme(
    content: @Composable () -> Unit,
) {
    val darkTheme = true // Siempre en modo oscuro

    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}