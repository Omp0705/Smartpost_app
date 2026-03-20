package com.om.smartpost.auth.presentation.forgotpass

import com.om.smartpost.auth.domain.ValidationError

// Define the steps for the Forgot Password flow
enum class ForgotStep {
    EMAIL_ENTRY,      // Step 1: User enters email
    OTP_VALIDATION,   // Step 2: User enters OTP received
    PASSWORD_RESET    // Step 3: User sets new password
}

data class ForgotUiState(
    val email: String = "",
    val otpCode: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val currentStep: ForgotStep = ForgotStep.EMAIL_ENTRY,
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val validationErrors: ValidationError? = null
)

