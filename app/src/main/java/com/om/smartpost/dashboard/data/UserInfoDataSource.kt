package com.om.smartpost.dashboard.data

import com.om.smartpost.auth.data.dto.SignUpRequestDto
import com.om.smartpost.auth.data.dto.SignupResponseDto
import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.dashboard.domain.UserInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserInfoDataSource(
    private val client: HttpClient
) {
    suspend fun getProtectedData(): Result<UserInfo, ApiError> {
        return safeCall<UserInfo> {
            client.get(
                urlString = constructUrl("/api/auth/profile")
            )
        }
    }
}