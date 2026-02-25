package app.xl.androidapp.domain.entity

data class Repo(
    val id: Long,
    val name: String,
    val owner: Owner,
    val language: String?,
    val description: String?
)