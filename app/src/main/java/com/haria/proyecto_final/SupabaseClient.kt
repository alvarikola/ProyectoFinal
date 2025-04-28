package com.haria.proyecto_final

import android.util.Log
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Perfil
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json

// Jamendo (app para música)

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
            install(Realtime)
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
            Log.i("Error", "Error al registrar: ${e.message}")
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
                .from("perfil")
                .select(){filter {
                    getCurrentUserId()?.let { eq("id", it) }
                }}
                .decodeSingle<Perfil>()
    }

    suspend fun getCancionesPorEstilo(estilo: String): List<Cancion> {
        return client.postgrest
            .from("cancion")
            .select(){filter {
                eq("estilo", estilo)
            }}
            .decodeList<Cancion>()
    }

    @OptIn(SupabaseExperimental::class)
    suspend fun crearCanal(id: String) {
        val flow: Flow<Perfil> = client.from("perfil").selectSingleValueAsFlow(Perfil::id) {
            eq("id", 1)
        }
        flow.collect {
            println("My country is $it")
        }
    }

    suspend fun actualizarPerfil(perfil: Perfil?): Boolean {
        try {
            // Actualizar los datos en la tabla de perfiles
            client.from("perfil")
                .update({
                    set("nombre", perfil?.nombre)
                    set("pais", perfil?.pais)
                }
                ) {
                    filter {
                        perfil?.id?.let { eq("id", it) }
                    }
                }
            return true
        } catch (e: Exception) {
            Log.i("Error", "Error al actualizar el perfil: ${e.message}")
            return false
        }
    }

    suspend fun getPerfiles(): List<Perfil> {
        return client.postgrest
            .from("perfil")
            .select(){filter {
                getCurrentUserId()?.let { neq("id", it) }
                //eq("emitiendo", true)
            }}
            .decodeList<Perfil>()
    }

    suspend fun getCancionPorId(id: Int): Cancion {
        return client.postgrest
            .from("cancion")
            .select(){filter {
                eq("id", id)
            }}
            .decodeSingle<Cancion>()
    }

    suspend fun establecerCancion(trackid: Int): Boolean {
        try {
            // Actualizar los datos en la tabla de perfiles
            client.from("perfil")
                .update({
                    set("trackid", trackid)
                }
                ) {
                    filter {
                        getCurrentUserId()?.let { eq("id", it) }
                    }
                }
            return true
        } catch (e: Exception) {
            Log.i("Error", "Error al actualizar el perfil: ${e.message}")
            return false
        }
    }
}
