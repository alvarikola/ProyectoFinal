package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable

@Serializable
data class Perfil(
    val id: String,
    val created_at: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    var pais: String? = null,
)