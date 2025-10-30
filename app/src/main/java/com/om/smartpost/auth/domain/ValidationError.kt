package com.om.smartpost.auth.domain

enum class ValidationError(val message: String) {
    IDENTIFIER_EMPTY("Please enter your email or username."),
    EMAIL_EMPTY("Please enter your email address."),
    EMAIL_INVALID("Please enter a valid email address."),
    NAME_EMPTY("Please enter your full name."),
    USERNAME_EMPTY("Please enter your username."),
    USERNAME_INVALID("Username must be at least 3 characters long."),
    MOBILE_EMPTY("Please enter your mobile number."),
    PASSWORD_EMPTY("Please enter your password."),
    PASSWORD_MISMATCH("Passwords do not match."),
    PASSWORD_TO_SHORT("Password must be at least 6 characters long."),
    UNKNOWN("Something went wrong. Please try again.")
}
