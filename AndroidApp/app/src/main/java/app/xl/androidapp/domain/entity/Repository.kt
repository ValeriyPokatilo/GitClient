package app.xl.androidapp.domain.entity

data class Repository(
    val id: Long,
    val name: String,
    val owner: Owner,
    val language: String?,
    val description: String?,
    val defaultBranch: String
)