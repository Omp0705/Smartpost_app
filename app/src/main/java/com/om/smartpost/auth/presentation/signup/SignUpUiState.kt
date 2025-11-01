package com.om.smartpost.auth.presentation.signup

import androidx.compose.ui.graphics.Color

enum class SignUpStage { Credentials, Profile}
data class SignUpUiState(
    val stage: SignUpStage = SignUpStage.Credentials,
    val email: String = "",
    val mobileNo: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordStrength: PasswordStrength = PasswordStrength.NONE,
    val isAgreementChecked: Boolean = false,

    val fullName: String = "",
    val username: String = "",
    val selectedRole: String? = null,

    val isLoading: Boolean = false,

//    Inline Validation Messages
    val emailError: String? = null,
    val mobileError: String? = null,
    val passwordHint: String? = null,
    val passwordError: String? = null,
    val fullNameError: String? = null,
    val usernameError: String? = null,
    val roleError: String? = null,


)

// For showing the Password Strength Indicator
enum class PasswordStrength(val progress: Float, val color: Color, val label: String) {
    NONE(0f, Color.Transparent, ""),
    POOR(0.25f, Color(0xFFD32F2F), "Poor"),
    MEDIUM(0.5f, Color(0xFFFF9800), "Medium"),
    GOOD(0.75f, Color(0xFFFFC107), "Good"),
    STRONG(1f, Color(0xFF4CAF50), "Strong")
}