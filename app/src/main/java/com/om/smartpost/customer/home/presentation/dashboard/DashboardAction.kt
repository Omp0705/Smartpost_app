package com.om.smartpost.customer.home.presentation.dashboard

sealed interface DashboardAction {
    object OnViewAllUpcomingClick : DashboardAction
    data class OnPackageClick(val trackingNumber: String) : DashboardAction
    data class OnNotificationClick(val notificationId: String) : DashboardAction // Assuming ID or title
}
