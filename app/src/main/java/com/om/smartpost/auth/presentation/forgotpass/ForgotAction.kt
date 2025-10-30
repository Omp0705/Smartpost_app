package com.om.smartpost.auth.presentation.forgotpass

sealed interface ForgotAction {
    data class updateEmail(val email: String):ForgotAction

    object ResetPassword: ForgotAction
    object onNavToSignIn: ForgotAction
}