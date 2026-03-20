package com.om.smartpost.customer.profile.domain.model

data class DeliveryPreferences(
    val preferredDeliverySlot: DeliverySlot,
    val leaveAtDoor: Boolean = false,
    val leaveWithGuard: Boolean = false,
    val deliverToNeighbor: Boolean = false,
    val callBeforeDelivery: Boolean = false,
    val otpRequired: Boolean = false,
    val signatureRequired: Boolean = false,
    val avoidMorning: Boolean = false,
    val weekendOnly: Boolean = false,
    val deliveryNote: String? = null
)
