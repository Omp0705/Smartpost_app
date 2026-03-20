package com.om.smartpost.customer.notifications.domain.models

data class Notification(
    val id: Long,
    val title: String,
    val message: String,
    val isRead: Boolean,
    val timeAgo: String // Formatted in the mapper for the UI
)