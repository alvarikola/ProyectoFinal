package com.haria.proyecto_final

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.utils.Chat

@Composable
fun ChatPropioContent(innerPadding: PaddingValues, context: ComponentActivity) {

    var perfil by remember { mutableStateOf<Perfil?>(null) }

    LaunchedEffect(true) {
        perfil = SupabaseManager.getPerfil()
    }

    // Escucha cambios en tiempo real
    LaunchedEffect(perfil?.id) {
        try {
            perfil?.id?.let {
                SupabaseManager.escucharCambiosPerfil(it) { nuevoPerfil ->
                    perfil = nuevoPerfil
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al escuchar cambios en el perfil: ${e.message}", e)
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(innerPadding).fillMaxSize()
    ) {
        Column(
            Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Tú Chat", fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
            perfil?.let { Chat(it.id) }
        }
    }
}