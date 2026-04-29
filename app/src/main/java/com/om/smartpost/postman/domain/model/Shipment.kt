package com.om.smartpost.postman.domain.model

import com.om.smartpost.postman.parcels.presentation.PostmanParcelStatus

data class Shipment(
    val id: String,
    val trackingNumber: String,
    val receiverName: String,
    val destinationAddress: String,
    val timeWindow: String,
    val status: PostmanParcelStatus
)
