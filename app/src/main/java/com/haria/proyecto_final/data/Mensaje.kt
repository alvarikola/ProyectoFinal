package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable


/**
 * Clase de datos que representa un mensaje en el sistema.
 *
 * @property userNombre Nombre del usuario que envió el mensaje (opcional).
 * @property text Contenido del mensaje.
 * @property userId Identificador único del usuario que envió el mensaje.
 */
@Serializable
data class Mensaje(
    val userNombre: String?,
    val text: String,
    val userId: String
)
