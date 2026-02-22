package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubErrorDto(
    @SerialName("message") val message: String?,
    @SerialName("documentation_url") val documentationUrl: String?,
    @SerialName("status") val status: String? = null
)