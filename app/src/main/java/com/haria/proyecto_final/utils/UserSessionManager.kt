package com.haria.proyecto_final.utils


import android.content.Context
import androidx.core.content.edit

object UserSessionManager {
    private const val PREF_NAME = "usuario"
    private const val KEY_USER_ID = "user_id"

    fun guardarUserId(context: Context, userId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(KEY_USER_ID, userId) }
    }

    fun obtenerUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_ID, null)
    }

    fun limpiarUserId(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { remove(KEY_USER_ID) }
    }
}