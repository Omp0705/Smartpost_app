package com.om.smartpost.auth.presentation.signup

sealed class SignUpEvent {
    data class ShowMessage(val message: String) : SignUpEvent()
    data class ValidationFailed(val errors: Map<String, String>) : SignUpEvent()
}