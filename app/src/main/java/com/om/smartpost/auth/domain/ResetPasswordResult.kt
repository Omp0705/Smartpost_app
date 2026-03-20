package com.om.smartpost.auth.domain

data class ResetPasswordResult(
    val message: String,
    val code: String
)

enum class ResultCodes {
    INVALID_OTP,
    OTP_EXPIRED,
    SUCCESS,
    SESSION_EXPIRED
}