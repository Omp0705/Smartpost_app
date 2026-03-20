package com.om.smartpost.customer.notifications.domain.repository

import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.customer.notifications.domain.models.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<Result<List<Notification>, ApiError>>
    suspend fun deleteNotification(id: Long): Result<Unit, ApiError>
}