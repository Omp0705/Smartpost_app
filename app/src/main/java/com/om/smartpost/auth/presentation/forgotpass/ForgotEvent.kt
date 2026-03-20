package com.om.smartpost.auth.presentation.forgotpass

import com.om.smartpost.auth.domain.ValidationError
import com.om.smartpost.core.presentation.UiText

sealed interface ForgotEvent {
    data class ValidationErrors(val error: ValidationError): ForgotEvent
    data class ShowMessage(val msg: UiText): ForgotEvent
    data object NavigateToSignIn : ForgotEvent
}