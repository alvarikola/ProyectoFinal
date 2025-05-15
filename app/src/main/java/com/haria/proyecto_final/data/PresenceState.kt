package com.haria.proyecto_final.data

import kotlinx.serialization.Serializable


/**
 * Clase de datos que representa el estado de presencia de un usuario.
 *
 * @property userId Identificador único del usuario cuyo estado de presencia se está representando.
 */
@Serializable
data class PresenceState(val userId: String)