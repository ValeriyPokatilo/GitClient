package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.RepoDto
import app.xl.androidapp.domain.entity.Repository

fun RepoDto.toEntity(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        owner = this.owner.toEntity(),
        language = this.language,
        description = this.description,
        defaultBranch = this.defaultBranch
    )
}