package com.om.smartpost.auth.presentation.signin

import com.om.smartpost.auth.domain.ValidationError
import com.om.smartpost.core.presentation.UiText

sealed interface SignInEvent{
    data class ValidationErrors(val error: ValidationError): SignInEvent
    data class ShowMessage(val message: UiText): SignInEvent
    object NavigateToCustomerHome: SignInEvent
    object NavigateToPostmanHome: SignInEvent
}