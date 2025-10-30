package com.om.smartpost.auth.presentation.forgotpass

data class ForgotUiState(
    val email: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,

)