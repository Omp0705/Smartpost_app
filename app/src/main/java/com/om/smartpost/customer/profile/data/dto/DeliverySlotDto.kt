package com.om.smartpost.customer.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeliverySlotDto(
    val code: TimeSlot,
    val label: String
)

enum class TimeSlot {
    SLOT_10_12,
    SLOT_12_02,
    SLOT_02_04,
    SLOT_04_06
}
