package com.haria.proyecto_final.menu

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haria.proyecto_final.R
import com.haria.proyecto_final.SupabaseManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavHostController, main: Boolean = false) {
    val expanded = remember { mutableStateOf(false) } // Estado para abrir y cerrar el DropdownMenu
    val icon = painterResource(id = R.drawable.logo_circular) // Reemplaza con tu recurso de icono
    val scope = rememberCoroutineScope()

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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", Modifier.size(50.dp))
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        )
    )
}