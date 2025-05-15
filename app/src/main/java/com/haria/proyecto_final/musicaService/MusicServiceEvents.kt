package com.haria.proyecto_final.musicaService

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * Objeto que gestiona los eventos relacionados con el servicio de música.
 * Proporciona un flujo de estado para indicar si la música está en reproducción.
 */
object MusicServiceEvents {
    // Flujo mutable que indica si la música está en reproducción.
    private val _isPlayingFlow = MutableStateFlow(false)
    /**
     * Flujo de solo lectura que expone el estado de reproducción de la música.
     */
    val isPlayingFlow: StateFlow<Boolean> = _isPlayingFlow.asStateFlow()

    /**
     * Actualiza el estado de reproducción de la música.
     *
     * @param playing Indica si la música está en reproducción (true) o no (false).
     */
    fun setIsPlaying(playing: Boolean) {
        _isPlayingFlow.value = playing
    }

    /**
     * Restablece el estado de reproducción a su valor inicial (false).
     */
    fun resetState() {
        _isPlayingFlow.value = false
    }
}