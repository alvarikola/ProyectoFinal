package com.haria.proyecto_final.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haria.proyecto_final.R
import com.haria.proyecto_final.supabase.SupabaseManager.login
import com.haria.proyecto_final.supabase.SupabaseManager.register
import kotlinx.coroutines.launch


/**
 * Composable que representa la pantalla de inicio de sesión y registro.
 *
 * @param onLoginSuccess Callback que se ejecuta cuando el inicio de sesión es exitoso.
 */
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") } // Estado para almacenar el correo electrónico ingresado.
    var password by remember { mutableStateOf("") } // Estado para almacenar la contraseña ingresada.
    var confirmPassword by remember { mutableStateOf("") } // Estado para confirmar la contraseña en modo registro.
    var errorMessage by remember { mutableStateOf("") } // Estado para mostrar mensajes de error o éxito.
    var isLoading by remember { mutableStateOf(false) } // Estado para indicar si hay una operación en curso.
    var isRegisterMode by remember { mutableStateOf(false) } // Estado para alternar entre los modos de registro e inicio de sesión.
    val icon = painterResource(id = R.drawable.logo_circular) // Recurso de imagen para el logo de la aplicación.

    val scope = rememberCoroutineScope() // Alcance de corrutinas para manejar operaciones asíncronas.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Muestra el logo de la aplicación.
        Image(
            painter = icon,
            contentDescription = "Icono de aplicación",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(200.dp).padding(bottom = 24.dp),
        )
        // Muestra el título de la pantalla según el modo actual.
        Text(
            if (isRegisterMode) "Registro" else "Log-in",
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo de texto para ingresar el correo electrónico.
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para ingresar la contraseña.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de texto adicional para confirmar la contraseña en modo registro.
        if (isRegisterMode) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para iniciar sesión o registrarse.
        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                scope.launch {
                    if (isRegisterMode) {
                        // Validar que las contraseñas coincidan
                        if (password != confirmPassword) {
                            errorMessage = "Las contraseñas no coinciden"
                            isLoading = false
                            return@launch
                        }

                        // Intentar registrar al usuario.
                        val success = register(email.trim(), password.trim())
                        isLoading = false
                        if (success) {
                            isRegisterMode = false
                            errorMessage = "Registro exitoso. Por favor inicia sesión."
                            email = ""
                            password = ""
                            confirmPassword = ""
                        } else {
                            errorMessage = "Error al registrarse. Intenta con otro correo."
                        }
                    } else {
                        // Intentar iniciar sesión.
                        val success = login(email.trim(), password.trim())
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Error al iniciar sesión. Verifica tus credenciales."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                // Indicador de carga mientras se realiza una operación.
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                // Texto del botón según el modo actual.
                Text(
                    if (isRegisterMode) "Registrarse" else "Iniciar sesión",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto y botón para alternar entre los modos de registro e inicio de sesión.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                if (isRegisterMode) "¿Ya tienes una cuenta? " else "¿No tienes una cuenta? ",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        TextButton(
            onClick = {
                isRegisterMode = !isRegisterMode
                errorMessage = ""
            }
        ) {
            Text(
                if (isRegisterMode) "Iniciar sesión" else "Registrarse",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Muestra mensajes de error o éxito.
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                errorMessage,
                color = if (errorMessage.contains("exitoso")) MaterialTheme.colorScheme.primary else Color.Red
            )
        }
    }
}