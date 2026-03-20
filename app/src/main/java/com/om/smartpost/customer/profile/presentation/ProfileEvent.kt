package com.om.smartpost.customer.profile.presentation

import com.om.smartpost.core.presentation.UiText

sealed interface ProfileEvent {
    data class ShowSnackbar(val message: UiText) : ProfileEvent
    object NavigateToLogin : ProfileEvent
    object NavigateToAddAddress : ProfileEvent // Or ShowBottomSheet
}
