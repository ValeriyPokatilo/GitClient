package app.xl.androidapp.data.network

import app.xl.androidapp.data.dto.ReadmeDto
import app.xl.androidapp.data.dto.RepoDetailsDto
import app.xl.androidapp.data.dto.RepoDto
import app.xl.androidapp.data.dto.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserInfoDto

    @GET("user/repos")
    suspend fun getRepositories(
        @Header("Authorization") token: String
    ): List<RepoDto>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepoDetailsDto

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getRepositoryReadme(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") branch: String? = null
    ): ReadmeDto
}