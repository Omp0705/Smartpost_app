package com.om.smartpost.customer.parcel.data.repository

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.customer.parcel.domain.model.Shipment
import com.om.smartpost.customer.parcel.domain.repository.ShipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.customer.parcel.data.mapper.toShipment

class ShipmentRepositoryImpl(
    private val remoteDataSource: RemoteShipmentDataSource
) : ShipmentRepository {

    private var cachedShipments: List<Shipment> = emptyList()

    override suspend fun getAllShipments(): Flow<Result<List<Shipment>, ApiError>> = flow {
        when (val result = remoteDataSource.getAllShipments()) {
            is Result.Success -> {
                val domainShipments = result.data.map { it.toShipment() }
                cachedShipments = domainShipments
                emit(Result.Success(domainShipments))
            }
            is Result.Error -> {
                emit(Result.Error(result.error))
            }
        }
    }

    override suspend fun getShipmentById(id: String): Flow<Result<Shipment, ApiError>> = flow {
        val shipment = cachedShipments.find { it.id == id }
        if (shipment != null) {
            emit(Result.Success(shipment))
        } else {
            emit(Result.Error(ApiError.ServerError)) // Fallback if missing
        }
    }

    override suspend fun updateShipmentTimeslot(shipmentId: String, newTimeslot: String): Result<Unit, ApiError> {
        return remoteDataSource.updateShipmentTimeslot(shipmentId, newTimeslot)
    }
}