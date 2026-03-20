package com.om.smartpost.customer.home.presentation.dashboard

import com.om.smartpost.core.presentation.UiText

sealed interface DashboardEvent {
    data class ShowError(val error: UiText) : DashboardEvent
    object NavigateToUpcomingDeliveries : DashboardEvent
    data class NavigateToPackageDetails(val trackingNumber: String) : DashboardEvent
}
