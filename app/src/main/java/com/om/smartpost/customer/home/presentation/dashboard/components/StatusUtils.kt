package com.om.smartpost.customer.home.presentation.dashboard.components

import androidx.compose.ui.graphics.Color

object StatusUtils {

    fun getStatusColors(status: String): Pair<Color, Color> {
        return when (status) {

            // 🟢 Created
            "Created" ->
                Color(0xFFEEEEEE) to Color(0xFF757575)

            // 🔵 Assigned to delivery agent
            "Assigned" ->
                Color(0xFFE3F2FD) to Color(0xFF1976D2)

            // 🟡 Picked up from sender
            "Picked Up" ->
                Color(0xFFE8F5E9) to Color(0xFF388E3C)

            // 🟠 In transit between hubs
            "In Transit" ->
                Color(0xFFFFF3E0) to Color(0xFFF57C00)

            // 🏢 Sorting facility
            "Reached Sorting Center" ->
                Color(0xFFE1F5FE) to Color(0xFF0288D1)

            // 🚚 Out for delivery
            "Out for Delivery" ->
                Color(0xFFFFEBEE) to Color(0xFFD32F2F)

            // ⏳ About to reach
            "Arriving Soon" ->
                Color(0xFFFFF8E1) to Color(0xFFFFA000)

            // ✅ Successfully delivered
            "Delivered" ->
                Color(0xFFE8F5E9) to Color(0xFF2E7D32)

            // ❌ Delivery failed
            "Delivery Failed" ->
                Color(0xFFFFEBEE) to Color(0xFFC62828)

            // 🔁 Rescheduled
            "Rescheduled" ->
                Color(0xFFF3E5F5) to Color(0xFF7B1FA2)

            // 🚫 Cancelled
            "Cancelled" ->
                Color(0xFFFBE9E7) to Color(0xFFD84315)

            // 🔄 Returned
            "Returned to Sender" ->
                Color(0xFFEDE7F6) to Color(0xFF5E35B1)

            else ->
                Color(0xFFF5F5F5) to Color(0xFF616161)
        }
    }
}

