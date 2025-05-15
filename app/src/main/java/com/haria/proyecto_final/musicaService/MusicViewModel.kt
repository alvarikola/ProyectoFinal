package com.haria.proyecto_final.musicaService

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haria.proyecto_final.data.Cancion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel para gestionar el estado de la música en la aplicación.
 * Proporciona flujos de estado para la reproducción y la canción actual.
 */
class MusicViewModel : ViewModel() {
    // Flujo mutable que indica si la música está en reproducción.
    private val _isPlaying = MutableStateFlow(false)

    /**
     * Flujo de solo lectura que expone el estado de reproducción de la música.
     */
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // Flujo mutable que contiene la canción actualmente seleccionada.
    private val _currentSong = MutableStateFlow<Cancion?>(null)

    /**
     * Flujo de solo lectura que expone la canción actualmente seleccionada.
     */
    val currentSong: StateFlow<Cancion?> = _currentSong.asStateFlow()

    init {
        // Observar los cambios en MusicServiceEvents
        viewModelScope.launch {
            MusicServiceEvents.isPlayingFlow.collect { playing ->
                _isPlaying.value = playing
            }
        }
    }

    /**
     * Actualiza el estado de reproducción de la música.
     *
     * @param playing Indica si la música está en reproducción (true) o no (false).
     */
    fun setPlaying(playing: Boolean) {
        Log.d("MusicViewModel", "Estado de reproducción cambiado a: $playing")
        _isPlaying.value = playing
    }

    /**
     * Establece la canción actualmente seleccionada.
     *
     * @param cancion Objeto de tipo Cancion que representa la canción actual, o null si no hay ninguna.
     */
    fun setCurrentSong(cancion: Cancion?) {
        Log.d("MusicViewModel", "Canción actual cambiada a: ${cancion?.nombre ?: "ninguna"}")
        _currentSong.value = cancion
    }
}