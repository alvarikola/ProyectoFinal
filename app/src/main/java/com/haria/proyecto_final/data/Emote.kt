package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable

@Serializable
data class Emote(
    val id: String,
    val animado: Boolean,
    val nombre: String? = null,
)