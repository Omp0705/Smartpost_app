package com.om.smartpost.customer.parcel.presentation

import com.om.smartpost.customer.parcel.domain.model.Shipment
import com.om.smartpost.customer.parcel.presentation.components.ParcelFilter

data class ParcelState(
    val shipments: List<Shipment> = emptyList(),
    val filteredShipments: List<Shipment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedStatus: ParcelFilter = ParcelFilter.ALL,
    val searchQuery: String = ""
)
