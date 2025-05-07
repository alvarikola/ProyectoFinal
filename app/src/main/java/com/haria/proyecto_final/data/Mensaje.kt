package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable

@Serializable
data class Mensaje(
    val userNombre: String?,
    val text: String,
    val userId: String
)
