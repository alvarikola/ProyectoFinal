package com.haria.proyecto_final.menu

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun Menu(context: ComponentActivity, navController: NavHostController) {
    val colorIcono = MaterialTheme.colorScheme.primary
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
                            Text(text = "Menú", fontSize = 40.sp, fontWeight = FontWeight.Bold)
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
                NavigationDrawerItem(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Home, contentDescription = "Salas", Modifier.size(35.dp), tint = colorIcono)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Salas", fontSize = 20.sp)
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate("mainScreen")
                    }
                )

                Divider(color = Color.Gray, thickness = 0.5.dp)

                NavigationDrawerItem(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Sala actual", Modifier.size(35.dp), tint = colorIcono)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Sala actual", fontSize = 20.sp)
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate("")
                    }
                )

                Divider(color = Color.Gray, thickness = 0.5.dp)

                NavigationDrawerItem(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Perfil", Modifier.size(35.dp), tint = colorIcono)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Perfil", fontSize = 20.sp)
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate("")
                    }
                )

            }
            Column(Modifier.fillMaxSize() ,verticalArrangement = Arrangement.Bottom) {
                NavigationDrawerItem(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Logout", Modifier.size(35.dp), tint = colorIcono)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Cerrar sesión", fontSize = 20.sp)
                        }
                    },
                    selected = false,
                    onClick = {

                    }
                )
            }
        }
    }
}