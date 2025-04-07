package com.haria.proyecto_final

import kotlinx.serialization.Serializable

@Serializable
data class Perfil(
    val UID: String,
    val created_at: String? = null,
    val nombre: String? = null,
    val email: String? = null
)