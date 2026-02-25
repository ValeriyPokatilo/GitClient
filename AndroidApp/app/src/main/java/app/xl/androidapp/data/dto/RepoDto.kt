package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("owner") val owner: OwnerDto,
    @SerialName("language") val language: String?,
    @SerialName("description") val description: String?,
    @SerialName("default_branch") val defaultBranch: String
)
