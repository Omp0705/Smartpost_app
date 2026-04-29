package com.om.smartpost.postman.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostmanShipmentDto(
    val id: String?,
    val trackingNumber: String?,
    val articleBarcode: String?,
    val serviceType: String?,
    val currentStatus: String?,
    val destinationPincode: String?,
    val destinationPoName: String?,
    val preferredSlot: String?,
    val predictedSlot: String?,
    val receiverDetails: ContactDto?
)

@Serializable
data class ContactDto(
    val fName: String? = null,
    val lName: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null
)
