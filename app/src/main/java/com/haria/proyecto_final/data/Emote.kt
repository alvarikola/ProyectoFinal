package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable


/**
 * Clase de datos que representa un emote.
 *
 * @property id Identificador único del emote.
 * @property animado Indica si el emote es animado (true) o estático (false).
 * @property nombre Nombre opcional del emote.
 */
@Serializable
data class Emote(
    val id: String,
    val animado: Boolean,
    val nombre: String? = null,
)