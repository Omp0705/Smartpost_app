package com.om.smartpost.auth.domain

sealed interface ValidationError {
    data object IdentifierEmpty : ValidationError
    data object EmailEmpty : ValidationError
    data object EmailInvalid : ValidationError
    data object NameEmpty : ValidationError
    data object UsernameEmpty : ValidationError
    data object UsernameInvalid : ValidationError
    data object MobileEmpty : ValidationError
    data object PasswordEmpty : ValidationError
    data object PasswordMismatch : ValidationError
    data object PasswordTooShort : ValidationError
    data object Unknown : ValidationError
}
