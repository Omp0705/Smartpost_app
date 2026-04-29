package com.om.smartpost.customer.parcel.domain.repository

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.parcel.domain.model.Shipment
import kotlinx.coroutines.flow.Flow

interface ShipmentRepository {
    suspend fun getAllShipments(): Flow<Result<List<Shipment>,ApiError>>
    suspend fun getShipmentById(id: String): Flow<Result<Shipment,ApiError>>
    suspend fun updateShipmentTimeslot(shipmentId: String, newTimeslot: String): Result<Unit, ApiError>
}
