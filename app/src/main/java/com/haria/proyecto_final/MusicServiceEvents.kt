package com.haria.proyecto_final

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MusicServiceEvents {
    // Eventos que emitirá el servicio
    private val _isPlayingFlow = MutableStateFlow(false)
    val isPlayingFlow: StateFlow<Boolean> = _isPlayingFlow.asStateFlow()

    // Métodos para actualizar los estados desde el servicio
    fun setIsPlaying(playing: Boolean) {
        _isPlayingFlow.value = playing
    }

    fun resetState() {
        _isPlayingFlow.value = false
    }
}