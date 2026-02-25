package app.xl.androidapp.domain.entity

data class RepoDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val language: String?,
    val description: String?,
    val forksCount: Int,
    val stargazersCount: Int,
    val watchersCount: Int
)
