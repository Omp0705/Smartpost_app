package com.om.smartpost.core.domain.mappers

import com.om.smartpost.auth.domain.ForgotPasswordError
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.domain.utils.NetworkError

fun ApiError.toAuthError(): AuthError {
    return when (this) {
        is ApiError.ServerMessage -> {
            when(serverRes.code) {
                "USERNAME_EXISTS" -> AuthError.UserExists
                "EMAIL_EXISTS" -> AuthError.EmailExists
                "MOBILE_EXISTS" -> AuthError.MobileExists
                "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials
                "ACCOUNT_INACTIVE" -> AuthError.AccountInactive
                "TOKEN_EXPIRED" -> AuthError.TokenExpired
                "UNAUTHORIZED" -> AuthError.Unauthorized
                else -> AuthError.Unknown
            }
        }
        is ApiError.Transport -> when (networkError){
            NetworkError.NO_INTERNET -> AuthError.Network
            NetworkError.UNAUTHORIZED -> AuthError.Unauthorized
            else -> AuthError.Unknown
        }
        ApiError.Conflict -> AuthError.UserExists // Default conflict to UserExists if no message
        ApiError.ServerError -> AuthError.Server
    }
}

fun ApiError.toForgotPasswordError(): ForgotPasswordError {
    return when (this) {
        is ApiError.ServerMessage -> {
             when(serverRes.code) {
                 "INVALID_OTP" -> ForgotPasswordError.InvalidOtp
                 "OTP_EXPIRED" -> ForgotPasswordError.OtpExpired
                 "SESSION_EXPIRED" -> ForgotPasswordError.SessionExpired
                 else -> ForgotPasswordError.Unknown
             }
        }
        is ApiError.Transport -> when (networkError){
            NetworkError.NO_INTERNET -> ForgotPasswordError.NoInternet
            NetworkError.UNAUTHORIZED -> ForgotPasswordError.SessionExpired
            else -> ForgotPasswordError.Unknown
        }
        ApiError.Conflict -> ForgotPasswordError.Unknown
        ApiError.ServerError -> ForgotPasswordError.ServerError
    }
}