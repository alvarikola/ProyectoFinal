package com.haria.proyecto_final.musicaService

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haria.proyecto_final.MusicServiceEvents
import com.haria.proyecto_final.data.Cancion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSong = MutableStateFlow<Cancion?>(null)
    val currentSong: StateFlow<Cancion?> = _currentSong.asStateFlow()

    init {
        // Observar los cambios en MusicServiceEvents
        viewModelScope.launch {
            MusicServiceEvents.isPlayingFlow.collect { playing ->
                _isPlaying.value = playing
            }
        }
    }

    fun setPlaying(playing: Boolean) {
        Log.d("MusicViewModel", "Estado de reproducción cambiado a: $playing")
        _isPlaying.value = playing
    }

    fun setCurrentSong(cancion: Cancion?) {
        Log.d("MusicViewModel", "Canción actual cambiada a: ${cancion?.nombre ?: "ninguna"}")
        _currentSong.value = cancion
    }
}