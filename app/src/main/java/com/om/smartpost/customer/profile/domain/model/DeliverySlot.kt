package com.om.smartpost.customer.profile.domain.model

import com.om.smartpost.customer.profile.data.dto.TimeSlot
import java.sql.Time

data class DeliverySlot(
    val code: TimeSlot,
    val label: String
)
