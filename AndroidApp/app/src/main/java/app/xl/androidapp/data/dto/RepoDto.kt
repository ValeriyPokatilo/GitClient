package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto(
    val id: Long,
    val name: String,
    val owner: OwnerDto,
    val language: String? = null,
    val description: String? = null,
    @SerialName("default_branch") val defaultBranch: String
)
