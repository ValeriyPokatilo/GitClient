package app.xl.androidapp.domain.repository

import app.xl.androidapp.domain.entity.Repository
import app.xl.androidapp.domain.entity.RepositoryDetails
import app.xl.androidapp.domain.entity.UserInfo

interface AppRepositoryInterface {
    suspend fun signIn(token: String): UserInfo

    suspend fun getRepositories(): List<Repository>

    suspend fun getRepository(ownerName: String, repositoryName: String): RepositoryDetails

    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String?

    suspend fun logout()
}