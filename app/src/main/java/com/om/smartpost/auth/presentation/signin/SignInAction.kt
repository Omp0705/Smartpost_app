package com.om.smartpost.auth.presentation.signin

sealed interface SignInAction{
    data class UpdateIdentifier(val identifier: String): SignInAction
    data class UpdatePassword(val password: String): SignInAction

    object SignIn: SignInAction
    object TogglePasswordVisibility : SignInAction
    object ForgotPassword : SignInAction
    object NavigateToSignUp : SignInAction
    object FacebookSignIn : SignInAction
    object GoogleSignIn : SignInAction
}