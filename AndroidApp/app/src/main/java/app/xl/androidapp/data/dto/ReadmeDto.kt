package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadmeDto(
    @SerialName("encoding") val encoding: String,
    @SerialName("content") val content: String,
)