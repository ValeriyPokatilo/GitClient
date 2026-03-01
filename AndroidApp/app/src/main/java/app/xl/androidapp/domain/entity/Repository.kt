package app.xl.androidapp.domain.entity

data class Repository(
    val id: Long,
    val name: String,
    val owner: Owner,
    val language: String?,
    val languageColor: Int? = null,
    val description: String?,
    val defaultBranch: String
)