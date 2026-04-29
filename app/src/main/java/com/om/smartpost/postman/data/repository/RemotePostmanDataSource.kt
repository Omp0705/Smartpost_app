package com.om.smartpost.postman.data.repository

import com.om.smartpost.core.data.networking.constructUrl
import com.om.smartpost.core.data.networking.safeCall
import com.om.smartpost.core.domain.utils.ApiError
import com.om.smartpost.core.domain.utils.Result
import com.om.smartpost.postman.data.dto.BeatDto
import com.om.smartpost.postman.data.dto.PostmanShipmentDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemotePostmanDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getAssignedBeats(): Result<List<BeatDto>, ApiError> {
        return safeCall<List<BeatDto>> {
            httpClient.get(constructUrl("/api/v1/beats/assigned"))
        }
    }

    suspend fun getShipmentsForBeat(beatId: String): Result<List<PostmanShipmentDto>, ApiError> {
        return safeCall<List<PostmanShipmentDto>> {
            httpClient.get(constructUrl("/api/v1/beats/$beatId/shipments"))
        }
    }
}
