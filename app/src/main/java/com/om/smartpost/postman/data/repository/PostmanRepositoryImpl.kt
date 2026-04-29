package com.om.smartpost.postman.data.repository

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.core.domain.utils.map
import com.om.smartpost.postman.data.dto.BeatDto
import com.om.smartpost.postman.data.dto.PostmanShipmentDto
import com.om.smartpost.postman.domain.model.Beat
import com.om.smartpost.postman.domain.model.Shipment
import com.om.smartpost.postman.domain.repository.PostmanRepository
import com.om.smartpost.postman.parcels.presentation.PostmanParcelStatus

class PostmanRepositoryImpl(
    private val remoteDataSource: RemotePostmanDataSource
) : PostmanRepository {

    override suspend fun getAssignedBeats(): Result<List<Beat>, ApiError> {
        return remoteDataSource.getAssignedBeats()
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override suspend fun getShipmentsForBeat(beatId: String): Result<List<Shipment>, ApiError> {
        return remoteDataSource.getShipmentsForBeat(beatId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }
}

fun BeatDto.toDomain(): Beat {
    return Beat(
        id = id,
        officeName = officeName,
        beatCode = beatCode,
        name = name,
        description = description ?: "",
        assignedPostmanName = assignedPostmanName ?: "",
        shipmentCount = shipmentCount ?: 0
    )
}

fun PostmanShipmentDto.toDomain(): Shipment {
    val status = when (currentStatus) {
        "OUT_FOR_DELIVERY", "PENDING" -> PostmanParcelStatus.PENDING
        "DELIVERED" -> PostmanParcelStatus.DELIVERED
        "FAILED", "CANCELLED", "RETURNED" -> PostmanParcelStatus.FAILED
        else -> PostmanParcelStatus.PENDING
    }
    
    val fName = receiverDetails?.fName ?: ""
    val lName = receiverDetails?.lName ?: ""
    val receiverName = "$fName $lName".trim().takeIf { it.isNotEmpty() } ?: "Unknown"

    val addr1 = receiverDetails?.addressLine1 ?: ""
    val addr2 = receiverDetails?.addressLine2 ?: ""
    val pin = destinationPincode ?: ""
    val addressStr = listOf(addr1, addr2, pin).filter { it.isNotBlank() }.joinToString(", ").takeIf { it.isNotEmpty() } ?: "No Address"
    
    // Combining preferred slot or predicted slot or default
    val timeWindow = preferredSlot ?: predictedSlot ?: "Anytime"

    return Shipment(
        id = id ?: "",
        trackingNumber = trackingNumber ?: "",
        receiverName = receiverName,
        destinationAddress = addressStr,
        timeWindow = timeWindow,
        status = status
    )
}
