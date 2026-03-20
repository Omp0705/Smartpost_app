package com.om.smartpost.customer.profile.data.dto

import com.om.smartpost.customer.profile.domain.model.AddressType
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String,
    val type: AddressType,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val pincode: String,
    val isDefault: Boolean
)
