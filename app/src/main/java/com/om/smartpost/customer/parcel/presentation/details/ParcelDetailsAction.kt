package com.om.smartpost.customer.parcel.presentation.details

sealed interface ParcelDetailsAction {
    data object OnBackClick : ParcelDetailsAction
    data object OnRescheduleClick : ParcelDetailsAction
    data object OnContactSupportClick : ParcelDetailsAction
}
