package com.om.smartpost.customer.home.presentation.dashboard

import com.om.smartpost.customer.home.domain.model.NotificationItem
import com.om.smartpost.customer.home.domain.model.PackageItem

data class DashboardState(
    val upcomingDeliveries: List<PackageItem> = emptyList(),
    val outForDelivery: List<PackageItem> = emptyList(),
    val recentNotifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
