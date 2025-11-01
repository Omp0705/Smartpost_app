package com.om.smartpost.auth.data

import com.om.smartpost.auth.data.dto.SignupResponseDto
import com.om.smartpost.auth.data.mappers.toDto
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.RegisterUser
import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.map
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRepositoryImpl(
    private val remoteAuthDataSource: RemoteAuthDataSource
): AuthRepository {

    override suspend fun registerUser(userData: RegisterUser): Result<AuthResult, AuthError> {
        val result = remoteAuthDataSource.signUp(userData)
        return when (result) {
            is Result.Success -> {
                val response = result.data
                Result.Success(
                    AuthResult.Success(
                        token = response.token,
                        refreshToken = response.refreshToken,
                        username = response.username,
                        role = response.role
                    )
                )
            }
            is Result.Error -> {

            }
        }
    }

}