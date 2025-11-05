package com.om.smartpost.dashboard.data

import com.om.smartpost.auth.data.RemoteAuthDataSource
import com.om.smartpost.auth.domain.AuthRepository
import com.om.smartpost.core.data.local.TokenManager
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.dashboard.domain.InfoRepository
import com.om.smartpost.dashboard.domain.UserInfo

class InfoRepositoryImpl(
    private val infoDataSource: UserInfoDataSource,
) : InfoRepository {
    override suspend fun getProtectedData(): Result<UserInfo, ApiError> {
        val result = infoDataSource.getProtectedData()
        return when (result) {
            is Result.Error -> {
                Result.Error(result.error)
            }
            is Result.Success -> {
                Result.Success(result.data)
            }
        }
    }

}