package com.haria.proyecto_final.utils

import androidx.compose.ui.graphics.Color

fun generateRandomColor(): Color {
    val red = (100..255).random()
    val green = (100..255).random()
    val blue = (100..255).random()
    return Color(red, green, blue)
}