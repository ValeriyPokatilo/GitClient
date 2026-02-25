package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.RepoDto
import app.xl.androidapp.domain.entity.Repo

fun RepoDto.toEntity(): Repo {
    return Repo(
        id = this.id,
        name = this.name,
        owner = this.owner.toEntity(),
        language = this.language,
        description = this.description,
        defaultBranch = this.defaultBranch
    )
}