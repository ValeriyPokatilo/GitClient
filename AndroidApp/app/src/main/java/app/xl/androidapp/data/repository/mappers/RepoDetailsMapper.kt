package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.RepoDetailsDto
import app.xl.androidapp.domain.entity.RepositoryDetails

fun RepoDetailsDto.toEntity(): RepositoryDetails {
    return RepositoryDetails(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        language = this.language,
        description = this.description,
        forksCount = this.forksCount,
        stargazersCount = this.stargazersCount,
        subscribersCount = this.subscribersCount,
        url = this.url,
        license = this.license?.toEntity()
    )
}