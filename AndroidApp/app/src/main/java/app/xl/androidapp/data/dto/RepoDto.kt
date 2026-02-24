package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class RepoDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String?,
    @SerialName("full_name") val fullName: String?,
    @SerialName("language") val language: String?,
    @SerialName("description") val description: String?
)
