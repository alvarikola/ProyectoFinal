package com.haria.proyecto_final.perfil

import androidx.compose.material3.Text
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haria.proyecto_final.SupabaseManager

@Composable
fun ContentPerfil(innerPadding: PaddingValues, context: Context) {

    var perfil by remember { mutableStateOf<Perfil?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            perfil = SupabaseManager.getPerfil()
            Log.i("prueba", "Perfil: ${perfil?.nombre}")
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
        Text("Perfil: ${perfil?.nombre}")
        Text("Perfil: ${perfil?.email}")
        Text("Perfil: ${perfil?.created_at}")
    }
}