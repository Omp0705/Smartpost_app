package com.om.smartpost.core.domain.utils

sealed interface AuthError : Error {
    data object UserExists : AuthError
    data object EmailExists : AuthError
    data object MobileExists : AuthError
    data object InvalidCredentials : AuthError
    data object AccountInactive : AuthError
    data object TokenExpired : AuthError
    data object Unauthorized : AuthError
    data object Unknown : AuthError
    data object Network : AuthError
    data object Server : AuthError
}