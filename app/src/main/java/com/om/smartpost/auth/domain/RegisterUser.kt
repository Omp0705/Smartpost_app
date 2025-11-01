package com.om.smartpost.auth.domain

// Domain Model For Registering New User

data class RegisterUser(
    val fullName: String,
    val email: String,
    val mobileNo: String,
    val username: String,
    val password: String,
    val role: String
)