package com.om.smartpost.auth.domain

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.Result

interface AuthRepository{
    suspend fun registerUser(userData: RegisterUser): Result<String, AuthError>
    suspend fun loginUser(loginUser: LoginUser): Result<AuthResult,AuthError>
    suspend fun refreshTokens(refreshToken: String): Result<AuthResult,AuthError>
    suspend fun validateSession(): Boolean
}