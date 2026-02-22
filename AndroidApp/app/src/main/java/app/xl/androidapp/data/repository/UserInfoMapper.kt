package app.xl.androidapp.data.repository

import app.xl.androidapp.data.dto.UserInfoDto
import app.xl.androidapp.domain.entity.UserInfo

fun UserInfoDto.toEntity(): UserInfo {
    return UserInfo(
        login = this.login ?: ""
    )
}