package com.haria.proyecto_final.musicaService

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import com.haria.proyecto_final.MainActivity
import com.haria.proyecto_final.MusicServiceEvents
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MusicService : Service() {
    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null


    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d("MusicService", "Servicio creado")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            ACTION_PLAY -> {
                val musicUrl = intent.getStringExtra(EXTRA_MUSIC_URL)
                val startTimeMillis = intent.getLongExtra("start_time_millis", -1L).takeIf { it != -1L }
                Log.d("MusicService", "URL de música recibida en servicio: $musicUrl")
                if (!musicUrl.isNullOrEmpty()) {
                    playMusic(musicUrl, startTimeMillis)
                }
            }
            ACTION_PAUSE -> pauseMusic()
            ACTION_STOP -> {
                stopMusicAndClearTrack()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    fun getCurrentPositionInLoop(startTimeMillis: Long): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTimeMillis = currentTimeMillis - startTimeMillis

        val durationMillis = mediaPlayer?.duration

        Log.i("MusicService", "Duración: $durationMillis")
        Log.i("MusicService", "elapsedTimeMillis: $elapsedTimeMillis")
        if (durationMillis != null) {
            if (durationMillis <= 0) return 0
        } // Evitar división por cero o errores

        val currentPositionInLoopMillis = elapsedTimeMillis % durationMillis!!
        Log.i("MusicService", "currentPositionInLoopMillis: $currentPositionInLoopMillis")
        return (currentPositionInLoopMillis / 1000).toInt() // Devolvemos los segundos
    }

    fun seekToCurrentLoopPosition(startTimeMillis: Long) {
        mediaPlayer?.let {
            val position = getCurrentPositionInLoop(startTimeMillis) * 1000
            if (it.isPlaying || it.isLooping) {
                it.seekTo(position)
            } else {
                it.seekTo(position)
                it.start()
            }
        }
    }

    fun playMusic(url: String, startTimeMillis: Long? = null) {
        Log.d("MusicService", "Intentando reproducir música: $url")
        // Si ya hay un MediaPlayer activo y es la misma URL, reanudamos
        if (mediaPlayer != null && url == currentUrl && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            updateNotification("Reproduciendo música")
            MusicServiceEvents.setIsPlaying(true)
            return
        }

        // Si es una nueva URL o no hay MediaPlayer, lo creamos
        releaseMediaPlayer()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            try {
                setDataSource(url)
                Log.d("MusicService", "Preparando MediaPlayer con URL: $url")
                prepareAsync()
                setOnPreparedListener {
                    if (startTimeMillis != null) {
                        seekToCurrentLoopPosition(startTimeMillis)
                    } else {
                        it.start()
                    }
                    currentUrl = url
                    MusicServiceEvents.setIsPlaying(true)
                    updateNotification("Reproduciendo música")
                }
                setOnCompletionListener {
                    Log.d("MusicService", "Reproducción completada")
                    updateNotification("Reproducción completada")
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("MusicService", "Error en MediaPlayer: $what, $extra")
                    updateNotification("Error al reproducir")
                    false
                }
            } catch (e: Exception) {
                Log.e("MusicService", "Error al preparar MediaPlayer: ${e.message}")
                e.printStackTrace()
                updateNotification("Error al preparar la reproducción")
            }
        }
    }

    fun pauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                Log.d("MusicService", "Pausando MediaPlayer")
                it.pause()
                MusicServiceEvents.setIsPlaying(false)
                updateNotification("Música en pausa")
            }
        }
    }

    fun stopMusic() {
        Log.d("MusicService", "Deteniendo y liberando MediaPlayer")
        releaseMediaPlayer()
        stopForeground(true)
        MusicServiceEvents.resetState()

    }

    fun stopMusicAndClearTrack() {
        stopMusic() // Detiene la reproducción y libera recursos
        try {
            // Ejecutar de forma sincrónica para asegurar la actualización
            val result = runBlocking { SupabaseManager.establecerCancion(null) }
            if (result) {
                Log.d("MusicService", "TrackID establecido como null correctamente")
            } else {
                Log.e("MusicService", "No se pudo establecer el trackID como null")
            }
        } catch (e: Exception) {
            Log.e("MusicService", "Error al establecer trackID como null: ${e.message}")
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
            Log.d("MusicService", "MediaPlayer liberado")
        }
        mediaPlayer = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Reproductor de Música",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para el servicio de reproducción de música"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("MusicService", "Canal de notificación creado")
        }
    }

    private fun updateNotification(contentText: String) {
        Log.d("MusicService", "Actualizando notificación: $contentText")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent(this, MusicService::class.java).apply {
            action = ACTION_PLAY
            putExtra(EXTRA_MUSIC_URL, currentUrl)
        }
        val playPendingIntent = PendingIntent.getService(
            this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val pauseIntent = Intent(this, MusicService::class.java).apply {
            action = ACTION_PAUSE
        }
        val pausePendingIntent = PendingIntent.getService(
            this, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, MusicService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reproductor de Música")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_music_note)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_play, "Reproducir", playPendingIntent)
            .addAction(R.drawable.ic_pause, "Pausar", pausePendingIntent)
            .addAction(R.drawable.ic_stop, "Detener", stopPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            startForeground(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e("MusicService", "Error al mostrar notificación: ${e.message}")
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("MusicService", "Service bound")
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("MusicService", "onTaskRemoved: app cerrada desde recientes")
        stopMusicAndClearTrack()
        stopSelf() // Asegura que el servicio se detenga
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Log.d("MusicService", "Servicio destruido")
        stopMusicAndClearTrack() // Limpia el trackId al destruir
        super.onDestroy()
    }

    companion object {
        const val ACTION_PLAY = "com.haria.proyecto_final.ACTION_PLAY"
        const val ACTION_PAUSE = "com.haria.proyecto_final.ACTION_PAUSE"
        const val ACTION_STOP = "com.haria.proyecto_final.ACTION_STOP"
        const val EXTRA_MUSIC_URL = "com.haria.proyecto_final.EXTRA_MUSIC_URL"
        private const val CHANNEL_ID = "MusicPlayerServiceChannel"
        private const val NOTIFICATION_ID = 1
    }
}