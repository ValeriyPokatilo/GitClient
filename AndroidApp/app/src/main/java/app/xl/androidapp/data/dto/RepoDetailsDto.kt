package app.xl.androidapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsDto(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val language: String? = null,
    val description: String? = null,
    @SerialName("forks_count") val forksCount: Int,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("subscribers_count") val subscribersCount: Int,
    @SerialName("html_url") val url: String,
    val license: LicenseDto? = null
)
