package app.xl.androidapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadmeDto(
    val encoding: String,
    val content: String
)