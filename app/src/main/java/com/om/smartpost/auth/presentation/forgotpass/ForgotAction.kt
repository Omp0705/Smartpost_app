package com.om.smartpost.auth.presentation.forgotpass

import com.om.smartpost.auth.presentation.signin.SignInAction

sealed interface ForgotAction {
    data class updateEmail(val email: String):ForgotAction
    data class updateOtpCode(val otp: String):ForgotAction
    data class updateNewPassword(val newPassword: String):ForgotAction
    data class updateConfirmPassword(val cnfPassword: String):ForgotAction

    object ToggleNewPasswordVisibility : ForgotAction
    object ToggleCnfPasswordVisibility : ForgotAction
    object SendResetLink: ForgotAction
    object ValidateOtp: ForgotAction
    object ResetPassword: ForgotAction
    object onNavToSignIn: ForgotAction
    object GoBackToEmailEntry : ForgotAction
}
