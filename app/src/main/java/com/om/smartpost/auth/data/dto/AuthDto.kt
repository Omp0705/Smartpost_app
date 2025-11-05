package com.om.smartpost.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val fullName: String,
    val username: String,
    val email: String,
    val mobileNo: String,
    val password: String,
    val role: String
)

@Serializable
data class SignupResponseDto(
    val msg: String
)

@Serializable
data class LoginRequestDto (
    val identifier: String,
    val password: String
)

@Serializable
data class LoginResponseDto (
    val token: String,
    val refreshToken: String,
    val username: String,
    val role: String
)

@Serializable
data class ValidSessionDto (
    val msg: String
)



@Serializable
data class ErrorResponseDto(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val code: String? = null // prefer server to send this
)





