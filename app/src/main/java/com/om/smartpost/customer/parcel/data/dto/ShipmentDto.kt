
package com.om.smartpost.customer.parcel.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShipmentDto(
    val id: String?,
    val trackingNumber: String?,
    val articleBarcode: String?,
    val serviceType: String?,
    val currentStatus: String?,
    val senderUserId: Long?,     // Direct integer from payload
    val receiverUserId: Long?,   // Direct integer from payload
    val senderDetails: ContactDto?,
    val receiverDetails: ContactDto?,
    val originPincode: String?,
    val originPoName: String?,
    val destinationPincode: String?,
    val destinationPoName: String?,
    val weightKg: Double?,
    val codAmount: Double?,
    val preferredSlot: String?,
    val predictedSlot: String?,
    val bookingDate: String?,
    val deliveryDate: String?,
    val createdAt: String?,
    val postmanName: String?,    // Extracted directly based on payload
    val trackingHistory: List<TrackingEventDto>? // Array for the timeline
)

@Serializable
data class ContactDto(
    val fName: String?,
    val lName: String?,
    val mobileNo: String?,
    val addressLine1: String?,
    val addressLine2: String?
)

@Serializable
data class TrackingEventDto(
    val status: String?,
    val description: String?,
    val location: String?,
    val timestamp: String?
)