package com.haria.proyecto_final

import android.util.Log
import androidx.lifecycle.ViewModel
import com.haria.proyecto_final.data.Cancion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicViewModel : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSong = MutableStateFlow<Cancion?>(null)
    val currentSong: StateFlow<Cancion?> = _currentSong.asStateFlow()

    fun setPlaying(playing: Boolean) {
        Log.d("MusicViewModel", "Estado de reproducción cambiado a: $playing")
        _isPlaying.value = playing
    }

    fun setCurrentSong(cancion: Cancion?) {
        Log.d("MusicViewModel", "Canción actual cambiada a: ${cancion?.nombre ?: "ninguna"}")
        _currentSong.value = cancion
    }
}