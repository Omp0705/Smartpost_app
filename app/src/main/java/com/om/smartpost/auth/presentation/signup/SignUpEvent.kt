package com.om.smartpost.auth.presentation.signup

import com.om.smartpost.auth.presentation.signin.SignInEvent
import com.om.smartpost.core.domain.utils.AuthError

sealed interface SignUpEvent {
    data class Success(val message: String): SignUpEvent
    data class Error(val error: AuthError): SignUpEvent
    object NavigateToHome: SignUpEvent
}