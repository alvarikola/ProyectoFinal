package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable

@Serializable
data class Cancion(
    val id: Int,
    val created_at: String? = null,
    val nombre: String? = null,
    val estilo: String? = null,
    val cantante: String? = null,
    val imagenUrl: String? = null,
)