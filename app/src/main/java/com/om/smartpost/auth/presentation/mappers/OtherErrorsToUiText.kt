package com.om.smartpost.auth.presentation.mappers

import com.om.smartpost.auth.domain.ForgotPasswordError
import com.om.smartpost.auth.domain.ValidationError
import com.om.smartpost.core.presentation.UiText

fun ForgotPasswordError.toUiText(): UiText {
    return when(this) {
        ForgotPasswordError.InvalidOtp -> UiText.DynamicString("Invalid OTP, otp does not match")
        ForgotPasswordError.OtpExpired -> UiText.DynamicString("Otp timeout, please try again")
        ForgotPasswordError.SessionExpired -> UiText.DynamicString("Something went wrong, please try again")
        ForgotPasswordError.NoInternet -> UiText.DynamicString("No internet connection")
        ForgotPasswordError.ServerError -> UiText.DynamicString("Server error")
        ForgotPasswordError.Unknown -> UiText.DynamicString("Something went wrong. Please try again.")
    }
}

fun ValidationError.toUiText(): UiText {
    return when(this) {
        ValidationError.IdentifierEmpty -> UiText.DynamicString("Please enter your email or username.")
        ValidationError.EmailEmpty -> UiText.DynamicString("Please enter your email address.")
        ValidationError.EmailInvalid -> UiText.DynamicString("Please enter a valid email address.")
        ValidationError.NameEmpty -> UiText.DynamicString("Please enter your full name.")
        ValidationError.UsernameEmpty -> UiText.DynamicString("Please enter your username.")
        ValidationError.UsernameInvalid -> UiText.DynamicString("Username must be at least 3 characters long.")
        ValidationError.MobileEmpty -> UiText.DynamicString("Please enter your mobile number.")
        ValidationError.PasswordEmpty -> UiText.DynamicString("Please enter your password.")
        ValidationError.PasswordMismatch -> UiText.DynamicString("Passwords do not match.")
        ValidationError.PasswordTooShort -> UiText.DynamicString("Password must be at least 6 characters long.")
        ValidationError.Unknown -> UiText.DynamicString("Something went wrong. Please try again.")
    }
}
