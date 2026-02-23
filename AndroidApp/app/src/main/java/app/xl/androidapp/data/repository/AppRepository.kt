package app.xl.androidapp.data.repository

import app.xl.androidapp.data.storage.TokenManager
import app.xl.androidapp.data.dto.GitHubErrorDto
import app.xl.androidapp.data.network.GitHubApi
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.UserInfo
import app.xl.androidapp.domain.repository.AppRepositoryInterface
import kotlinx.serialization.json.Json
import java.io.IOException

class AppRepository(
    private val api: GitHubApi,
    private val json: Json,
    private val tokenManager: TokenManager
) : AppRepositoryInterface {
    override suspend fun signIn(token: String): UserInfo {
        try {
            val dto = api.getUser(token)
            tokenManager.saveToken(token)
            return dto.toEntity()
        } catch (exception: retrofit2.HttpException) {

            val errorBody = exception.response()?.errorBody()?.string()
            val errorMessage = if (!errorBody.isNullOrEmpty()) {
                runCatching {
                    json.decodeFromString<GitHubErrorDto>(errorBody).message
                }.getOrNull()
            } else {
                null
            }

            throw AppError.Http(
                code = exception.code(),
                errorMessage = errorMessage,
                cause = exception
            )
        } catch (exception: IOException) {
            throw AppError.Network(
                cause = exception
            )
        }
    }

//    suspend fun getRepositories(): List<Repo> {
//        // TODO:
//    }

//    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }

//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }
}