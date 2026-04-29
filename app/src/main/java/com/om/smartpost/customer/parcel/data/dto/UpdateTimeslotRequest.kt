package com.om.smartpost.customer.parcel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTimeslotRequest(
    val timeWindow: String
)
