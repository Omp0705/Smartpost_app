package com.om.smartpost.customer.parcel.data.repository

import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.parcel.data.dto.ShipmentDto
import com.om.smartpost.customer.parcel.data.dto.UpdateTimeslotRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody

class RemoteShipmentDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getAllShipments(): Result<List<ShipmentDto>, ApiError> {
        return safeCall<List<ShipmentDto>> {
            httpClient.get(constructUrl("/api/v1/shipments/all-shipments"))
        }
    }

    suspend fun updateShipmentTimeslot(shipmentId: String, newTimeslot: String): Result<Unit, ApiError> {
        return safeCall<Unit> {
            httpClient.patch(constructUrl("/api/v1/shipments/$shipmentId/timeslot")) {
                setBody(UpdateTimeslotRequest(timeWindow = newTimeslot))
            }
        }
    }
}