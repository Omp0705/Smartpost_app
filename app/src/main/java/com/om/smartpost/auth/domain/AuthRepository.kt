package com.om.smartpost.auth.domain

import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.Result

interface AuthRepository{
    suspend fun registerUser(userData: RegisterUser): Result<AuthResult, AuthError>
}