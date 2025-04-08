package com.haria.proyecto_final.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haria.proyecto_final.Cancion
import com.haria.proyecto_final.SupabaseManager
import com.haria.proyecto_final.perfil.Perfil

@Composable
fun ContentMain(innerPadding: PaddingValues, context: Context) {
    var cancion by remember { mutableStateOf<Cancion?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            cancion = SupabaseManager.getCancion()
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
        Text("Cancion: ${cancion?.nombre}")
        Text("Estilo: ${cancion?.estilo}")
    }
}