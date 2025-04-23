package com.haria.proyecto_final.perfil

import androidx.compose.material3.Text
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.utils.Loading
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ContentPerfil(innerPadding: PaddingValues, context: Context) {
    val formatoEntrada = DateTimeFormatter.ISO_DATE_TIME
    val formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    var perfil by remember { mutableStateOf<Perfil?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var nombreEditado by remember { mutableStateOf("") }
    var paisEditado by remember { mutableStateOf("") }
    var expandedPaisMenu by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    // Lista de países para el dropdown
    val paises = listOf("España", "México", "Argentina", "Colombia", "Chile", "Perú", "Ecuador", "Venezuela", "Uruguay", "Bolivia", "Otro")

    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfil()
            nombreEditado = perfil?.nombre ?: ""
            paisEditado = perfil?.pais ?: ""
        } catch (e: Exception) {
            Log.e("Error", "Error al obtener el perfil: ${e.message}")
        }
    }

    if (perfil == null) {
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
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "perfil",
                        modifier = Modifier
                            .size(170.dp)
                            .padding(end = 8.dp),
                    )
                    Text(perfil?.nombre ?: "Nombre", fontSize = 50.sp, fontWeight = FontWeight.Bold)
                }

                // Botón de editar
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
                    Text("Datos del usuario", fontSize = 35.sp, fontWeight = FontWeight.Bold)
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
                // Modo edición
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

                        // Icono de perfil (no editable)
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Icono de perfil",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 16.dp)
                        )

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

                        // Botones de acción
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
                                    // Aquí guardar los cambios en la base de datos
                                    perfil = perfil?.copy(
                                        nombre = nombreEditado,
                                        pais = paisEditado
                                    )

                                    // Simular la llamada a Supabase para guardar
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