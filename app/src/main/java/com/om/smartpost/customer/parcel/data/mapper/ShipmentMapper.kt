package com.om.smartpost.customer.parcel.data.mapper

import com.om.smartpost.customer.parcel.data.dto.ShipmentDto
import com.om.smartpost.customer.parcel.data.dto.TrackingEventDto
import com.om.smartpost.customer.parcel.domain.model.Shipment
import com.om.smartpost.customer.parcel.domain.model.ShipmentStatus
import com.om.smartpost.customer.parcel.domain.model.TrackingEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun ShipmentDto.toShipment(): Shipment {
    // Safely combine first and last names from the payload
    val formattedSenderName = "${senderDetails?.fName.orEmpty()} ${senderDetails?.lName.orEmpty()}".trim()
    val formattedReceiverName = "${receiverDetails?.fName.orEmpty()} ${receiverDetails?.lName.orEmpty()}".trim()

    return Shipment(
        id = id ?: "",
        trackingNumber = trackingNumber ?: "Unknown",
        articleBarcode = articleBarcode,
        serviceType = serviceType ?: "Unknown",
        currentStatus = mapStatus(currentStatus),
        senderId = senderUserId?.toString(),
        receiverId = receiverUserId?.toString(),
        senderName = formattedSenderName.ifEmpty { "Unknown Sender" },
        receiverName = formattedReceiverName.ifEmpty { "Unknown Receiver" },
        receiverAddressLine1 = receiverDetails?.addressLine1 ?: "",
        receiverAddressLine2 = receiverDetails?.addressLine2,
        receiverAddress = listOfNotNull(receiverDetails?.addressLine1, receiverDetails?.addressLine2).joinToString(", "),
        receiverPhone = receiverDetails?.mobileNo ?: "", // Updated to mobileNo
        originPincode = originPincode ?: "",
        originPoName = originPoName ?: "Unknown",
        destinationPincode = destinationPincode ?: "",
        destinationPoName = destinationPoName ?: "Unknown",
        weightKg = weightKg,
        codAmount = codAmount,
        preferredSlot = preferredSlot,
        predictedSlot = predictedSlot,
        bookingDate = parseLocalDate(bookingDate),
        deliveryDate = parseLocalDate(deliveryDate),
        createdAt = parseLocalDateTime(createdAt),
        postmanName = postmanName,
        // Map the timeline list!
        trackingHistory = trackingHistory?.map { it.toTrackingEvent() } ?: emptyList()
    )
}

// Map the individual timeline events
fun TrackingEventDto.toTrackingEvent(): TrackingEvent {
    return TrackingEvent(
        status = mapStatus(this.status),
        description = this.description ?: "",
        location = this.location ?: "Unknown Location",
        timestamp = parseLocalDateTime(this.timestamp)
    )
}

private fun mapStatus(status: String?): ShipmentStatus {
    return try {
        if (status != null) ShipmentStatus.valueOf(status) else ShipmentStatus.PENDING
    } catch (e: Exception) {
        ShipmentStatus.PENDING
    }
}

private fun parseLocalDate(dateStr: String?): LocalDate? {
    return try {
        dateStr?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    } catch (e: Exception) {
        null
    }
}

private fun parseLocalDateTime(dateStr: String?): LocalDateTime {
    return try {
        dateStr?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) } ?: LocalDateTime.now()
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}