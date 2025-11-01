package com.om.smartpost.auth.presentation.signup

import com.om.smartpost.auth.presentation.signin.SignInAction

sealed interface SignUpAction {
    data class UpdateEmail(val value: String) : SignUpAction
    data class UpdateMobile(val value: String) : SignUpAction
    data class UpdatePassword(val value: String) : SignUpAction
    data class TogglePasswordVisibility(val show: Boolean) : SignUpAction
    data class ToggleAgreement(val checked: Boolean) : SignUpAction

    data class UpdateFullName(val value: String) : SignUpAction
    data class UpdateUsername(val value: String) : SignUpAction
    data class SelectRole(val role: String) : SignUpAction

    data object Continue: SignUpAction
    data object CreateAccount : SignUpAction
    data object NavigateBack : SignUpAction
}