package com.om.smartpost.postman.domain.model

data class Beat(
    val id: String,
    val officeName: String,
    val beatCode: String,
    val name: String,
    val description: String,
    val assignedPostmanName: String,
    val shipmentCount: Int
)
