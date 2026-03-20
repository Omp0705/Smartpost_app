package com.om.smartpost.customer.parcel.presentation

import com.om.smartpost.customer.parcel.presentation.components.ParcelFilter

sealed interface ParcelAction {
    data class OnSearchQueryChange(val query: String) : ParcelAction
    data class OnStatusFilterSelected(val filter: ParcelFilter) : ParcelAction
    data class OnParcelClick(val parcelId: String) : ParcelAction
}
