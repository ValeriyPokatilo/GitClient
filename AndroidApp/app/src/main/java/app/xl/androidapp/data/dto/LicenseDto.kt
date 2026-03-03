package app.xl.androidapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LicenseDto(
    val name: String,
    val url: String? = null
)