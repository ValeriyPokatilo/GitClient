package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.OwnerDto
import app.xl.androidapp.domain.entity.Owner

fun OwnerDto.toEntity(): Owner {
    return Owner(
        id = this.id,
        login = this.login
    )
}