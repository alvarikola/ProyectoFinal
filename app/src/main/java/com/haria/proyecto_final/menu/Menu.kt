package com.haria.proyecto_final.menu

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.utils.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


@Composable
fun Menu(context: ComponentActivity, navController: NavHostController, drawerState: DrawerState) {
    val colorIcono = MaterialTheme.colorScheme.secondary
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
    if (perfil?.trackid != null) {
        ModalDrawerSheet {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(Modifier.background(MaterialTheme.colorScheme.primary)) {
                    NavigationDrawerItem(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp),
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Tú Chat", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        selected = false,
                        onClick = {},
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = MaterialTheme.colorScheme.primary, // Color de fondo cuando no está seleccionado
                        )
                    )
                }
                Column(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    perfil?.let { Chat(it.id) }
                }
            }
        }
    }
}