package com.om.smartpost.auth.presentation.signup

data class SignUpUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val mobileNo: String = "",
    val password: String = "",
    val selectedRole: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isAgreementChecked: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)