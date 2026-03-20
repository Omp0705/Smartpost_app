package com.om.smartpost.auth.data

import com.om.smartpost.auth.data.dto.LoginRequestDto
import com.om.smartpost.auth.data.dto.LoginResponseDto
import com.om.smartpost.auth.data.dto.PasswordResetRequest
import com.om.smartpost.auth.data.dto.ResetPasswordResponse
import com.om.smartpost.auth.data.dto.SignUpRequestDto
import com.om.smartpost.auth.data.dto.SignupResponseDto
import com.om.smartpost.auth.data.dto.ValidSessionDto
import com.om.smartpost.auth.data.dto.ValidateOtpRequest
import com.om.smartpost.auth.data.dto.FcmTokenRequest
import com.om.smartpost.auth.data.mappers.toDto
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.LoginUser
import com.om.smartpost.auth.domain.RegisterUser
import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.map
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteAuthDataSource(
    private val client: HttpClient
) {
    suspend fun signUp(userData: SignUpRequestDto): Result<SignupResponseDto, ApiError
            > {
        return safeCall<SignupResponseDto> {
            client.post(
                urlString = constructUrl("/api/auth/register")
            ) {
                setBody(userData)
            }
        }
    }

    suspend fun signIn(loginUser: LoginRequestDto): Result<LoginResponseDto, ApiError> {
        return safeCall<LoginResponseDto> {
            client.post(
                urlString = constructUrl("/api/auth/login")
            ) {
                setBody(loginUser)
            }
        }
    }

    suspend fun refreshTokens(refreshToken: String): Result<LoginResponseDto,ApiError> {
        return safeCall<LoginResponseDto> {
            client.post(
                urlString = constructUrl("/api/auth/refresh")
            ) {
                setBody(mapOf("refreshToken" to refreshToken))
            }
        }
    }

    suspend fun validateSession(): Result<ValidSessionDto,ApiError> {
        return safeCall<ValidSessionDto> {
            client.get(
                urlString = constructUrl("/api/auth/validate")
            )
        }
    }

    suspend fun forgotPassword(email: String): Result<ResetPasswordResponse,ApiError> {
        return safeCall<ResetPasswordResponse> {
            client.post(
                urlString = constructUrl("/api/auth/forgot-password")
            ){
                setBody(mapOf("email" to email))
            }
        }
    }

    suspend fun validateOtp(request: ValidateOtpRequest): Result<ResetPasswordResponse,ApiError> {
        return safeCall<ResetPasswordResponse> {
            client.post(
                urlString = constructUrl("/api/auth/verify-otp")
            ){
                setBody(request)
            }
        }
    }

    suspend fun resetPassword(request: PasswordResetRequest): Result<ResetPasswordResponse,ApiError> {
        return safeCall<ResetPasswordResponse> {
            client.post(
                urlString = constructUrl("/api/auth/reset-password")
            ){
                setBody(request)
            }
        }
    }

    suspend fun updateFcmToken(token: String): Result<Unit, ApiError> {
        return safeCall<Unit> {
            client.patch(
                urlString = constructUrl("/api/users/me/fcm-token")
            ) {
                setBody(FcmTokenRequest(token))
            }
        }
    }


}