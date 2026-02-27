package app.xl.androidapp.data.repository

import app.xl.androidapp.R
import app.xl.androidapp.data.dto.GitHubErrorDto
import app.xl.androidapp.data.network.GitHubApi
import app.xl.androidapp.data.network.toBearerHeader
import app.xl.androidapp.data.repository.mappers.toEntity
import app.xl.androidapp.data.storage.TokenManager
import app.xl.androidapp.domain.entity.AppError
import app.xl.androidapp.domain.entity.Repo
import app.xl.androidapp.domain.entity.RepoDetails
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
        val authHeader = token.toBearerHeader()

        try {
            return api.getUser(authHeader)
                .also { tokenManager.saveToken(token) }
                .toEntity()
        } catch (exception: retrofit2.HttpException) {
            throw handleHttpException(exception)
        } catch (exception: IOException) {
            throw AppError.Network(cause = exception)
        }
    }

    override suspend fun getRepositories(): List<Repo> {
        val authHeader = createAuthHeader()

        try {
            return api.getRepositories(authHeader).map { it.toEntity() }
        } catch (exception: retrofit2.HttpException) {
            throw handleHttpException(exception)
        } catch (exception: IOException) {
            throw AppError.Network(cause = exception)
        }
    }

    override suspend fun getRepository(ownerName: String, repositoryName: String): RepoDetails {
        val authHeader = createAuthHeader()

        try {
            return api.getRepository(
                token = authHeader,
                ownerName = ownerName,
                repositoryName = repositoryName
            ).toEntity()
        } catch (exception: retrofit2.HttpException) {
            throw handleHttpException(exception)
        } catch (exception: IOException) {
            throw AppError.Network(cause = exception)
        }
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String? {
        val authHeader = createAuthHeader()

        return try {
            val readmeDto = api.getRepositoryReadme(
                token = authHeader,
                ownerName = ownerName,
                repositoryName = repositoryName,
                branchName = branchName
            )

            if (readmeDto.encoding != "base64") {
                throw AppError.DataFormat("Unsupported encoding: ${readmeDto.encoding}")
            }

            String(android.util.Base64.decode(readmeDto.content, android.util.Base64.DEFAULT))
                .takeIf { it.isNotEmpty() }
        } catch (exception: retrofit2.HttpException) {
            if (exception.code() == 404) return null
            throw handleHttpException(exception)
        } catch (exception: IOException) {
            throw AppError.Network(cause = exception)
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    private fun createAuthHeader(): String {
        val token = tokenManager.getToken() ?: throw AppError.Network(
            Exception(R.string.invalid_token.toString())
        )
        return token.toBearerHeader()
    }

    private fun handleHttpException(exception: retrofit2.HttpException): AppError.Http {
        val errorBody = exception.response()?.errorBody()?.string()

        val errorMessage =
            if (!errorBody.isNullOrEmpty()) {
                runCatching {
                    json.decodeFromString<GitHubErrorDto>(errorBody).message
                }.getOrNull()
            } else null

        return AppError.Http(
            code = exception.code(),
            errorMessage = errorMessage,
            cause = exception
        )
    }
}