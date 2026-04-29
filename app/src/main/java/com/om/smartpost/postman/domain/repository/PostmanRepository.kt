package com.om.smartpost.postman.domain.repository

import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.postman.domain.model.Beat
import com.om.smartpost.postman.domain.model.Shipment

interface PostmanRepository {
    suspend fun getAssignedBeats(): Result<List<Beat>, ApiError>
    suspend fun getShipmentsForBeat(beatId: String): Result<List<Shipment>, ApiError>
}
