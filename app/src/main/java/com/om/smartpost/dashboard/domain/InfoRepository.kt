package com.om.smartpost.dashboard.domain

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result

interface InfoRepository {
    suspend fun getProtectedData(): Result<UserInfo, ApiError>;
}