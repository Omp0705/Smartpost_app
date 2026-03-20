package com.om.smartpost.customer.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeliveryPreferencesRequest(
    val preferredDeliverySlot: DeliverySlotDto? = null,
    val leaveAtDoor: Boolean? = null,
    val leaveWithGuard: Boolean? = null,
    val deliverToNeighbor: Boolean? = null,
    val callBeforeDelivery: Boolean? = null,
    val otpRequired: Boolean? = null,
    val signatureRequired: Boolean? = null,
    val avoidMorning: Boolean? =null,
    val weekendOnly: Boolean? = null,
    val deliveryNote: String? = null
)