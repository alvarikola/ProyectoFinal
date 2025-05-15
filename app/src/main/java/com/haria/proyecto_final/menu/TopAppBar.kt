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
import coil.ImageLoader
import com.haria.proyecto_final.R
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.musicaService.MusicService
import com.haria.proyecto_final.utils.AVIFEmoteStatic
import com.haria.proyecto_final.utils.BotonFlotante
import kotlinx.coroutines.launch


/**
 * Composable que representa una barra superior personalizada para la aplicación.
 *
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param main Indica si la barra superior pertenece a la pantalla principal.
 * @param salaPropia Indica si el usuario está en su propia sala.
 * @param imageLoader Cargador de imágenes para mostrar emotes personalizados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavHostController, main: Boolean = false, salaPropia: Boolean = false, imageLoader: ImageLoader) {
    // Estado para abrir y cerrar el DropdownMenu
    val expanded = remember { mutableStateOf(false) }
    // Recurso de imagen para el icono de la aplicación.
    val icon = painterResource(id = R.drawable.logo_circular)
    val scope = rememberCoroutineScope()
    // Estado para almacenar el perfil del usuario.
    var perfil by remember { mutableStateOf<Perfil?>(null) }

    // Efecto lanzado al inicializar el Composable para obtener el perfil del usuario.
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
                // Diseño de la barra superior para la pantalla principal.
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    // Muestra el icono de la aplicación.
                    Image(
                        painter = icon,
                        contentDescription = "Icono de aplicación",
                        Modifier.size(60.dp),
                    )
                    // Botón de perfil con menú desplegable.
                    IconButton(onClick = { expanded.value = !expanded.value }, modifier = Modifier.size(50.dp)) {
                        if(perfil?.emoteid == null) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Perfil",
                                Modifier.size(50.dp)
                            )
                        }
                        else {
                            AVIFEmoteStatic(perfil?.emoteid!!, 50, imageLoader)
                        }
                        // Menú desplegable con opciones de navegación.
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
                // Diseño de la barra superior para otras pantallas.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Botón para volver a la pantalla anterior.
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", Modifier.size(50.dp))
                    }
                    // Botón flotante para navegar a la pantalla de chat propio si corresponde.
                    if(perfil?.trackid != null && !salaPropia) {
                        BotonFlotante(
                            onClick = {
                                navController.navigate("chatPropioScreen")
                            },
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        // Comportamiento de desplazamiento de la barra superior.
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        )
    )
}
