package com.om.smartpost.customer.notifications.presentation

import com.om.smartpost.customer.notifications.domain.models.Notification

sealed interface NotificationsAction {
    data class OnDeleteClick(val id: Long) : NotificationsAction
    data class OnNotificationClick(val notification: Notification) : NotificationsAction
    object OnRefresh : NotificationsAction
    object OnBackClick : NotificationsAction
}