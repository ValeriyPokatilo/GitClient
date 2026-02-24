package app.xl.androidapp.data.repository

import app.xl.androidapp.data.dto.RepoDto
import app.xl.androidapp.domain.entity.Repo

fun RepoDto.toEntity(): Repo {
    return Repo(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        language = this.language,
        description = this.description
    )
}