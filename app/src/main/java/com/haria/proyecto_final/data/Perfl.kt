package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable


/**
 * Clase de datos que representa un perfil de usuario.
 *
 * @property id Identificador único del perfil.
 * @property created_at Fecha de creación del perfil en formato de cadena (opcional).
 * @property nombre Nombre del usuario asociado al perfil (opcional).
 * @property email Dirección de correo electrónico del usuario (opcional).
 * @property pais País del usuario (opcional).
 * @property fecha_inicio_cancion Fecha de inicio de la canción en formato de cadena (opcional).
 * @property trackid Identificador único de la cancion que escucha el perfil (opcional).
 * @property emoteid Identificador único del emote asociado a la foto del perfil (opcional).
 */
@Serializable
data class Perfil(
    val id: String,
    val created_at: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    var pais: String? = null,
    var fecha_inicio_cancion: String? = null,
    var trackid: Int? = null,
    var emoteid: String? = null,
)