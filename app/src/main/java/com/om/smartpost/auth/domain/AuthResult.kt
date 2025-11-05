package com.om.smartpost.auth.domain

import java.sql.Timestamp

data class AuthResult(
    val token: String,
    val refreshToken: String,
    val username: String,
    val role: String
)
