package com.om.smartpost.auth.presentation.signin

import com.om.smartpost.auth.domain.ValidationError

sealed interface SignInEvent{
    data class ValidationErrors(val error: ValidationError): SignInEvent
    data class ShowMessage(val message: String): SignInEvent
}