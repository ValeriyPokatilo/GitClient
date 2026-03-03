package app.xl.androidapp.data.storage

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit {
            putString(KEY_AUTH_TOKEN, token)
        }
    }

    fun getToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    fun clearToken() {
        prefs.edit {
            remove(KEY_AUTH_TOKEN)
        }
    }
}