package com.om.smartpost.customer.profile.data.repository

import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.profile.data.dto.ProfileDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

import com.om.smartpost.customer.profile.data.dto.AddressDto
import com.om.smartpost.customer.profile.data.dto.DeliveryPreferencesDto
import com.om.smartpost.customer.profile.data.dto.UpdateDeliveryPreferencesRequest
import com.om.smartpost.customer.profile.domain.model.DeliverySlot
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.delete
import io.ktor.client.request.setBody

class RemoteProfileDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getUserProfile(): Result<ProfileDto, ApiError> {
        return safeCall<ProfileDto> {
            httpClient.get(constructUrl("/api/users/me"))
        }
    }

    suspend fun addAddress(addressDto: AddressDto): Result<Unit, ApiError> {
        return safeCall<Unit> {
            httpClient.post(constructUrl("/api/users/me/addresses")) {
                setBody(addressDto)
            }
        }
    }

    suspend fun updateAddress(id: String, addressDto: AddressDto): Result<Unit, ApiError> {
        return safeCall<Unit> {
            httpClient.patch(constructUrl("/api/users/me/addresses/$id")) {
                setBody(addressDto)
            }
        }
    }

    suspend fun deleteAddress(id: String): Result<Unit, ApiError> {
        return safeCall<Unit> {
            httpClient.delete(constructUrl("/api/users/me/addresses/$id"))
        }
    }

//    suspend fun updateDeliverySlot(slotKey: String): Result<Unit, ApiError> {
//        return safeCall<Unit> {
//            httpClient.put(constructUrl("/api/user/me/delivery-preference")) {
//                setBody(mapOf("slot" to slotKey))
//            }
//        }
//    }

    suspend fun updateDeliveryPreference(
        request: UpdateDeliveryPreferencesRequest
    ): Result<DeliveryPreferencesDto,ApiError> {
        return safeCall<DeliveryPreferencesDto> {
            httpClient.patch(constructUrl("/api/users/me/delivery-preference")) {
                setBody(request)
            }
        }
    }
}
