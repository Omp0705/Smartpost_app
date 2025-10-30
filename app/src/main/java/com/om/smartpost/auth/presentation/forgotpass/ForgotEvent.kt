package com.om.smartpost.auth.presentation.forgotpass

import com.om.smartpost.auth.domain.ValidationError

sealed interface ForgotEvent {
    data class ValidationErrors(val error: ValidationError): ForgotEvent
    data class ShowMessage(val msg: String): ForgotEvent
}