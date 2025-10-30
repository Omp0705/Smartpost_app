package com.om.smartpost.auth.presentation.signin

data class SignInUiState(
    val identifier: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
//    val user: User? = null    Domain model user for passing
)