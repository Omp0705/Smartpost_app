package com.om.smartpost.auth.data

import com.om.smartpost.auth.data.dto.SignupResponseDto
import com.om.smartpost.auth.data.mappers.toDto
import com.om.smartpost.auth.domain.AuthResult
import com.om.smartpost.auth.domain.RegisterUser
import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.map
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteAuthDataSource(
    private val client: HttpClient
) {
    suspend fun signUp(userData: RegisterUser): Result<SignupResponseDto, NetworkError> {
        return safeCall<SignupResponseDto> {
            client.post(
                urlString = constructUrl("/api/auth/register")
            ){
                setBody(userData.toDto())
            }
        }
    }
}