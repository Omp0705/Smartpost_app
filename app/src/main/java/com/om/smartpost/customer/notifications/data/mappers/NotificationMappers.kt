package com.om.smartpost.customer.notifications.data.mappers

import com.om.smartpost.customer.notifications.data.dto.NotificationResponseDto
import com.om.smartpost.customer.notifications.domain.models.Notification
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun NotificationResponseDto.toDomain(): Notification {
    return Notification(
        id = this.id,
        title = this.title,
        message = this.message,
        isRead = this.isRead,
        timeAgo = formatTimeAgo(this.createdAt) // Implement your date formatting logic here
    )
}

fun formatTimeAgo(createdAt: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val createdInstant = Instant.from(formatter.parse(createdAt))
        val now = Instant.now()

        val duration = Duration.between(createdInstant, now)

        when {
            duration.seconds < 60 -> "Just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} min ago"
            duration.toHours() < 24 -> "${duration.toHours()} hr ago"
            duration.toDays() < 7 -> "${duration.toDays()} day ago"
            duration.toDays() < 30 -> "${duration.toDays() / 7} week ago"
            duration.toDays() < 365 -> "${duration.toDays() / 30} month ago"
            else -> "${duration.toDays() / 365} year ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}