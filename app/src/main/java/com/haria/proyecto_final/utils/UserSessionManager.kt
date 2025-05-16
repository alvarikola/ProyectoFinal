package com.haria.proyecto_final.utils


import android.content.Context
import androidx.core.content.edit


/**
 * Objeto que gestiona la sesión del usuario mediante el almacenamiento de datos en preferencias compartidas.
 */
object UserSessionManager {
    // Nombre del archivo de preferencias compartidas.
    private const val PREF_NAME = "usuario"
    // Clave para almacenar el ID del usuario.
    private const val KEY_USER_ID = "user_id"

    /**
     * Guarda el ID del usuario en las preferencias compartidas.
     *
     * @param context Contexto de la aplicación necesario para acceder a las preferencias.
     * @param userId ID del usuario que se desea guardar.
     */
    fun guardarUserId(context: Context, userId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(KEY_USER_ID, userId) }
    }

    /**
     * Obtiene el ID del usuario almacenado en las preferencias compartidas.
     *
     * @param context Contexto de la aplicación necesario para acceder a las preferencias.
     * @return El ID del usuario si existe, o `null` si no está almacenado.
     */
    fun obtenerUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_ID, null)
    }

    /**
     * Elimina el ID del usuario de las preferencias compartidas.
     *
     * @param context Contexto de la aplicación necesario para acceder a las preferencias.
     */
    fun limpiarUserId(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { remove(KEY_USER_ID) }
    }
}