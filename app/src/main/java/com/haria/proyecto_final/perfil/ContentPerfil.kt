package com.haria.proyecto_final.perfil

import androidx.compose.material3.Text
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haria.proyecto_final.SupabaseManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentPerfil(innerPadding: PaddingValues, context: Context) {



    val formatoEntrada = DateTimeFormatter.ISO_DATE_TIME
    val formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    var perfil by remember { mutableStateOf<Perfil?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfil()
        } catch (
            e: Exception
        ) {
            Log.e("Error", "Error al obtener el perfil: ${e.message}")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "main",
                Modifier.size(170.dp).padding(end = 8.dp),
            )
            Text("${perfil?.nombre}", fontSize = 60.sp, fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            OutlinedButton(
                onClick = {}
            ) {
                Text("Editar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
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
            val fechaFormateada = perfil?.created_at?.let {
                LocalDateTime.parse(it, formatoEntrada).format(formatoSalida)
            }
            Text("Fecha de creación de la cuenta: $fechaFormateada", fontSize = 20.sp)
        }
    }
}