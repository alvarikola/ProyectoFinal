package com.haria.proyecto_final.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Composable que muestra un indicador de carga centrado en la pantalla.
 *
 * Este componente utiliza un `CircularProgressIndicator` para indicar que una operación está en progreso.
 *
 * @constructor No requiere parámetros.
 */
@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}