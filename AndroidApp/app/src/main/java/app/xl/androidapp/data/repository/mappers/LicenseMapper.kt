package app.xl.androidapp.data.repository.mappers

import app.xl.androidapp.data.dto.LicenseDto
import app.xl.androidapp.domain.entity.License

fun LicenseDto?.toEntity(): License? {
    return this?.let { License(it.name, it.url) }
}