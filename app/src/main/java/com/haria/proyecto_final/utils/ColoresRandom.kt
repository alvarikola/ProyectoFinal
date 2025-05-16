package com.haria.proyecto_final.utils

import androidx.compose.ui.graphics.Color


/**
 * Genera un color aleatorio dentro de un rango específico.
 *
 * @return Un objeto `Color` con valores aleatorios para los componentes rojo, verde y azul.
 */
fun generateRandomColor(): Color {
    val red = (100..255).random()
    val green = (100..255).random()
    val blue = (100..255).random()
    return Color(red, green, blue)
}