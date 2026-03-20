package com.om.smartpost.customer.profile.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String? = null,
    val addresses: List<Address> = emptyList(),
    val deliveryPreferences: DeliveryPreferences? = null
)
