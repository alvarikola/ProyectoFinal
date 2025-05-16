package com.haria.proyecto_final.supabase

import android.content.Context
import android.util.Log
import com.haria.proyecto_final.data.Cancion
import com.haria.proyecto_final.data.Emote
import com.haria.proyecto_final.data.Perfil
import com.haria.proyecto_final.utils.UserSessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

// Jamendo (app para música)

/**
 * Objeto que gestiona la interacción con Supabase, incluyendo autenticación,
 * operaciones en la base de datos y escucha de cambios en tiempo real.
 */
object SupabaseManager {
    private const val SUPABASE_URL = "https://vxyxtbqtbujipctpwjgo.supabase.co"
    private const val SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZ4eXh0YnF0YnVqaXBjdHB3amdvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI0NjgwOTMsImV4cCI6MjA1ODA0NDA5M30.EaXTHXuob_hRCXxDkyqw1oU-hP2Ng2l9JwztyjuA4nM"

    lateinit var client: SupabaseClient
        private set

    private lateinit var appContext: Context

    /**
     * Inicializa el cliente de Supabase con el contexto de la aplicación.
     *
     * @param context Contexto de la aplicación.
     */
    fun init(context: Context){
        appContext = context.applicationContext
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

    /**
     * Inicia sesión en Supabase con un correo electrónico y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return `true` si el inicio de sesión fue exitoso, `false` en caso contrario.
     */
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

    /**
     * Registra un nuevo usuario en Supabase con un correo electrónico y contraseña.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return `true` si el registro fue exitoso, `false` en caso contrario.
     */
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

    /**
     * Cierra la sesión del usuario actual en Supabase.
     */
    suspend fun logout() {
        try {
            client.auth.signOut()
            UserSessionManager.limpiarUserId(appContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Verifica si hay un usuario autenticado en Supabase.
     *
     * @return `true` si hay un usuario autenticado, `false` en caso contrario.
     */
    fun isLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }

    /**
     * Obtiene el ID del usuario autenticado actualmente.
     *
     * @return ID del usuario autenticado o `null` si no hay usuario autenticado.
     */
    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    /**
     * Obtiene el perfil del usuario autenticado actualmente.
     *
     * @return Objeto `Perfil` del usuario autenticado.
     */
    suspend fun getPerfil(): Perfil {

        return client.postgrest
                .from("perfil")
                .select(){filter {
                    getCurrentUserId()?.let { eq("id", it) }
                }}
                .decodeSingle<Perfil>()
    }

    /**
     * Obtiene el perfil de un usuario por su ID.
     *
     * @param perfilId ID del perfil a buscar.
     * @return Objeto `Perfil` correspondiente al ID proporcionado.
     */
    suspend fun getPerfilPorId(perfilId: String): Perfil {

        return client.postgrest
            .from("perfil")
            .select(){filter {
                eq("id", perfilId)
            }}
            .decodeSingle<Perfil>()
    }

    /**
     * Obtiene una lista de canciones filtradas por estilo.
     *
     * @param estilo Estilo de música a filtrar.
     * @return Lista de objetos `Cancion` que coinciden con el estilo.
     */
    suspend fun getCancionesPorEstilo(estilo: String): List<Cancion> {
        return client.postgrest
            .from("cancion")
            .select(){filter {
                eq("estilo", estilo)
            }}
            .decodeList<Cancion>()
    }

    /**
     * Actualiza el perfil del usuario con los datos proporcionados.
     *
     * @param perfil Objeto `Perfil` con los datos actualizados.
     * @return `true` si la actualización fue exitosa, `false` en caso contrario.
     */
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

    /**
     * Obtiene una lista de perfiles excluyendo al usuario autenticado.
     *
     * @return Lista de objetos `Perfil` de otros usuarios.
     */
    suspend fun getPerfiles(): List<Perfil> {
        return client.postgrest
            .from("perfil")
            .select(){filter {
                getCurrentUserId()?.let { neq("id", it) }
            }}
            .decodeList<Perfil>()
    }

    /**
     * Obtiene una canción por su ID.
     *
     * @param id ID de la canción a buscar.
     * @return Objeto `Cancion` correspondiente al ID proporcionado.
     */
    suspend fun getCancionPorId(id: Int): Cancion {
        return client.postgrest
            .from("cancion")
            .select(){filter {
                eq("id", id)
            }}
            .decodeSingle<Cancion>()
    }

    /**
     * Establece el track ID de una canción en el perfil del usuario autenticado.
     *
     * @param trackid ID del track a establecer.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    suspend fun establecerCancion(trackid: Int?): Boolean {
        val userId = UserSessionManager.obtenerUserId(appContext)
        Log.d("SupabaseManager", "Usuario ID obtenido: $userId") // Log adicional
        if (userId == null) {
            Log.e("Supabase", "No hay usuario autenticado. No se actualizó el trackId.")
            return false
        }
        try {
            client.from("perfil")
                .update({ set("trackid", trackid) }) {
                    filter { eq("id", userId) }
                }
            Log.e("Supabase", "Track ID actualizado: $trackid")
            Log.e("Supabase", "cliente $client.")
            return true
        } catch (e: Exception) {
            Log.e("Supabase", "Error al actualizar el perfil: ${e.message}", e)
            return false
        }
    }

    /**
     * Establece un emote en el perfil del usuario autenticado.
     *
     * @param emoteId ID del emote a establecer.
     * @return `true` si la operación fue exitosa, `false` en caso contrario.
     */
    suspend fun establecerEmote(emoteId: String): Boolean {
        val userId = UserSessionManager.obtenerUserId(appContext)
        if (userId == null) {
            return false
        }
        try {
            client.from("perfil")
                .update({ set("emoteid", emoteId) }) {
                    filter { eq("id", userId) }
                }
            Log.e("Supabase", "Emote actualizado: $emoteId")
            return true
        } catch (e: Exception) {
            Log.e("Supabase", "Error al actualizar el perfil: ${e.message}", e)
            return false
        }
    }

    /**
     * Escucha cambios en el perfil de un usuario específico.
     *
     * @param id ID del perfil a escuchar.
     * @param onProfileUpdate Callback que se ejecuta cuando el perfil se actualiza.
     */
    @OptIn(SupabaseExperimental::class)
    suspend fun escucharCambiosPerfil(id: String, onProfileUpdate: (Perfil) -> Unit) {
        val flow: Flow<Perfil> = client.from("perfil").selectSingleValueAsFlow(Perfil::id) {
            eq("id", id)
        }
        flow.collect {
           onProfileUpdate(it)
        }
    }

    /**
     * Escucha cambios en los perfiles de otros usuarios.
     *
     * @return Flujo de una lista de objetos `Perfil` actualizados.
     */
    @OptIn(SupabaseExperimental::class)
    fun escucharCambiosPerfiles(): Flow<List<Perfil>> {
        return client.from("perfil")
            .selectAsFlow(Perfil::id, filter = FilterOperation("id", FilterOperator.NEQ, getCurrentUserId() ?: ""))
    }

    /**
     * Obtiene un canal de Supabase por su nombre.
     *
     * @param nombre Nombre del canal.
     * @return Objeto del canal correspondiente.
     */
    fun obtenerCanal(nombre: String) = client.channel(nombre)

    /**
     * Obtiene una lista de emotes estáticos.
     *
     * @return Lista de objetos `Emote` que no son animados.
     */
    suspend fun getEmotesEstaticos(): List<Emote> {
        return client.postgrest
            .from("emote")
            .select(){filter {
                eq("animado", false)
            }
            }
            .decodeList<Emote>()
    }

    /**
     * Obtiene una lista de emotes animados.
     *
     * @return Lista de objetos `Emote` que son animados.
     */
    suspend fun getEmotesAnimados(): List<Emote> {
        return client.postgrest
            .from("emote")
            .select(){filter {
                eq("animado", true)
            }
            }
            .decodeList<Emote>()
    }
}
