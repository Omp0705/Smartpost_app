package com.om.smartpost.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val newPassword: String
)

@Serializable
data class ValidateOtpRequest (
    val email: String,
    val otp: String
)

@Serializable
data class ResetPasswordResponse (
    val status: String,
    val message: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

