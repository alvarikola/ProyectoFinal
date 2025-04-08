package com.haria.proyecto_final

import com.haria.proyecto_final.perfil.Perfil
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json


object SupabaseManager {
    private const val SUPABASE_URL = "https://vxyxtbqtbujipctpwjgo.supabase.co"
    private const val SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZ4eXh0YnF0YnVqaXBjdHB3amdvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI0NjgwOTMsImV4cCI6MjA1ODA0NDA5M30.EaXTHXuob_hRCXxDkyqw1oU-hP2Ng2l9JwztyjuA4nM"

    lateinit var client: SupabaseClient
        private set

    fun init(){
        client = createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_API_KEY
        ) {
            //Already the default serializer, but you can provide a custom Json instance (optional):
            defaultSerializer = KotlinXSerializer(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
            install(Auth)
            install(Postgrest)
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun register(email: String, password: String): Boolean {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun logout() {
        try {
            client.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }

    fun getCurrentUserEmail(): String? {
        return client.auth.currentUserOrNull()?.email
    }
    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    suspend fun getPerfil(): Perfil {

        return client.postgrest
                .from("Perfil")
                .select(){filter {
                    getCurrentUserId()?.let { eq("UID", it) }
                }}
                .decodeSingle<Perfil>()



    }
}
