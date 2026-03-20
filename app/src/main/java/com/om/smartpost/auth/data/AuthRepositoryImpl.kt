package com.om.smartpost.auth.data

import com.om.smartpost.auth.data.dto.LoginRequestDto
import com.om.smartpost.auth.data.dto.PasswordResetRequest
import com.om.smartpost.auth.data.dto.ValidateOtpRequest
import com.om.smartpost.auth.data.mappers.toDomain
import com.om.smartpost.auth.data.mappers.toDto
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.ForgotPasswordError
import com.om.smartpost.auth.domain.LoginUser
import com.om.smartpost.auth.domain.RegisterUser
import com.om.smartpost.auth.domain.ResetPasswordResult
import com.om.smartpost.core.data.local.TokenManager
import com.om.smartpost.core.domain.mappers.toAuthError
import com.om.smartpost.core.domain.mappers.toForgotPasswordError
import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.Result

class AuthRepositoryImpl(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun registerUser(
        userData: RegisterUser
    ): Result<String, AuthError> {
        return when (val result = remoteAuthDataSource.signUp(userData.toDto())) {
            is Result.Success -> {
                val loginResult = remoteAuthDataSource.signIn(
                    LoginRequestDto(
                        identifier = userData.email,
                        password = userData.password
                    )
                )
                when (loginResult) {
                    is Result.Success -> {
                        val authResult = loginResult.data.toDomain()
                        tokenManager.saveTokens(authResult.token,authResult.refreshToken)
                        tokenManager.saveRole(authResult.role)
                        Result.Success("User Logged In Successful")
                    }
                    is Result.Error -> {
                        Result.Error(loginResult.error.toAuthError())
                    }
                }

            }
            is Result.Error -> Result.Error(result.error.toAuthError())
        }
    }

    override suspend fun loginUser(loginUser: LoginUser): Result<AuthResult, AuthError> {
        return when (
            val result = remoteAuthDataSource.signIn(loginUser.toDto())
        ) {
            is Result.Success -> {
                tokenManager.saveTokens(result.data.token,result.data.refreshToken)
                tokenManager.saveRole(result.data.role)
                Result.Success(result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(result.error.toAuthError())
            }
        }
    }

    override suspend fun refreshTokens(refreshToken: String): Result<AuthResult, AuthError> {
        return when (
            val result = remoteAuthDataSource.refreshTokens(refreshToken = refreshToken)
        ) {
            is Result.Success -> {
                Result.Success(result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(result.error.toAuthError())
            }
        }
    }

    override suspend fun validateSession(): Boolean {
        // We could also verify the role here if needed, but for now just check token
        return when (
            val result = remoteAuthDataSource.validateSession()
        ) {
            is Result.Success -> {
                true
            }

            is Result.Error -> {
                false
            }
        }
    }

    override suspend fun sendResetLink(email: String): Result<ResetPasswordResult, ForgotPasswordError> {
        return when (
            val result = remoteAuthDataSource.forgotPassword(email)
        ) {
            is Result.Success -> {
                Result.Success(result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(result.error.toForgotPasswordError())
            }
        }
    }

    override suspend fun validateOtp(
        email: String,
        otp: String
    ): Result<ResetPasswordResult, ForgotPasswordError> {
        return when (
            val result = remoteAuthDataSource.validateOtp(ValidateOtpRequest(email, otp))
        ){
            is Result.Success -> {
                Result.Success(result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(result.error.toForgotPasswordError())
            }
        }
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Result<ResetPasswordResult, ForgotPasswordError> {
        return when (
            val result = remoteAuthDataSource.resetPassword(PasswordResetRequest(email, newPassword))
        ){
            is Result.Success -> {
                Result.Success(result.data.toDomain())
            }
            is Result.Error -> {
                Result.Error(result.error.toForgotPasswordError())
            }
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
    }

    override suspend fun getUserRole(): String? {
        return tokenManager.getUserRole()
    }

    override suspend fun updateFcmToken(token: String): Result<Unit, AuthError> {
        return when (val result = remoteAuthDataSource.updateFcmToken(token)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error.toAuthError())
        }
    }
}