package com.haria.proyecto_final

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.pruebas.MusicService
import com.haria.proyecto_final.SupabaseManager.init
import com.haria.proyecto_final.navigation.NavigationGraph
import com.haria.proyecto_final.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        init()

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val logeado = SupabaseManager.isLoggedIn()
            ProyectoFinalTheme {
                NavigationGraph(this, navController, logeado)
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
    }

    private fun playMusic() {
        val musicUrl = "https://prod-1.storage.jamendo.com/?trackid=1879171&format=mp31&from=GQoxWTIMiLV%2F8Pt0zM4C9g%3D%3D%7CjxNKDeGf%2FsG%2B5bwWJa%2FnDQ%3D%3D"
        val intent = Intent(this, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_MUSIC_URL, musicUrl)
        }
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun pauseMusic() {
        if (isBound && musicService != null) {
            musicService?.pauseMusic()
        }
    }

    private fun stopMusic() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        val intent = Intent(this, MusicService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        super.onDestroy()
    }

}