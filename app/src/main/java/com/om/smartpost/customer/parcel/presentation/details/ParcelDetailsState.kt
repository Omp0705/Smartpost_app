package com.om.smartpost.customer.parcel.presentation.details

import com.om.smartpost.customer.parcel.domain.model.Shipment

data class ParcelDetailsState(
    val isLoading: Boolean = false,
    val parcel: Shipment? = null,
    val error: String? = null
)
