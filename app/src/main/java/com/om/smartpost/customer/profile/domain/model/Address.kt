package com.om.smartpost.customer.profile.domain.model

data class Address(
    val id: String,
    val type: AddressType, // "Home", "Office", etc.
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val pincode: String,
    val isDefault: Boolean = false
)

enum class AddressType(val serializedName: String) {
    HOME("HOME"),
    WORK("WORK"),
    OTHER("OTHER")
}