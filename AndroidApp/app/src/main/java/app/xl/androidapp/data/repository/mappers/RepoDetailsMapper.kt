package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.RepoDetailsDto
import app.xl.androidapp.domain.entity.RepoDetails

fun RepoDetailsDto.toEntity(): RepoDetails {
    return RepoDetails(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        language = this.language,
        description = this.description,
        forksCount = this.forksCount,
        stargazersCount = this.stargazersCount,
        watchersCount = this.watchersCount
    )
}