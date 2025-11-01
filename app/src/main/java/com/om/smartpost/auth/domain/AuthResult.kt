package com.om.smartpost.auth.domain

import java.sql.Timestamp

sealed class AuthResult{

    data class Success(
        val token: String,
        val refreshToken: String,
        val username: String,
        val role: String
    ): AuthResult()

    data class Error(
        val timestamp: Timestamp,
        val status: Int,
        val error: String,
        val message: String
        ): AuthResult()

}