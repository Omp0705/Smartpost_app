package com.om.smartpost.customer.parcel.presentation.details

sealed interface ParcelDetailsEvent {
    data object NavigateBack : ParcelDetailsEvent
    data class ShowSnackbar(val message: String) : ParcelDetailsEvent
}
