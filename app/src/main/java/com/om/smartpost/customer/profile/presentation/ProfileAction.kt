package com.om.smartpost.customer.profile.presentation

import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences

sealed interface ProfileAction {
    object LoadProfile: ProfileAction
    object Logout: ProfileAction
    object ConfirmLogout: ProfileAction
    object DismissLogout: ProfileAction

    // Preferences Actions
    sealed interface Preference : ProfileAction {
        object Open : Preference
        object Close : Preference
        data class UpdateDraft(val preferences: DeliveryPreferences) : Preference
        object Save : Preference
    }
    // Address Actions
    sealed interface Address : ProfileAction {
        object OpenForNew : Address

        data class OpenForEdit(val addressId: String) : Address
        data class Delete(val addressId: String) : Address

        object Close : Address

        // Dynamic field updates
        data class EditField(val field: AddressField, val value: String) : Address

        object Save : Address
    }

}
enum class AddressField { TYPE,LINE1, LINE2, CITY, PINCODE }