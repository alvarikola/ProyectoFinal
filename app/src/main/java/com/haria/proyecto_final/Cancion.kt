package com.haria.proyecto_final

import kotlinx.serialization.Serializable

@Serializable
data class Cancion(
    val id: String,
    val created_at: String? = null,
    val nombre: String? = null,
    val estilo: String? = null
)