package com.om.smartpost.customer.home.domain.model

data class NotificationItem(
    val title: String, // e.g., "Your parcel IN2024PO7761 is out for delivery"
    val timeAgo: String, // e.g., "10 min ago"
    val isRead: Boolean = false
)
