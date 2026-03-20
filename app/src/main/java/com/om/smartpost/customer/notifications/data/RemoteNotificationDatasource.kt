package com.om.smartpost.customer.notifications.data
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.delete
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.customer.notifications.data.dto.NotificationResponseDto

class RemoteNotificationDataSource(private val client: HttpClient) {
    suspend fun fetchNotifications() = safeCall<List<NotificationResponseDto>> {
        client.get(constructUrl("/api/v1/notifications"))
    }

    suspend fun deleteNotification(id: Long) = safeCall<Unit> {
        client.delete(constructUrl("/api/v1/notifications/$id"))
    }
}