package com.om.smartpost.customer.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String? = null,
    val addresses: List<AddressDto> = emptyList(),
    val preferredDeliveryTime: String? = null,
    val deliveryPreferences: DeliveryPreferencesDto? = null
)
