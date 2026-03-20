package com.om.smartpost.auth.presentation.mappers

import com.om.smartpost.core.domain.utils.AuthError
import com.om.smartpost.core.presentation.UiText

fun AuthError.toUiText(): UiText {
    return when(this) {
        AuthError.AccountInactive -> UiText.DynamicString("Your account is not active")
        AuthError.EmailExists -> UiText.DynamicString("Email already exists")
        AuthError.InvalidCredentials -> UiText.DynamicString("Invalid username or password")
        AuthError.MobileExists -> UiText.DynamicString("Mobile number already exists")
        AuthError.TokenExpired -> UiText.DynamicString("Session expired, please login again")
        AuthError.Unauthorized -> UiText.DynamicString("You are not authorized")
        AuthError.UserExists -> UiText.DynamicString("Username already exists")
        AuthError.Network -> UiText.DynamicString("Please check your internet connection")
        AuthError.Server -> UiText.DynamicString("Server error, please try again later")
        AuthError.Unknown -> UiText.DynamicString("Something went wrong. Please try again.")
    }
}
