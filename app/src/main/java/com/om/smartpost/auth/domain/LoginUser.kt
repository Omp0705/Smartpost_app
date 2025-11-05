package com.om.smartpost.auth.domain

import com.om.smartpost.auth.presentation.signin.SignInAction

data class LoginUser(
    val identifier: String,
    val password: String
)