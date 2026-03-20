package com.om.smartpost.auth.presentation.signup

import com.om.smartpost.core.presentation.UiText

sealed interface SignUpEvent {
    data class Success(val message: UiText): SignUpEvent
    data class Error(val error: UiText): SignUpEvent
    object NavigateToHome: SignUpEvent
}