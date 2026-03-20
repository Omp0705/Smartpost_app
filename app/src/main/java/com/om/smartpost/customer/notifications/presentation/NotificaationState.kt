package com.om.smartpost.customer.notifications.presentation

import com.om.smartpost.customer.notifications.domain.models.Notification

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
)