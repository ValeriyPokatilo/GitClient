package app.xl.androidapp.data.network

import app.xl.androidapp.data.dto.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Header

interface GitHubApi {
    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserInfoDto
}