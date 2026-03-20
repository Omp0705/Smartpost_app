package com.om.smartpost.customer.notifications.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NotificationResponseDto(
    val id: Long,
    val title: String,
    val message: String,
    val link: String? = null,
    @SerialName("isRead") val isRead: Boolean,
    val createdAt: String, // Kept as String for kotlinx.serialization
    val type: String
)