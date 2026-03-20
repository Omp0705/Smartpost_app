package com.om.smartpost.customer.parcel.presentation

sealed interface ParcelEvent {
    data class NavigateToParcelDetails(val parcelId: String) : ParcelEvent
    data class ShowSnackbar(val message: String) : ParcelEvent
}
