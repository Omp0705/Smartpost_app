package com.om.smartpost.core.domain.mappers

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.NetworkError
import com.om.smartpost.core.presentation.utils.AuthLabel

fun ApiError.toAuthError(): AuthError {
    return when (this) {
        is ApiError.ServerMessage -> AuthError.fromCode(serverRes.code)
        is ApiError.Transport -> when (networkError){
            NetworkError.NO_INTERNET -> AuthError.UNAUTHORIZED
            NetworkError.UNAUTHORIZED -> AuthError.UNAUTHORIZED
            else -> AuthError.UNKNOWN
        }
        ApiError.Conflict -> AuthError.UNKNOWN
        ApiError.ServerError -> AuthError.UNKNOWN
    }
}