package com.om.smartpost.core.domain.utils

enum class AuthError: Error {
    USERNAME_EXISTS,
    EMAIL_EXISTS,
    SERVER_ERROR
}