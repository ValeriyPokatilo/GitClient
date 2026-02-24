package app.xl.androidapp.domain.entity

data class Repo(
    val id: Long,
    val name: String?,
    val fullName: String?,
    val language: String?,
    val description: String?
)