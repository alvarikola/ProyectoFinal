package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable


/**
 * Clase de datos que representa una canción.
 *
 * @property id Identificador único de la canción.
 * @property created_at Fecha de creación de la canción en formato de cadena (opcional).
 * @property nombre Nombre de la canción (opcional).
 * @property estilo Estilo o género musical de la canción (opcional).
 * @property cantante Nombre del cantante o intérprete de la canción (opcional).
 * @property imagenUrl URL de la imagen asociada a la canción (opcional).
 */
@Serializable
data class Cancion(
    val id: Int,
    val created_at: String? = null,
    val nombre: String? = null,
    val estilo: String? = null,
    val cantante: String? = null,
    val imagenUrl: String? = null,
)