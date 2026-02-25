package app.xl.androidapp.domain.repository

import app.xl.androidapp.data.dto.RepoDetailsDto
import app.xl.androidapp.data.dto.RepoDto
import app.xl.androidapp.domain.entity.UserInfo

interface AppRepositoryInterface {
    suspend fun signIn(token: String): UserInfo

    suspend fun getRepositories(): List<RepoDto>

    suspend fun getRepository(owner: String, repo: String): RepoDetailsDto

//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String

    suspend fun logout()
}