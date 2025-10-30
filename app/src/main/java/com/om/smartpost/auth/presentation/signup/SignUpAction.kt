package com.om.smartpost.auth.presentation.signup

sealed class SignUpAction {
    data class UpdateFullName(val value: String) : SignUpAction()
    data class UpdateUsername(val value: String) : SignUpAction()
    data class UpdateEmail(val value: String) : SignUpAction()
    data class UpdateMobile(val value: String) : SignUpAction()
    data class UpdatePassword(val value: String) : SignUpAction()
    data class UpdateConfirmPassword(val value: String) : SignUpAction()
    data class SelectRole(val role: String) : SignUpAction()
    data class TogglePasswordVisibility(val show: Boolean) : SignUpAction()
    data class ToggleConfirmPasswordVisibility(val show: Boolean) : SignUpAction()
    data class ToggleAgreement(val checked: Boolean) : SignUpAction()
    object Submit : SignUpAction()
    object NavigateBack : SignUpAction()
}