package com.om.smartpost.core.domain.utils

enum class AuthError(val message: String) : Error {
    USERNAME_EXISTS("Username already exists"),
    EMAIL_EXISTS("Email already exists"),
    MOBILE_EXISTS("Mobile number already exists"),
    INVALID_CREDENTIALS("Invalid username or password"),
    ACCOUNT_INACTIVE("Your account is not active"),
    TOKEN_EXPIRED("Session expired, please login again"),
    UNAUTHORIZED("You are not authorized"),
    UNKNOWN("Something went wrong. Please try again.");

    companion object {
        fun fromCode(code: String?): AuthError {
            return when (code) {
                "USERNAME_EXISTS" -> USERNAME_EXISTS
                "EMAIL_EXISTS" -> EMAIL_EXISTS
                "MOBILE_EXISTS" -> MOBILE_EXISTS
                "INVALID_CREDENTIALS" -> INVALID_CREDENTIALS
                "ACCOUNT_INACTIVE" -> ACCOUNT_INACTIVE
                "TOKEN_EXPIRED" -> TOKEN_EXPIRED
                "UNAUTHORIZED" -> UNAUTHORIZED
                else -> UNKNOWN
            }
        }
    }
}