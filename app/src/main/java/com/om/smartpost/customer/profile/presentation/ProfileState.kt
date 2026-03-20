package com.om.smartpost.customer.profile.presentation

import com.om.smartpost.customer.profile.domain.model.UserProfile
import com.om.smartpost.core.presentation.UiText
import com.om.smartpost.customer.profile.domain.model.AddressType
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences

data class ProfileState (
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: UiText? = null,
    val isLogoutDialogVisible: Boolean = false,

//    Now this states are nested for sheets:
    val addAddressFromState: AddressFormState = AddressFormState(),
    val preferencesSheetState: PreferencesSheetState = PreferencesSheetState()

)

data class AddressFormState(
    val editingAddressId: String? = null,
    val addressType: AddressType = AddressType.HOME,
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val pincode: String = "",
    val isVisible: Boolean = false,
    val isSubmitting: Boolean = false
)



data class PreferencesSheetState(
    val isVisible: Boolean = false,
    val isUpdating: Boolean = false,
    val draft: DeliveryPreferences? = null
)