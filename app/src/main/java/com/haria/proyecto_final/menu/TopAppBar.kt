package com.haria.proyecto_final.menu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.estiloCancion.PlayerAction
import com.haria.proyecto_final.musicaService.MusicService
import com.haria.proyecto_final.utils.BotonFlotante
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onNavigationClick: () -> Unit = {}, navController: NavHostController, main: Boolean = false, salaPropia: Boolean = false) {
    val expanded = remember { mutableStateOf(false) } // Estado para abrir y cerrar el DropdownMenu
    val icon = painterResource(id = R.drawable.logo_circular) // Reemplaza con tu recurso de icono
    val scope = rememberCoroutineScope()
    var perfil by remember { mutableStateOf<Perfil?>(null) }

    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfil()
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener el perfil: ${e.message}")
        }
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

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        title = {
            if (main) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Image(
                        painter = icon,
                        contentDescription = "Icono de aplicación",
                        Modifier.size(60.dp),
                    )
                    // Ícono de perfil
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Perfil", Modifier.size(50.dp))
                        // DropdownMenu que se abre al hacer clic en el ícono de perfil
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false } // Cierra el menú si se hace clic fuera
                        ) {
                            DropdownMenuItem(
                                text = { Text("Tu sala") },
                                onClick = {
                                    navController.navigate("musicaScreen")
                                    expanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Editar perfil") },
                                onClick = {
                                    navController.navigate("perfilScreen")
                                    expanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = {
                                    scope.launch {
                                        val job = launch {
                                            MusicService.ACTION_STOP
                                            SupabaseManager.establecerCancion(null)
                                        }
                                        job.join() // Espera a que termine
                                        SupabaseManager.logout()
                                        navController.navigate("loginScreen")
                                    }
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", Modifier.size(50.dp))
                    }
                    if(perfil?.trackid != null && !salaPropia) {
                        BotonFlotante(
                            onClick = {
                                onNavigationClick()
                            },
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        )
    )
}
