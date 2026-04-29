package com.om.smartpost.customer.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.presentation.UiText
import com.om.smartpost.customer.profile.domain.model.AddressType
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences
import com.om.smartpost.customer.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfile()
    }
    fun onAction(action: ProfileAction){
        when(action) {
            is ProfileAction.LoadProfile -> {
                loadProfile()
            }
            is ProfileAction.Logout -> {
                _state.update { it.copy(isLogoutDialogVisible = true) }
            }
            is ProfileAction.Address -> {
                handleAddressAction(action)
            }
            is ProfileAction.Preference -> {
                handlePreferenceAction(action)

            }
            is ProfileAction.ConfirmLogout -> {
                logout()
            }
            is ProfileAction.DismissLogout -> {
                _state.update { it.copy(isLogoutDialogVisible = false) }
            }
        }
    }


    private fun handleAddressAction(action: ProfileAction.Address) {
        when (action) {
            is ProfileAction.Address.OpenForNew -> {
                _state.update { it.copy(addAddressFromState = AddressFormState(isVisible = true)) }
            }

            is ProfileAction.Address.Close -> {
                updateAddressState { it.copy(isVisible = false) }
            }
            is ProfileAction.Address.EditField -> {
                updateAddressState {
                    when(action.field){
                        AddressField.TYPE -> {
                            val type = AddressType.entries.find {
                                it.name.equals(action.value, ignoreCase = true)
                            } ?: AddressType.HOME
                            it.copy(addressType = type)
                        }
                        AddressField.LINE1 -> { it.copy(addressLine1 = action.value)}
                        AddressField.LINE2 -> {it.copy(addressLine2 = action.value)}
                        AddressField.CITY -> {it.copy(city = action.value)}
                        AddressField.PINCODE -> {it.copy(pincode = action.value)}
                    }
                }
            }
            is ProfileAction.Address.OpenForEdit -> {
                val address = _state.value.userProfile?.addresses?.find { it.id == action.addressId }
                _state.update { it.copy(
                    addAddressFromState =
                        AddressFormState(
                            isVisible = true,
                            editingAddressId = address?.id,
                            addressType = address?.type ?: AddressType.HOME,
                            addressLine1= address?.addressLine1 ?: "",
                            addressLine2 = address?.addressLine2 ?: "",
                            city = address?.city ?: "",
                            pincode = address?.pincode ?: "",
                            isSubmitting = false
                        )
                ) }
            }
            is ProfileAction.Address.Save -> {
                val form = _state.value.addAddressFromState
                if(form.editingAddressId != null){
                    updateExistingAddress(form)
                } else {
                    createNewAddress(form)
                }
            }
            is ProfileAction.Address.Delete -> {
                deleteAddress(action.addressId)
            }
        }
    }

    private fun handlePreferenceAction(action: ProfileAction.Preference) {
        when (action) {
            ProfileAction.Preference.Open -> {
                val initialDraft = _state.value.userProfile?.deliveryPreferences ?: DeliveryPreferences(
                    preferredDeliverySlot = com.om.smartpost.customer.profile.domain.model.DeliverySlot(
                        code = com.om.smartpost.customer.profile.data.dto.TimeSlot.SLOT_10_12, 
                        label = "10:00 AM - 12:00 PM"
                    ),
                    leaveAtDoor = false,
                    leaveWithGuard = false,
                    deliverToNeighbor = false,
                    otpRequired = false,
                    avoidMorning = false,
                    weekendOnly = false,
                    deliveryNote = ""
                )
                
                _state.update { it.copy(
                    preferencesSheetState = it.preferencesSheetState.copy(
                        isVisible = true,
                        draft = initialDraft
                    )
                )}
            }

            is ProfileAction.Preference.UpdateDraft -> {
                _state.update { it.copy(
                    preferencesSheetState = it.preferencesSheetState.copy(draft = action.preferences)
                )}
            }

            ProfileAction.Preference.Save -> {
                val draft = _state.value.preferencesSheetState.draft ?: return
                println(draft)
                savePreferences(draft)
            }

            ProfileAction.Preference.Close -> {
                _state.update { it.copy(preferencesSheetState = it.preferencesSheetState.copy(isVisible = false)) }
            }
        }
    }
    // Neat helper function to reduce boilerplate .copy() nesting
    private fun updateAddressState(update: (AddressFormState) -> AddressFormState) {
        _state.update { it.copy(addAddressFromState = update(it.addAddressFromState)) }
    }

    private fun createNewAddress(form: AddressFormState) {
        // Basic validation check before launching coroutine
        if (form.addressLine1.isBlank() || form.city.isBlank() || form.pincode.isBlank()) {
            viewModelScope.launch {
                _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Please fill in all required fields")))
            }
            return
        }

        viewModelScope.launch {
            // 1. Set loading state
            _state.update { it.copy(
                addAddressFromState = it.addAddressFromState.copy(isSubmitting = true)
            )}

            // 2. Map Form State to Domain Model
            val newAddress = com.om.smartpost.customer.profile.domain.model.Address(
                id = "", // Backend handles ID for new addresses
                type = form.addressType,
                addressLine1 = form.addressLine1,
                addressLine2 = form.addressLine2,
                city = form.city,
                pincode = form.pincode,
                isDefault = false
            )

            // 3. Repository Call
            when (val result = repository.addAddress(newAddress)) {
                is Result.Success -> {
                    // 4. Reset form, hide sheet, and refresh profile data
                    _state.update { it.copy(
                        addAddressFromState = AddressFormState(isVisible = false)
                    )}
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Address added successfully")))
                    loadProfile() // Refresh to show the new address in the list
                }
                is Result.Error -> {
                    // 5. Stop loading and show error
                    _state.update { it.copy(
                        addAddressFromState = it.addAddressFromState.copy(isSubmitting = false)
                    )}
                    // Use your error mapping logic here
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Failed to add address")))
                }
            }
        }
    }

    private fun updateExistingAddress(form: AddressFormState) {
        val addressId = form.editingAddressId ?: return
        if (form.addressLine1.isBlank() || form.city.isBlank() || form.pincode.isBlank()) {
            viewModelScope.launch {
                _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Please fill in all required fields")))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(
                addAddressFromState = it.addAddressFromState.copy(isSubmitting = true)
            )}

            val updatedAddress = com.om.smartpost.customer.profile.domain.model.Address(
                id = addressId,
                type = form.addressType,
                addressLine1 = form.addressLine1,
                addressLine2 = form.addressLine2,
                city = form.city,
                pincode = form.pincode,
                isDefault = false
            )

            when (val result = repository.updateAddress(addressId, updatedAddress)) {
                is Result.Success -> {
                    _state.update { it.copy(addAddressFromState = AddressFormState(isVisible = false)) }
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Address updated successfully")))
                    loadProfile()
                }
                is Result.Error -> {
                    _state.update { it.copy(addAddressFromState = it.addAddressFromState.copy(isSubmitting = false)) }
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Failed to update address")))
                }
            }
        }
    }

    private fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            val currentProfile = _state.value.userProfile
            val originalAddresses = currentProfile?.addresses ?: emptyList()

            // Optimistic update
            if (currentProfile != null) {
                val updatedAddresses = originalAddresses.filter { it.id != addressId }
                _state.update {
                    it.copy(userProfile = currentProfile.copy(addresses = updatedAddresses))
                }
            }

            when (val result = repository.deleteAddress(addressId)) {
                is Result.Success -> {
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Address deleted successfully")))
                }
                is Result.Error -> {
                    // Revert optimistic update
                    if (currentProfile != null) {
                        _state.update {
                            it.copy(userProfile = currentProfile.copy(addresses = originalAddresses))
                        }
                    }
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Failed to delete address")))
                }
            }
        }
    }

    private fun savePreferences(draft: DeliveryPreferences) {
        viewModelScope.launch {
            println("DEBUG: Starting Coroutine for network call") // Check if this prints

            _state.update {
                it.copy(preferencesSheetState = it.preferencesSheetState.copy(isUpdating = true))
            }

            try {
                println("DEBUG: About to call repository with: $draft")
                val result = repository.updateDeliveryPreference(draft)
                println("DEBUG: Repository returned result: $result")

                when (result) {
                    is Result.Success -> {
                        _state.update { it.copy(
                            userProfile = it.userProfile?.copy(deliveryPreferences = draft),
                            preferencesSheetState = it.preferencesSheetState.copy(
                                isUpdating = false,
                                isVisible = false
                            )
                        )}
                        _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Preferences saved!")))
                    }
                    is Result.Error -> {
                        _state.update { it.copy(
                            preferencesSheetState = it.preferencesSheetState.copy(isUpdating = false)
                        )}
                        _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString("Error: ${result.error}")))
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: Crash caught in ViewModel: ${e.message}")
                e.printStackTrace()
                _state.update { it.copy(preferencesSheetState = it.preferencesSheetState.copy(isUpdating = false)) }
            }
        }
    }



    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.getUserProfile()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, userProfile = result.data) }
                }
                is Result.Error -> {
                    val message = when(val error = result.error) {
                        is com.om.smartpost.customer.profile.domain.model.ProfileError.Network -> {
                           when(error.type) {
                               com.om.smartpost.core.domain.utils.NetworkError.NO_INTERNET -> "No internet connection"
                               com.om.smartpost.core.domain.utils.NetworkError.REQUEST_TIMEOUT -> "Request timed out"
                               com.om.smartpost.core.domain.utils.NetworkError.SERVER_ERROR -> "Server error"
                               else -> "Something went wrong"
                           }
                        }
                        is com.om.smartpost.customer.profile.domain.model.ProfileError.Validation -> "Validation error"
                        else -> "Unknown error"
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = UiText.DynamicString(message)
                        )
                    }
                    _events.send(ProfileEvent.ShowSnackbar(UiText.DynamicString(message)))
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLogoutDialogVisible = false) }
            repository.logout()
            _events.send(ProfileEvent.NavigateToLogin)
        }
    }
}
