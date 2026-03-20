package com.om.smartpost.customer.parcel.domain.model

import com.om.smartpost.customer.profile.domain.model.Address
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import java.time.LocalDate
import java.time.LocalDateTime

data class Shipment(
    val id: String,
    val trackingNumber: String,
    val articleBarcode: String?,
    val serviceType: String,
    val currentStatus: ShipmentStatus,
    val senderId: String?,
    val receiverId: String?,
    val senderName: String,
    val receiverName: String,
    val receiverAddressLine1: String,
    val receiverAddressLine2: String?,
    val receiverAddress: String,
    val receiverPhone: String,
    val originPincode: String,
    val originPoName: String,
    val destinationPincode: String,
    val destinationPoName: String,
    val weightKg: Double?,
    val codAmount: Double?,
    val preferredSlot: String?,
    val predictedSlot: String?,
    val bookingDate: LocalDate?,
    val deliveryDate: LocalDate?,
    val createdAt: LocalDateTime,
    val postmanName: String?,
    // The timeline data for your UI
    val trackingHistory: List<TrackingEvent>
)

// Data class to represent each dot on the timeline
data class TrackingEvent(
    val status: ShipmentStatus,
    val description: String,
    val location: String,
    val timestamp: LocalDateTime
)

enum class ShipmentStatus(val displayName: String) {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    CREATED("Created"),
    IN_TRANSIT("In Transit"),
    ARRIVED_AT_DESTINATION("Arrived at Destination"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERY_FAILED("Delivery Failed"),
    RETURN_TO_SENDER("Returned to Sender"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

data class DropoffInfo(
    val id: String,
    val locationName: String,
    val address: String
)

enum class AreaType {
    URBAN,
    SUB_URBAN,
    RURAL
}

enum class DeliveryPriority {
    NORMAL,
    SPEED
}

