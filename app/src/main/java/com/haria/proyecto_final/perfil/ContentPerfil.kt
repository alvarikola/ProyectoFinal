package com.haria.proyecto_final.perfil

import androidx.compose.material3.Text
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.haria.proyecto_final.supabase.SupabaseManager
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.utils.AVIFEmoteStatic
import com.haria.proyecto_final.utils.Loading
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Composable que representa el contenido de la pantalla de perfil del usuario.
 *
 * @param innerPadding Espaciado interno para ajustar el diseño.
 * @param context Contexto de la aplicación.
 * @param emotes Lista de emotes disponibles para seleccionar.
 * @param imageLoader Cargador de imágenes para mostrar emotes personalizados.
 */
@Composable
fun ContentPerfil(innerPadding: PaddingValues, context: Context, emotes: List<Emote>, imageLoader: ImageLoader) {
    // Formato de entrada para fechas.
    val formatoEntrada = DateTimeFormatter.ISO_DATE_TIME
    // Formato de salida para fechas.
    val formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Estado del perfil del usuario.
    var perfil by remember { mutableStateOf<Perfil?>(null) }
    // Estado para alternar entre modo edición y visualización.
    var isEditing by remember { mutableStateOf(false) }
    // Nombre editado por el usuario.
    var nombreEditado by remember { mutableStateOf("") }
    // País editado por el usuario.
    var paisEditado by remember { mutableStateOf("") }
    // Estado para el menú desplegable de países.
    var expandedPaisMenu by remember { mutableStateOf(false) }
    // Estado para el menú desplegable de emotes.
    var expandedEmoteMenu by remember { mutableStateOf(false) }

    // Alcance de corrutinas para operaciones asíncronas.
    val scope = rememberCoroutineScope()


    // Lista de países para el menú desplegable.
    val paises = listOf("España", "México", "Argentina", "Colombia", "Chile", "Perú", "Ecuador", "Venezuela", "Uruguay", "Bolivia", "Otro")

    // Efecto lanzado al inicializar el Composable para obtener el perfil del usuario.
    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfil()
            nombreEditado = perfil?.nombre ?: ""
            paisEditado = perfil?.pais ?: ""
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

    if (perfil == null) {
        // Muestra un indicador de carga mientras se obtiene el perfil.
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isEditing) {
                // Modo visualización
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (perfil?.emoteid == null) {
                        // Muestra un ícono genérico si no hay emote seleccionado.
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "perfil",
                            modifier = Modifier
                                .size(170.dp)
                                .padding(end = 8.dp),
                        )
                    }
                    else {
                        // Muestra el emote seleccionado
                        AVIFEmoteStatic(perfil?.emoteid!!, 170, imageLoader)
                    }
                    Text(perfil?.nombre ?: "Nombre", fontSize = 50.sp, fontWeight = FontWeight.Bold, lineHeight = 60.sp)
                }

                // Botón para activar el modo edición.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                ) {
                    OutlinedButton(
                        onClick = { isEditing = true }
                    ) {
                        Text("Editar", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                // Información del usuario
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text("Datos del usuario", fontSize = 35.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text("Correo electrónico: ${perfil?.email}", fontSize = 20.sp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text("País: ${perfil?.pais ?: "No especificado"}", fontSize = 20.sp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    val fechaFormateada = perfil?.created_at?.let {
                        LocalDateTime.parse(it, formatoEntrada).format(formatoSalida)
                    }
                    Text("Fecha de creación de la cuenta: $fechaFormateada", fontSize = 20.sp)
                }
            } else {
                // Modo edición del perfil
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Editar Perfil",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Imagen seleccionable (reemplaza al ícono)
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 16.dp)
                                .clickable {
                                    expandedEmoteMenu = true // Abrir menú de emotes
                                }
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        ) {
                            if (perfil?.emoteid != null) {
                                // Muestra la imagen del emote seleccionado
                                AVIFEmoteStatic(perfil?.emoteid!!, 150, imageLoader)
                            } else {
                                // Muestra un ícono genérico si no hay emote seleccionado.
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Icono de perfil",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Menú desplegable de emotes
                        DropdownMenu(
                            expanded = expandedEmoteMenu,
                            onDismissRequest = { expandedEmoteMenu = false },
                        ) {
                            emotes.forEach { emote ->
                                DropdownMenuItem(
                                    text = { AVIFEmoteStatic(emote.id, 100, imageLoader) },
                                    onClick = {
                                        scope.launch {
                                            SupabaseManager.establecerEmote(emote.id)
                                        }
                                        expandedEmoteMenu = false
                                    },
                                    modifier = Modifier.height(100.dp)
                                )
                            }
                        }

                        // Campo para editar nombre
                        OutlinedTextField(
                            value = nombreEditado,
                            onValueChange = { nombreEditado = it },
                            label = { Text("Nombre") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )

                        // Campo para seleccionar país
                        OutlinedTextField(
                            value = paisEditado,
                            onValueChange = { paisEditado = it },
                            label = { Text("País") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Seleccionar país",
                                    Modifier.clickable {
                                        expandedPaisMenu = true
                                    },
                                )
                            }
                        )

                        // Menú desplegable para seleccionar el país.
                        DropdownMenu(
                            expanded = expandedPaisMenu,
                            onDismissRequest = { expandedPaisMenu = false },
                        ) {
                            paises.forEach { pais ->
                                DropdownMenuItem(
                                    text = { Text(pais) },
                                    onClick = {
                                        paisEditado = pais
                                        expandedPaisMenu = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botones de acción para guardar o cancelar cambios.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { isEditing = false }
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = {
                                    // Guarda los cambios en la base de datos.
                                    perfil = perfil?.copy(
                                        nombre = nombreEditado,
                                        pais = paisEditado
                                    )

                                    try {
                                        scope.launch {
                                            SupabaseManager.actualizarPerfil(perfil)
                                            Log.d("Perfil", "Perfil actualizado: $perfil")
                                        }
                                        Log.d("Perfil", "Guardando perfil: $perfil")
                                        isEditing = false
                                    } catch (e: Exception) {
                                        Log.e("Error", "Error al actualizar el perfil: ${e.message}")
                                    }
                                }
                            ) {
                                Text("Guardar cambios")
                            }
                        }
                    }
                }
            }
        }
    }
}

