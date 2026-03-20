package com.om.smartpost.customer.profile.data.mapper

import com.om.smartpost.customer.profile.data.dto.AddressDto
import com.om.smartpost.customer.profile.data.dto.DeliveryPreferencesDto
import com.om.smartpost.customer.profile.data.dto.DeliverySlotDto
import com.om.smartpost.customer.profile.data.dto.ProfileDto
import com.om.smartpost.customer.profile.data.dto.UpdateDeliveryPreferencesRequest
import com.om.smartpost.customer.profile.domain.model.Address
import com.om.smartpost.customer.profile.domain.model.DeliveryPreferences
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import com.om.smartpost.customer.profile.domain.model.UserProfile

fun AddressDto.toDomain(): Address {
    return Address(
        id = id,
        type = type,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        pincode = pincode,
        isDefault = isDefault
    )
}

fun Address.toDto(): AddressDto {
    return AddressDto(
        id = id,
        type = type,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        pincode = pincode,
        isDefault = isDefault
    )
}


fun ProfileDto.toDomain(): UserProfile {
    return UserProfile(
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profilePictureUrl = profilePictureUrl,
        addresses = addresses.map { it.toDomain() },
        deliveryPreferences = deliveryPreferences?.toDomain()
    )
}

fun DeliveryPreferencesDto.toDomain(): DeliveryPreferences {
    return DeliveryPreferences(
        preferredDeliverySlot = DeliverySlot(
            preferredDeliverySlot.code,
            preferredDeliverySlot.label
        ),
        leaveAtDoor = leaveAtDoor,
        leaveWithGuard = leaveWithGuard,
        deliverToNeighbor = deliverToNeighbor,
        callBeforeDelivery = callBeforeDelivery,
        otpRequired = otpRequired,
        signatureRequired = signatureRequired,
        avoidMorning = avoidMorning,
        weekendOnly = weekendOnly,
        deliveryNote = deliveryNote,
    )
}

fun DeliveryPreferences.toUpdateRequest(): UpdateDeliveryPreferencesRequest {
   return UpdateDeliveryPreferencesRequest(
       preferredDeliverySlot = DeliverySlotDto(preferredDeliverySlot.code,preferredDeliverySlot.label),
       leaveAtDoor = leaveAtDoor,
       leaveWithGuard = leaveWithGuard,
       deliverToNeighbor = deliverToNeighbor,
       callBeforeDelivery = callBeforeDelivery,
       otpRequired = otpRequired,
       signatureRequired = signatureRequired,
       avoidMorning = avoidMorning,
       weekendOnly = weekendOnly,
       deliveryNote = deliveryNote
   )

}

