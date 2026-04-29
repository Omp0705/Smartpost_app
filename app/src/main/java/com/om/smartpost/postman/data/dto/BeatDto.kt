package com.om.smartpost.postman.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BeatDto(
    val id: String,
    val officeId: String,
    val officeName: String,
    val beatCode: String,
    val name: String,
    val description: String?,
    val areaKeywords: String?,
    val routeOrder: Int?,
    val active: Boolean?,
    val assignedPostmanId: String?,
    val assignedPostmanEmployeeId: String?,
    val assignedPostmanName: String?,
    val shipmentCount: Int?
)
