package app.xl.androidapp.presentation.utils

import android.content.Context
import androidx.core.graphics.toColorInt
import app.xl.androidapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.json.JSONObject

@Singleton
class LanguageColorProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val colorMap: Map<String, Int> by lazy {
        loadColors()
    }

    private fun loadColors(): Map<String, Int> {
        val json = context.assets
            .open("color_map.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = JSONObject(json)

        return jsonObject.keys().asSequence().associateWith { key ->
            jsonObject.getString(key).toColorInt()
        }
    }

    fun getColor(language: String?): Int {
        return language?.let { colorMap[it] } ?: R.color.white
    }
}
