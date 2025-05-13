package com.haria.proyecto_final

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.haria.proyecto_final.supabase.SupabaseManager.init
import com.haria.proyecto_final.musicaService.MusicService
import com.haria.proyecto_final.musicaService.MusicViewModel
import com.haria.proyecto_final.navigation.NavigationGraph
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.ui.theme.ProyectoFinalTheme


class MainActivity : ComponentActivity() {
    private var musicService: MusicService? = null
    private var isBound = false
    private lateinit var musicViewModel: MusicViewModel

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            Log.d("MainActivity", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
            Log.d("MainActivity", "Service disconnected")
        }
    }

    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("MainActivity", "Broadcast recibido: ${intent?.action}")
            when (intent?.action) {
                "PLAY_MUSIC" -> {
                    val musicUrl = intent.getStringExtra("music_url")
                    val startTimeMilis = intent.getLongExtra("start_time_millis", -1L).takeIf { it != -1L }
                    Log.d("MainActivity", "URL de música recibida: $musicUrl")
                    musicUrl?.let {
                        playMusic(it, startTimeMilis)
                        musicViewModel.setPlaying(true)
                        Log.d("MainActivity", musicViewModel.isPlaying.value.toString())
                    }
                }
                "PAUSE_MUSIC" -> {
                    pauseMusic()
                    musicViewModel.setPlaying(false)
                }
                "STOP_MUSIC" -> {
                    stopMusic()
                    musicViewModel.setPlaying(false)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        // Registrar todos los receivers necesarios
        val intentFilter = IntentFilter().apply {
            addAction("PLAY_MUSIC")
            addAction("PAUSE_MUSIC")
            addAction("STOP_MUSIC")
        }
        registerReceiver(musicReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        Log.d("MainActivity", "Receiver registrado")

        // Solicitar permiso de notificación en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        init(applicationContext)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val logeado = SupabaseManager.isLoggedIn()
            ProyectoFinalTheme {
                NavigationGraph(this, navController, logeado, musicViewModel)
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    }

    private fun playMusic(musicUrl: String, startTimeMilis: Long?) {
        Log.d("MainActivity", "Iniciando servicio de música con URL: $musicUrl")
        val intent = Intent(this, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_MUSIC_URL, musicUrl)
            if (startTimeMilis != null) {
                putExtra("start_time_millis", startTimeMilis)
            }
        }
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun pauseMusic() {
        Log.d("MainActivity", "Pausando música")
        if (isBound && musicService != null) {
            musicService?.pauseMusic()
        } else {
            val intent = Intent(this, MusicService::class.java).apply {
                action = MusicService.ACTION_PAUSE
            }
            startService(intent)
        }
    }

    private fun stopMusic() {
        Log.d("MainActivity", "Deteniendo música")
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        val intent = Intent(this, MusicService::class.java).apply {
            action = MusicService.ACTION_STOP
        }
        startService(intent)
        stopService(intent)
    }

    override fun onDestroy() {
        try {
            stopMusic()
            unregisterReceiver(musicReceiver)
            if (isBound) {
                unbindService(serviceConnection)
                isBound = false
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al liberar recursos: ${e.message}")
        }
        super.onDestroy()
    }
}